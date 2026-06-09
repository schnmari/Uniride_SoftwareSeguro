package com.uniride.service;

import com.uniride.model.Motorista;
import com.uniride.model.Usuario;
import com.uniride.repository.UsuarioRepository;

import java.util.Optional;

public class UsuarioService {

    private final UsuarioRepository repository = UsuarioRepository.getInstance();
    private final SenhaService senhaService = SenhaService.getInstance();
    private final LogService log = LogService.getInstance();

    public boolean cadastrar(String nome, String email, String senha, String nascimento) {
        // T1 — fronteira de confiança: revalida as entradas antes de persistir,
        // independentemente de quem chamou (Tampering). Lança IllegalArgumentException
        // para que dado inválido nunca chegue ao repositório.
        validarDadosCadastro(nome, email, senha);

        if (repository.emailJaCadastrado(email)) {
            return false;
        }
        // B1 — senha criptografada com BCrypt antes de armazenar
        String senhaCriptografada = senhaService.criptografar(senha);
        Usuario usuario = new Usuario(repository.proximoId(), nome, email, senhaCriptografada, nascimento);
        repository.salvar(usuario);
        log.registrar("CADASTRO", "email=" + email + " id=" + usuario.getId());
        return true;
    }

    private void validarDadosCadastro(String nome, String email, String senha) {
        if (nome == null || nome.isBlank()
                || email == null || email.isBlank()
                || senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Nome, e-mail e senha são obrigatórios.");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        if (senha.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter ao menos 6 caracteres.");
        }
    }

    public ResultadoLogin autenticar(String email, String senha) {
        // C1 — verifica bloqueio antes de qualquer coisa
        if (repository.estaBloqueado(email)) {
            log.registrar("LOGIN_BLOQUEADO", "email=" + email);
            return ResultadoLogin.BLOQUEADO;
        }

        Optional<Usuario> usuario = repository.findByEmail(email);

        if (usuario.isPresent() && senhaService.verificar(senha, usuario.get().getSenha())) {
            repository.resetarTentativas(email);
            log.registrar("LOGIN_SUCESSO", "email=" + email + " id=" + usuario.get().getId());
            return ResultadoLogin.sucesso(usuario.get());
        }

        repository.registrarTentativaFalha(email);
        int restantes = repository.tentativasRestantes(email);

        if (restantes <= 0) {
            log.registrar("CONTA_BLOQUEADA", "email=" + email + " (excesso de tentativas)");
            return ResultadoLogin.BLOQUEADO;
        }

        log.registrar("LOGIN_FALHA", "email=" + email + " restantes=" + restantes);
        return ResultadoLogin.falha(restantes);
    }

    public boolean habilitarComoMotorista(Usuario usuario, String cpf, String numeroRegistro, String dataVencimento) {
        // T (A2) — fronteira de confiança: revalida os campos antes de processar,
        // independentemente de quem chamou. Dado inválido nunca vira um Motorista.
        validarDadosMotorista(cpf, numeroRegistro, dataVencimento);

        if (usuario.isMotorista()) return false;
        // S (A2) — impede dois usuários se habilitarem com o mesmo CPF
        if (repository.cpfJaCadastrado(cpf)) {
            throw new IllegalArgumentException("CPF já cadastrado para outro motorista.");
        }
        Motorista motorista = new Motorista(cpf, numeroRegistro, dataVencimento);
        usuario.setDadosMotorista(motorista);
        usuario.setMotorista(true);
        log.registrar("VIROU_MOTORISTA", "email=" + usuario.getEmail() + " id=" + usuario.getId());
        return true;
    }

    private void validarDadosMotorista(String cpf, String numeroRegistro, String dataVencimento) {
        if (cpf == null || cpf.isBlank()
                || numeroRegistro == null || numeroRegistro.isBlank()
                || dataVencimento == null || dataVencimento.isBlank()) {
            throw new IllegalArgumentException("CPF, número de registro e data de vencimento são obrigatórios.");
        }
    }

    // Classe interna para representar o resultado do login
    public static class ResultadoLogin {
        public enum Tipo { SUCESSO, FALHA, BLOQUEADO }

        public final Tipo tipo;
        public final Usuario usuario;
        public final int tentativasRestantes;

        private ResultadoLogin(Tipo tipo, Usuario usuario, int tentativasRestantes) {
            this.tipo = tipo;
            this.usuario = usuario;
            this.tentativasRestantes = tentativasRestantes;
        }

        public static ResultadoLogin sucesso(Usuario u) {
            return new ResultadoLogin(Tipo.SUCESSO, u, 0);
        }

        public static ResultadoLogin falha(int restantes) {
            return new ResultadoLogin(Tipo.FALHA, null, restantes);
        }

        public static final ResultadoLogin BLOQUEADO = new ResultadoLogin(Tipo.BLOQUEADO, null, 0);
    }
}

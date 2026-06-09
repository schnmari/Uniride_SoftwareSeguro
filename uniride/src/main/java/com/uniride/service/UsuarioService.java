package com.uniride.service;

import com.uniride.model.Motorista;
import com.uniride.model.Usuario;
import com.uniride.repository.UsuarioRepository;

import java.util.Optional;

public class UsuarioService {

    private final UsuarioRepository repository = UsuarioRepository.getInstance();
    private final SenhaService senhaService = SenhaService.getInstance();

    public boolean cadastrar(String nome, String email, String senha, String nascimento) {
        if (repository.emailJaCadastrado(email)) {
            return false;
        }
        // B1 — senha criptografada com BCrypt antes de armazenar
        String senhaCriptografada = senhaService.criptografar(senha);
        Usuario usuario = new Usuario(repository.proximoId(), nome, email, senhaCriptografada, nascimento);
        repository.salvar(usuario);
        return true;
    }

    public ResultadoLogin autenticar(String email, String senha) {
        // C1 — verifica bloqueio antes de qualquer coisa
        if (repository.estaBloqueado(email)) {
            return ResultadoLogin.BLOQUEADO;
        }

        Optional<Usuario> usuario = repository.findByEmail(email);

        if (usuario.isPresent() && senhaService.verificar(senha, usuario.get().getSenha())) {
            repository.resetarTentativas(email);
            return ResultadoLogin.sucesso(usuario.get());
        }

        repository.registrarTentativaFalha(email);
        int restantes = repository.tentativasRestantes(email);

        if (restantes <= 0) {
            return ResultadoLogin.BLOQUEADO;
        }

        return ResultadoLogin.falha(restantes);
    }

    public boolean habilitarComoMotorista(Usuario usuario, String cpf, String numeroRegistro, String dataVencimento) {
        if (usuario.isMotorista()) return false;
        Motorista motorista = new Motorista(cpf, numeroRegistro, dataVencimento);
        usuario.setDadosMotorista(motorista);
        usuario.setMotorista(true);
        return true;
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

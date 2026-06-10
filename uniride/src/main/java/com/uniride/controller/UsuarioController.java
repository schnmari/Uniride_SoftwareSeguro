package com.uniride.controller;

import com.uniride.model.Usuario;
import com.uniride.service.UsuarioService;
import com.uniride.service.UsuarioService.ResultadoLogin;
import com.uniride.view.MenuView;

public class UsuarioController {

    private final UsuarioService service = new UsuarioService();
    private final MenuView view = new MenuView();

    public void cadastrar() {
        System.out.println("\nCadastro de usuário");
        System.out.println();

        String nome = view.lerTexto("Nome");
        String email = view.lerTexto("E-mail institucional");
        String senha = view.lerSenha("Senha");
        String nascimento = view.lerTexto("Data de nascimento (dd/mm/aaaa)");

        if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
            System.out.println("\nPreencha todos os campos obrigatórios.");
            return;
        }

        if (!email.contains("@")) {
            System.out.println("\nE-mail inválido.");
            return;
        }

        if (senha.length() < 6) {
            System.out.println("\nA senha deve ter ao menos 6 caracteres.");
            return;
        }

        boolean sucesso;
        try {
            sucesso = service.cadastrar(nome, email, senha, nascimento);
        } catch (IllegalArgumentException e) {
            // Rede de segurança: o service rejeitou os dados na fronteira de confiança.
            System.out.println("\n" + e.getMessage());
            return;
        }

        if (sucesso) {
            System.out.println("\nConta criada com sucesso.");
        } else {
            System.out.println("\nEste e-mail já está cadastrado.");
        }
    }

    public Usuario login() {
        System.out.println("\nEntrar na conta");
        System.out.println();

        String email = view.lerTexto("E-mail");
        String senha = view.lerSenha("Senha");

        ResultadoLogin resultado = service.autenticar(email, senha);

        switch (resultado.tipo) {
            case SUCESSO -> {
                System.out.println("\nBem-vindo, " + resultado.usuario.getNome() + ".");
                return resultado.usuario;
            }
            case BLOQUEADO ->
                System.out.println("\nConta bloqueada por excesso de tentativas. Aguarde 15 minutos.");
            case FALHA ->
                System.out.println("\nE-mail ou senha incorretos. Tentativas restantes: " + resultado.tentativasRestantes);
        }

        return null;
    }

    public void tornarMotorista(Usuario usuario) {
        if (usuario.isMotorista()) {
            System.out.println("\nSua conta já está habilitada como motorista.");
            return;
        }

        System.out.println("\nHabilitação como motorista");
        System.out.println();

        String cpf = view.lerTexto("CPF");
        String registro = view.lerTexto("Número de registro da CNH");
        String vencimento = view.lerTexto("Data de vencimento da CNH (dd/mm/aaaa)");

        if (cpf.isBlank() || registro.isBlank() || vencimento.isBlank()) {
            System.out.println("\nPreencha todos os campos.");
            return;
        }

        boolean confirma = view.lerConfirmacao(
                "Confirmo que as informações fornecidas são verdadeiras");
        if (!confirma) {
            System.out.println("\nHabilitação cancelada: confirmação não fornecida.");
            return;
        }

        try {
            service.habilitarComoMotorista(usuario, cpf, registro, vencimento, confirma);
        } catch (IllegalArgumentException e) {
            // Rede de segurança: o service rejeitou os dados na fronteira de confiança.
            System.out.println("\n" + e.getMessage());
            return;
        }
        System.out.println("\nConta habilitada como motorista.");
    }
}

package com.uniride;

import com.uniride.controller.UsuarioController;
import com.uniride.controller.ViagemController;
import com.uniride.model.Usuario;
import com.uniride.view.MenuView;

public class Main {

    public static void main(String[] args) {
        MenuView menu = new MenuView();
        UsuarioController usuarioController = new UsuarioController();
        ViagemController viagemController = new ViagemController();

        Usuario usuarioLogado = null;

        while (true) {
            if (usuarioLogado == null) {
                int opcao = menu.menuInicial();
                switch (opcao) {
                    case 1 -> usuarioLogado = usuarioController.login();
                    case 2 -> usuarioController.cadastrar();
                    case 0 -> { System.out.println("\nAté logo."); return; }
                    default -> System.out.println("\nOpção inválida.");
                }
            } else if (usuarioLogado.isMotorista()) {
                int opcao = menu.menuMotorista(usuarioLogado);
                switch (opcao) {
                    case 1 -> viagemController.listar();
                    case 2 -> viagemController.solicitarCarona(usuarioLogado);
                    case 3 -> viagemController.novaViagem(usuarioLogado);
                    case 4 -> viagemController.gerenciarMinhasViagens(usuarioLogado);
                    case 0 -> { usuarioLogado = null; System.out.println("\nSessão encerrada."); }
                    default -> System.out.println("\nOpção inválida.");
                }
            } else {
                int opcao = menu.menuPassageiro(usuarioLogado);
                switch (opcao) {
                    case 1 -> viagemController.listar();
                    case 2 -> viagemController.solicitarCarona(usuarioLogado);
                    case 3 -> usuarioController.tornarMotorista(usuarioLogado);
                    case 0 -> { usuarioLogado = null; System.out.println("\nSessão encerrada."); }
                    default -> System.out.println("\nOpção inválida.");
                }
            }
        }
    }
}

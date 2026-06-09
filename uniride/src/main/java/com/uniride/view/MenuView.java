package com.uniride.view;

import com.uniride.model.Usuario;

import java.util.Scanner;

public class MenuView {

    private final Scanner scanner = new Scanner(System.in);

    public int menuInicial() {
        System.out.println();
        System.out.println("UniRide — Caronas Universitárias");
        System.out.println();
        System.out.println("  1. Entrar");
        System.out.println("  2. Criar conta");
        System.out.println("  0. Sair");
        System.out.print("\n> ");
        return lerInt();
    }

    public int menuPassageiro(Usuario usuario) {
        System.out.println();
        System.out.println("Olá, " + usuario.getNome() + " — Passageiro");
        System.out.println();
        System.out.println("  1. Ver caronas disponíveis");
        System.out.println("  2. Solicitar carona");
        System.out.println("  3. Habilitar como motorista");
        System.out.println("  0. Sair da conta");
        System.out.print("\n> ");
        return lerInt();
    }

    public int menuMotorista(Usuario usuario) {
        System.out.println();
        System.out.println("Olá, " + usuario.getNome() + " — Motorista");
        System.out.println();
        System.out.println("  1. Ver caronas disponíveis");
        System.out.println("  2. Solicitar carona");
        System.out.println("  3. Publicar oferta de carona");
        System.out.println("  4. Minhas caronas");
        System.out.println("  0. Sair da conta");
        System.out.print("\n> ");
        return lerInt();
    }

    public int lerInt() {
        try {
            String linha = scanner.nextLine().trim();
            return Integer.parseInt(linha);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String lerTexto(String campo) {
        System.out.print(campo + ": ");
        return scanner.nextLine().trim();
    }

    public String lerSenha(String campo) {
        System.out.print(campo + ": ");
        return scanner.nextLine().trim();
    }
}

package com.uniride.controller;

import com.uniride.model.Usuario;
import com.uniride.model.Viagem;
import com.uniride.service.ViagemService;
import com.uniride.view.MenuView;

import java.util.List;
import java.util.Optional;

public class ViagemController {

    private final ViagemService service = new ViagemService();
    private final MenuView view = new MenuView();

    public void listar() {
        List<Viagem> ofertas = service.listarOfertas();
        List<Viagem> solicitacoes = service.listarSolicitacoes();
        System.out.println();

        System.out.println("Ofertas de carona:");
        if (ofertas.isEmpty()) {
            System.out.println("  Nenhuma oferta disponível.");
        } else {
            for (Viagem v : ofertas) System.out.println("  " + v);
        }

        System.out.println();
        System.out.println("Solicitações de carona:");
        if (solicitacoes.isEmpty()) {
            System.out.println("  Nenhuma solicitação disponível.");
        } else {
            for (Viagem v : solicitacoes) System.out.println("  " + v);
        }
    }

    public void solicitarCarona(Usuario usuario) {
        System.out.println("\nSolicitar carona");
        System.out.println();

        String titulo = view.lerTexto("Título");
        String descricao = view.lerTexto("Descrição");
        String partida = view.lerTexto("Ponto de partida");
        String chegada = view.lerTexto("Ponto de chegada");
        String dataHora = view.lerTexto("Data e hora (dd/mm/aaaa hh:mm)");
        String precoStr = view.lerTexto("Quanto pode pagar (R$)");

        if (titulo.isBlank() || partida.isBlank() || chegada.isBlank()) {
            System.out.println("\nPreencha os campos obrigatórios.");
            return;
        }

        int preco;
        try {
            preco = Integer.parseInt(precoStr);
        } catch (NumberFormatException e) {
            System.out.println("\nValor inválido.");
            return;
        }

        try {
            service.cadastrarSolicitacao(titulo, descricao, partida, chegada, dataHora, preco, usuario);
        } catch (IllegalArgumentException e) {
            System.out.println("\n" + e.getMessage());
            return;
        }
        System.out.println("\nSolicitação publicada.");
    }

    public void novaViagem(Usuario usuario) {
        if (!usuario.isMotorista()) {
            System.out.println("\nApenas motoristas podem publicar ofertas de carona.");
            return;
        }

        System.out.println("\nPublicar oferta de carona");
        System.out.println();

        String titulo = view.lerTexto("Título");
        String descricao = view.lerTexto("Descrição");
        String partida = view.lerTexto("Ponto de partida");
        String chegada = view.lerTexto("Ponto de chegada");
        String dataHora = view.lerTexto("Data e hora (dd/mm/aaaa hh:mm)");
        String precoStr = view.lerTexto("Preço (R$)");

        if (titulo.isBlank() || partida.isBlank() || chegada.isBlank()) {
            System.out.println("\nPreencha os campos obrigatórios.");
            return;
        }

        int preco;
        try {
            preco = Integer.parseInt(precoStr);
        } catch (NumberFormatException e) {
            System.out.println("\nPreço inválido.");
            return;
        }

        try {
            service.cadastrarOferta(titulo, descricao, partida, chegada, dataHora, preco, usuario);
        } catch (IllegalArgumentException e) {
            System.out.println("\n" + e.getMessage());
            return;
        }
        System.out.println("\nOferta publicada.");
    }

    public void gerenciarMinhasViagens(Usuario usuario) {
        List<Viagem> ofertas = service.listarOfertasPorUsuario(usuario);
        List<Viagem> solicitacoes = service.listarSolicitacoesPorUsuario(usuario);
        System.out.println();

        if (ofertas.isEmpty() && solicitacoes.isEmpty()) {
            System.out.println("Você não tem caronas publicadas.");
            return;
        }

        if (!ofertas.isEmpty()) {
            System.out.println("Suas ofertas:");
            for (Viagem v : ofertas) System.out.println("  " + v);
        }

        if (!solicitacoes.isEmpty()) {
            System.out.println("\nSuas solicitações:");
            for (Viagem v : solicitacoes) System.out.println("  " + v);
        }

        System.out.println();
        System.out.println("  1. Editar oferta");
        System.out.println("  2. Remover oferta");
        System.out.println("  3. Editar solicitação");
        System.out.println("  4. Remover solicitação");
        System.out.println("  0. Voltar");
        System.out.print("\n> ");

        int opcao = view.lerInt();
        switch (opcao) {
            case 1 -> editarOferta(usuario);
            case 2 -> removerOferta(usuario);
            case 3 -> editarSolicitacao(usuario);
            case 4 -> removerSolicitacao(usuario);
            case 0 -> {}
            default -> System.out.println("Opção inválida.");
        }
    }

    private void editarOferta(Usuario usuario) {
        String idStr = view.lerTexto("ID da oferta");
        Long id;
        try { id = Long.parseLong(idStr); }
        catch (NumberFormatException e) { System.out.println("\nID inválido."); return; }

        Optional<Viagem> viagem = service.buscarOfertaPorId(id);
        if (viagem.isEmpty() || !viagem.get().getUsuario().getId().equals(usuario.getId())) {
            System.out.println("\nOferta não encontrada.");
            return;
        }

        Viagem v = viagem.get();
        System.out.println("\nDeixe em branco para manter o valor atual.\n");
        String titulo = view.lerTexto("Novo título (" + v.getTitulo() + ")");
        String partida = view.lerTexto("Novo ponto de partida (" + v.getPontoPartida() + ")");
        String chegada = view.lerTexto("Novo ponto de chegada (" + v.getPontoChegada() + ")");
        if (!titulo.isBlank()) v.setTitulo(titulo);
        if (!partida.isBlank()) v.setPontoPartida(partida);
        if (!chegada.isBlank()) v.setPontoChegada(chegada);
        service.atualizarOferta(v);
        System.out.println("\nOferta atualizada.");
    }

    private void removerOferta(Usuario usuario) {
        String idStr = view.lerTexto("ID da oferta");
        Long id;
        try { id = Long.parseLong(idStr); }
        catch (NumberFormatException e) { System.out.println("\nID inválido."); return; }
        System.out.println(service.deletarOferta(id, usuario) ? "\nOferta removida." : "\nOferta não encontrada ou sem permissão.");
    }

    private void editarSolicitacao(Usuario usuario) {
        String idStr = view.lerTexto("ID da solicitação");
        Long id;
        try { id = Long.parseLong(idStr); }
        catch (NumberFormatException e) { System.out.println("\nID inválido."); return; }

        Optional<Viagem> viagem = service.buscarSolicitacaoPorId(id);
        if (viagem.isEmpty() || !viagem.get().getUsuario().getId().equals(usuario.getId())) {
            System.out.println("\nSolicitação não encontrada.");
            return;
        }

        Viagem v = viagem.get();
        System.out.println("\nDeixe em branco para manter o valor atual.\n");
        String titulo = view.lerTexto("Novo título (" + v.getTitulo() + ")");
        String partida = view.lerTexto("Novo ponto de partida (" + v.getPontoPartida() + ")");
        String chegada = view.lerTexto("Novo ponto de chegada (" + v.getPontoChegada() + ")");
        if (!titulo.isBlank()) v.setTitulo(titulo);
        if (!partida.isBlank()) v.setPontoPartida(partida);
        if (!chegada.isBlank()) v.setPontoChegada(chegada);
        service.atualizarSolicitacao(v);
        System.out.println("\nSolicitação atualizada.");
    }

    private void removerSolicitacao(Usuario usuario) {
        String idStr = view.lerTexto("ID da solicitação");
        Long id;
        try { id = Long.parseLong(idStr); }
        catch (NumberFormatException e) { System.out.println("\nID inválido."); return; }
        System.out.println(service.deletarSolicitacao(id, usuario) ? "\nSolicitação removida." : "\nSolicitação não encontrada ou sem permissão.");
    }
}

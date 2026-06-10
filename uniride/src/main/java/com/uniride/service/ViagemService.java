package com.uniride.service;

import com.uniride.model.Usuario;
import com.uniride.model.Viagem;
import com.uniride.repository.ViagemRepository;

import java.util.List;
import java.util.Optional;

public class ViagemService {

    private final ViagemRepository repository = ViagemRepository.getInstance();

    public void cadastrarOferta(String titulo, String descricao, String partida, String chegada,
                                String dataHora, int preco, Usuario usuario) {
        // T (A3) — fronteira de confiança: revalida antes de salvar, evitando que
        // dados inválidos/adulterados entrem na listagem, independentemente de quem chamou.
        validarDadosViagem(titulo, partida, chegada, preco);
        Viagem viagem = new Viagem(repository.proximoId(), titulo, descricao,
                partida, chegada, dataHora, preco, "oferta", usuario);
        repository.salvarOferta(viagem);
    }

    public void cadastrarSolicitacao(String titulo, String descricao, String partida, String chegada,
                                     String dataHora, int preco, Usuario usuario) {
        validarDadosViagem(titulo, partida, chegada, preco);
        Viagem viagem = new Viagem(repository.proximoId(), titulo, descricao,
                partida, chegada, dataHora, preco, "solicitação", usuario);
        repository.salvarSolicitacao(viagem);
    }

    private void validarDadosViagem(String titulo, String partida, String chegada, int preco) {
        if (titulo == null || titulo.isBlank()
                || partida == null || partida.isBlank()
                || chegada == null || chegada.isBlank()) {
            throw new IllegalArgumentException("Título, ponto de partida e ponto de chegada são obrigatórios.");
        }
        if (preco < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
    }

    public List<Viagem> listarOfertas() {
        return repository.findAllOfertas();
    }

    public List<Viagem> listarSolicitacoes() {
        return repository.findAllSolicitacoes();
    }

    public List<Viagem> listarOfertasPorUsuario(Usuario usuario) {
        return repository.findOfertasByUsuario(usuario);
    }

    public List<Viagem> listarSolicitacoesPorUsuario(Usuario usuario) {
        return repository.findSolicitacoesByUsuario(usuario);
    }

    public boolean deletarOferta(Long id, Usuario usuario) {
        return repository.deletarOferta(id, usuario);
    }

    public boolean deletarSolicitacao(Long id, Usuario usuario) {
        return repository.deletarSolicitacao(id, usuario);
    }

    public Optional<Viagem> buscarOfertaPorId(Long id) {
        return repository.findOfertaById(id);
    }

    public Optional<Viagem> buscarSolicitacaoPorId(Long id) {
        return repository.findSolicitacaoById(id);
    }

    public void atualizarOferta(Viagem viagem) {
        repository.atualizarOferta(viagem);
    }

    public void atualizarSolicitacao(Viagem viagem) {
        repository.atualizarSolicitacao(viagem);
    }
}

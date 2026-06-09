package com.uniride.repository;

import com.uniride.model.Usuario;
import com.uniride.model.Viagem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ViagemRepository {

    private static final ViagemRepository instancia = new ViagemRepository();
    private final List<Viagem> ofertas = new ArrayList<>();
    private final List<Viagem> solicitacoes = new ArrayList<>();
    private Long proximoId = 1L;

    private ViagemRepository() {}

    public static ViagemRepository getInstance() {
        return instancia;
    }

    public Long proximoId() {
        return proximoId;
    }

    public void salvarOferta(Viagem viagem) {
        ofertas.add(viagem);
        proximoId++;
    }

    public void salvarSolicitacao(Viagem viagem) {
        solicitacoes.add(viagem);
        proximoId++;
    }

    public List<Viagem> findAllOfertas() {
        return new ArrayList<>(ofertas);
    }

    public List<Viagem> findAllSolicitacoes() {
        return new ArrayList<>(solicitacoes);
    }

    public List<Viagem> findOfertasByUsuario(Usuario usuario) {
        return ofertas.stream()
                .filter(v -> v.getUsuario().getId().equals(usuario.getId()))
                .collect(Collectors.toList());
    }

    public List<Viagem> findSolicitacoesByUsuario(Usuario usuario) {
        return solicitacoes.stream()
                .filter(v -> v.getUsuario().getId().equals(usuario.getId()))
                .collect(Collectors.toList());
    }

    public Optional<Viagem> findOfertaById(Long id) {
        return ofertas.stream().filter(v -> v.getId().equals(id)).findFirst();
    }

    public Optional<Viagem> findSolicitacaoById(Long id) {
        return solicitacoes.stream().filter(v -> v.getId().equals(id)).findFirst();
    }

    public boolean deletarOferta(Long id, Usuario usuario) {
        return ofertas.removeIf(v -> v.getId().equals(id) && v.getUsuario().getId().equals(usuario.getId()));
    }

    public boolean deletarSolicitacao(Long id, Usuario usuario) {
        return solicitacoes.removeIf(v -> v.getId().equals(id) && v.getUsuario().getId().equals(usuario.getId()));
    }

    public void atualizarOferta(Viagem atualizada) {
        for (int i = 0; i < ofertas.size(); i++) {
            if (ofertas.get(i).getId().equals(atualizada.getId())) {
                ofertas.set(i, atualizada);
                return;
            }
        }
    }

    public void atualizarSolicitacao(Viagem atualizada) {
        for (int i = 0; i < solicitacoes.size(); i++) {
            if (solicitacoes.get(i).getId().equals(atualizada.getId())) {
                solicitacoes.set(i, atualizada);
                return;
            }
        }
    }
}

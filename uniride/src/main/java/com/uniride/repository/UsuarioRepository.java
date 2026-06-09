package com.uniride.repository;

import com.uniride.model.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UsuarioRepository {

    private static final UsuarioRepository instancia = new UsuarioRepository();
    private final List<Usuario> usuarios = new ArrayList<>();
    private final Map<String, Integer> tentativas = new HashMap<>();
    private final Map<String, Long> bloqueios = new HashMap<>();
    private Long proximoId = 1L;

    private static final int MAX_TENTATIVAS = 5;
    private static final long TEMPO_BLOQUEIO_MS = 15 * 60 * 1000L;

    private UsuarioRepository() {}

    public static UsuarioRepository getInstance() {
        return instancia;
    }

    public void salvar(Usuario usuario) {
        usuarios.add(usuario);
        proximoId++;
    }

    public Long proximoId() {
        return proximoId;
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    public List<Usuario> findAll() {
        return new ArrayList<>(usuarios);
    }

    public boolean emailJaCadastrado(String email) {
        return usuarios.stream().anyMatch(u -> u.getEmail().equals(email));
    }

    public boolean estaBloqueado(String email) {
        if (!bloqueios.containsKey(email)) return false;
        long desbloqueioEm = bloqueios.get(email);
        if (System.currentTimeMillis() < desbloqueioEm) return true;
        bloqueios.remove(email);
        tentativas.remove(email);
        return false;
    }

    public void registrarTentativaFalha(String email) {
        int total = tentativas.getOrDefault(email, 0) + 1;
        tentativas.put(email, total);
        if (total >= MAX_TENTATIVAS) {
            bloqueios.put(email, System.currentTimeMillis() + TEMPO_BLOQUEIO_MS);
        }
    }

    public void resetarTentativas(String email) {
        tentativas.remove(email);
        bloqueios.remove(email);
    }

    public int tentativasRestantes(String email) {
        return MAX_TENTATIVAS - tentativas.getOrDefault(email, 0);
    }
}

package com.uniride.repository;

import com.uniride.model.Usuario;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Req. B2 (busca tipada): findByEmail retorna o usuário correto e não quebra
 * com entradas adversas (e-mail inexistente).
 */
class UsuarioRepositoryTest {

    private final UsuarioRepository repository = UsuarioRepository.getInstance();

    @Test
    void findByEmailRetornaUsuarioCorretoENaoQuebraComEntradaInexistente() {
        String email = "busca_" + System.nanoTime() + "@pucpr.edu.br";
        Usuario usuario = new Usuario(repository.proximoId(), "Rafael", email, "hash", "01/01/2000");
        repository.salvar(usuario);

        Optional<Usuario> encontrado = repository.findByEmail(email);
        assertTrue(encontrado.isPresent(), "Deve encontrar o usuário pelo e-mail.");
        assertEquals(email, encontrado.get().getEmail());

        Optional<Usuario> inexistente = repository.findByEmail("naoexiste_" + System.nanoTime() + "@x.com");
        assertTrue(inexistente.isEmpty(), "E-mail inexistente deve retornar Optional vazio, sem lançar erro.");
    }
}

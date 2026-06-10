package com.uniride.service;

import com.uniride.model.Usuario;
import com.uniride.model.Viagem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Req. C2 (CWE-639, autorização) + A3: um usuário não consegue excluir a carona
 * de outro usuário. No código, a autorização é verificada por dono do recurso:
 * deletarOferta só remove quando o id do usuário bate com o dono da viagem,
 * retornando false caso contrário.
 */
class ViagemServiceTest {

    private final ViagemService service = new ViagemService();

    @Test
    void usuarioNaoExcluiCaronaDeOutroUsuario() {
        // Ids únicos e altos evitam colisão com viagens de outros testes (repositório singleton).
        Usuario dono = new Usuario(900001L, "Dono", "dono@pucpr.edu.br", "hash", "01/01/2000");
        Usuario atacante = new Usuario(900002L, "Atacante", "atacante@pucpr.edu.br", "hash", "01/01/2000");

        service.cadastrarOferta("Carona Centro", "desc", "A", "B", "10/06 08:00", 10, dono);

        List<Viagem> doDono = service.listarOfertasPorUsuario(dono);
        assertEquals(1, doDono.size(), "O dono deve ter exatamente a oferta criada.");
        Long idOferta = doDono.get(0).getId();

        assertFalse(service.deletarOferta(idOferta, atacante),
                "Usuário que não é dono não pode excluir a carona de outro.");
        assertTrue(service.buscarOfertaPorId(idOferta).isPresent(),
                "A carona deve continuar existindo após tentativa não autorizada.");

        assertTrue(service.deletarOferta(idOferta, dono),
                "O próprio dono pode excluir sua carona.");
    }
}

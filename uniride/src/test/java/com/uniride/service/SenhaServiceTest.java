package com.uniride.service;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Req. B1 (BCrypt) + A1: a senha é armazenada como hash, nunca em texto claro,
 * e a verificação confirma apenas a senha original.
 */
class SenhaServiceTest {

    private final SenhaService senhaService = SenhaService.getInstance();

    @Test
    void senhaArmazenadaComoHashBCryptVerificavelENuncaEmTextoClaro() {
        String hash = senhaService.criptografar("senha123");

        assertNotEquals("senha123", hash, "O hash não pode ser igual à senha em texto claro.");
        assertTrue(hash.startsWith("$2"), "Deve ser um hash BCrypt.");
        assertTrue(BCrypt.checkpw("senha123", hash), "BCrypt deve confirmar a senha original.");

        assertTrue(senhaService.verificar("senha123", hash), "Senha correta deve ser aceita.");
        assertFalse(senhaService.verificar("senhaErrada", hash), "Senha incorreta deve ser rejeitada.");
    }
}

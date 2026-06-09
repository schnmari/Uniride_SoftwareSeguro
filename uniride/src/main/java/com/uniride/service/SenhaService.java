package com.uniride.service;

import org.mindrot.jbcrypt.BCrypt;

public class SenhaService {

    private static final SenhaService instancia = new SenhaService();

    private SenhaService() {}

    public static SenhaService getInstance() {
        return instancia;
    }

    public String criptografar(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public boolean verificar(String senha, String hash) {
        return BCrypt.checkpw(senha, hash);
    }
}

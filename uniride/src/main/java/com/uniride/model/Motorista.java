package com.uniride.model;

public class Motorista {

    private String cpf;
    private String numeroRegistro;
    private String dataVencimento;

    public Motorista(String cpf, String numeroRegistro, String dataVencimento) {
        this.cpf = cpf;
        this.numeroRegistro = numeroRegistro;
        this.dataVencimento = dataVencimento;
    }

    public String getCpf() { return cpf; }
    public String getNumeroRegistro() { return numeroRegistro; }
    public String getDataVencimento() { return dataVencimento; }
}

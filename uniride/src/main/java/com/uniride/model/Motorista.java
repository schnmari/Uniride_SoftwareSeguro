package com.uniride.model;

public class Motorista {

    private String cpf;
    private String numeroRegistro;
    private String dataVencimento;
    // Comprovante de não-repúdio: hash que amarra os dados + identidade + data + a confirmação.
    private String comprovanteAceite;

    public Motorista(String cpf, String numeroRegistro, String dataVencimento) {
        this.cpf = cpf;
        this.numeroRegistro = numeroRegistro;
        this.dataVencimento = dataVencimento;
    }

    public String getCpf() { return cpf; }
    public String getNumeroRegistro() { return numeroRegistro; }
    public String getDataVencimento() { return dataVencimento; }
    public String getComprovanteAceite() { return comprovanteAceite; }
    public void setComprovanteAceite(String comprovanteAceite) { this.comprovanteAceite = comprovanteAceite; }
}

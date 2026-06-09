package com.uniride.model;

public class Usuario {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String nascimento;
    private boolean motorista;
    private Motorista dadosMotorista;

    public Usuario(Long id, String nome, String email, String senha, String nascimento) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.nascimento = nascimento;
        this.motorista = false;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getNascimento() { return nascimento; }
    public boolean isMotorista() { return motorista; }
    public void setMotorista(boolean motorista) { this.motorista = motorista; }
    public Motorista getDadosMotorista() { return dadosMotorista; }
    public void setDadosMotorista(Motorista dadosMotorista) { this.dadosMotorista = dadosMotorista; }

    @Override
    public String toString() {
        return nome + " <" + email + ">" + (motorista ? " [Motorista]" : " [Passageiro]");
    }
}

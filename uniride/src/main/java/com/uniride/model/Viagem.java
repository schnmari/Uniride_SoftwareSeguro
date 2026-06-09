package com.uniride.model;

public class Viagem {

    private Long id;
    private String titulo;
    private String descricao;
    private String pontoPartida;
    private String pontoChegada;
    private String dataHora;
    private int preco;
    private String tipoCarona;
    private Usuario usuario;

    public Viagem(Long id, String titulo, String descricao, String pontoPartida,
                  String pontoChegada, String dataHora, int preco, String tipoCarona, Usuario usuario) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.pontoPartida = pontoPartida;
        this.pontoChegada = pontoChegada;
        this.dataHora = dataHora;
        this.preco = preco;
        this.tipoCarona = tipoCarona;
        this.usuario = usuario;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getPontoPartida() { return pontoPartida; }
    public void setPontoPartida(String pontoPartida) { this.pontoPartida = pontoPartida; }
    public String getPontoChegada() { return pontoChegada; }
    public void setPontoChegada(String pontoChegada) { this.pontoChegada = pontoChegada; }
    public String getDataHora() { return dataHora; }
    public void setDataHora(String dataHora) { this.dataHora = dataHora; }
    public int getPreco() { return preco; }
    public void setPreco(int preco) { this.preco = preco; }
    public String getTipoCarona() { return tipoCarona; }
    public void setTipoCarona(String tipoCarona) { this.tipoCarona = tipoCarona; }
    public Usuario getUsuario() { return usuario; }

    @Override
    public String toString() {
        return "[" + id + "] " + titulo + " | " + pontoPartida + " -> " + pontoChegada +
               " | " + dataHora + " | R$" + preco + " | " + usuario.getNome();
    }
}

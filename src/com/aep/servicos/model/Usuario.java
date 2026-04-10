package com.aep.servicos.model;

public class Usuario {

    protected String nome;
    protected String contato;

    public Usuario(String nome, String contato) {
        this.nome = nome;
        this.contato = contato;
    }

    public String getNome() { return nome; }
    public String getContato() { return contato; }

    @Override
    public String toString() {
        return "Usuário: " + nome + " - Contato: " + contato;
    }
}
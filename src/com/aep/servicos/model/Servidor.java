package com.aep.servicos.model;

public class Servidor extends Usuario {

    private final String setor;

    public Servidor(String nome, String contato, String setor) {
        super(nome, contato);
        this.setor = setor;
    }

    public String getSetor() { return setor; }

    @Override
    public String toString() {
        return "Servidor: " + nome + " - Setor: " + setor + " - Contato: " + contato;
    }
}
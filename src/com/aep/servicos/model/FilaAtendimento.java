package com.aep.servicos.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FilaAtendimento {

    private final List<Solicitacao> fila = new ArrayList<>();

    public void adicionar(Solicitacao s) {
        fila.add(s);
    }

    public List<Solicitacao> listarOrdenadaPorPrioridade() {
        return fila.stream()
                .sorted(Comparator.comparing(Solicitacao::getPrioridade).reversed())
                .toList();
    }

    public List<Solicitacao> listarTodas() {
        return new ArrayList<>(fila);
    }

    public List<Solicitacao> filtrarPorCategoria(Categoria categoria) {
        return fila.stream()
                .filter(s -> s.getCategoria() == categoria)
                .toList();
    }

    public List<Solicitacao> filtrarPorPrioridade(Prioridade prioridade) {
        return fila.stream()
                .filter(s -> s.getPrioridade() == prioridade)
                .toList();
    }

    public List<Solicitacao> filtrarPorLocalizacao(String local) {
        return fila.stream()
                .filter(s -> s.getLocalizacao().equalsIgnoreCase(local))
                .toList();
    }

    public Solicitacao buscarPorProtocolo(String protocolo) {
        return fila.stream()
                .filter(s -> s.getProtocolo().equals(protocolo))
                .findFirst()
                .orElse(null);
    }
}
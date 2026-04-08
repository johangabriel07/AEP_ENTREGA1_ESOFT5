package com.aep.servicos.repository;

import com.aep.servicos.model.*;

import java.util.ArrayList;
import java.util.List;

public class SolicitacaoRepository {

    private final List<Solicitacao> lista = new ArrayList<>();

    public void salvar(Solicitacao s) {
        lista.add(s);
    }

    public List<Solicitacao> listar() {
        return new ArrayList<>(lista);
    }

    public Solicitacao buscarPorProtocolo(String protocolo) {
        return lista.stream()
                .filter(s -> s.getProtocolo().equals(protocolo))
                .findFirst()
                .orElse(null);
    }

    public List<Solicitacao> filtrarPorPrioridade(Prioridade prioridade) {
        return lista.stream()
                .filter(s -> s.getPrioridade() == prioridade)
                .toList();
    }

    public List<Solicitacao> filtrarPorCategoria(Categoria categoria) {
        return lista.stream()
                .filter(s -> s.getCategoria() == categoria)
                .toList();
    }

    public List<Solicitacao> filtrarPorLocalizacao(String local) {
        return lista.stream()
                .filter(s -> s.getLocalizacao().equalsIgnoreCase(local))
                .toList();
    }

    public List<Solicitacao> listarOrdenadaPorPrioridade() {
        return lista.stream()
                .sorted((a, b) -> b.getPrioridade().compareTo(a.getPrioridade()))
                .toList();
    }
}
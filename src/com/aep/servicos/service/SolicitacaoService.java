package com.aep.servicos.service;

import com.aep.servicos.model.*;
import com.aep.servicos.repository.SolicitacaoRepository;

import java.util.List;
import java.util.UUID;

public class SolicitacaoService {

    private final SolicitacaoRepository repo = new SolicitacaoRepository();

    public String criar(String descricao, String localizacao,
                        Categoria categoria, Prioridade prioridade,
                        boolean anonimo, String nome, String contato) {

        validarDescricao(descricao, anonimo);
        validarLocalizacao(localizacao);

        if (!anonimo) {
            validarIdentificacao(nome, contato);
        }

        String protocolo = gerarProtocolo();

        Solicitacao s = new Solicitacao(
                protocolo, categoria, descricao, localizacao, prioridade, anonimo, nome, contato
        );

        repo.salvar(s);
        registrarLog(protocolo, anonimo, categoria);

        return protocolo;
    }

    public List<Solicitacao> listar() {
        return repo.listar();
    }

    public Solicitacao buscar(String protocolo) {
        return repo.buscarPorProtocolo(protocolo);
    }

    public void atualizarStatus(String protocolo, Status novoStatus,
                                String comentario, String responsavel) {

        Solicitacao s = buscar(protocolo);

        if (s == null) throw new IllegalArgumentException("Solicitação não encontrada.");

        if (!fluxoValido(s.getStatus(), novoStatus)) {
            throw new IllegalStateException(
                    "Mudança invalida: " + s.getStatus() + " → " + novoStatus
            );
        }

        validarComentario(comentario);
        validarResponsavel(responsavel);

        s.atualizarStatus(novoStatus, comentario, responsavel);
    }

    public List<Solicitacao> listarPorPrioridade(Prioridade prioridade) {
        return repo.filtrarPorPrioridade(prioridade);
    }

    public List<Solicitacao> listarPorCategoria(Categoria categoria) {
        return repo.filtrarPorCategoria(categoria);
    }

    public List<Solicitacao> listarPorLocalizacao(String local) {
        return repo.filtrarPorLocalizacao(local);
    }

    public List<Solicitacao> filaOrdenada() {
        return repo.listarOrdenadaPorPrioridade();
    }

    private void validarDescricao(String desc, boolean anonimo) {
        if (desc == null || desc.isBlank()) {
            throw new IllegalArgumentException("Descrição é obrigatoria.");
        }
    }

    private void validarLocalizacao(String loc) {
        if (loc == null || loc.isBlank()) {
            throw new IllegalArgumentException("Localização é obrigatoria.");
        }
    }

    private void validarIdentificacao(String nome, String contato) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatorio para solicitações identificadas.");
        }
        if (contato == null || contato.isBlank()) {
            throw new IllegalArgumentException("Contato é obrigatorio para solicitações identificadas.");
        }
    }

    private void validarComentario(String comentario) {
        if (comentario == null || comentario.isBlank()) {
            throw new IllegalArgumentException("Comentário é obrigatorio ao atualizar status.");
        }
    }

    private void validarResponsavel(String responsavel) {
        if (responsavel == null || responsavel.isBlank()) {
            throw new IllegalArgumentException("Nome do responsável é obrigatório.");
        }
    }

    private boolean fluxoValido(Status atual, Status novo) {
        return switch (atual) {
            case ABERTO      -> novo == Status.TRIAGEM;
            case TRIAGEM     -> novo == Status.EM_EXECUCAO;
            case EM_EXECUCAO -> novo == Status.RESOLVIDO;
            case RESOLVIDO   -> novo == Status.ENCERRADO;
            case ENCERRADO   -> false;
        };
    }

    private void registrarLog(String protocolo, boolean anonimo, Categoria categoria) {
        System.out.println("[LOG] Nova solicitação registrada | Protocolo: " + protocolo
                + " | Anônimo: " + anonimo
                + " | Categoria: " + categoria);
    }

    private String gerarProtocolo() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
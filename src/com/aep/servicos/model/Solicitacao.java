package com.aep.servicos.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Solicitacao {

    private final String protocolo;
    private final Categoria categoria;
    private final String descricao;
    private final String localizacao;
    private final boolean anonimo;
    private final String nomeRequerente;
    private final String contatoRequerente;
    private final LocalDateTime dataCriacao;
    private final int prazoAlvo;

    private Status status;
    private final List<HistoricoStatus> historico;

    public Solicitacao(String protocolo, Categoria categoria, String descricao,
                       String localizacao, Prioridade prioridade,
                       boolean anonimo, String nomeRequerente, String contatoRequerente) {

        this.protocolo = protocolo;
        this.categoria = categoria;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.anonimo = anonimo;
        this.nomeRequerente = anonimo ? null : nomeRequerente;
        this.contatoRequerente = anonimo ? null : contatoRequerente;
        this.status = Status.ABERTO;
        this.dataCriacao = LocalDateTime.now();
        this.prazoAlvo = calcularPrazo(prioridade);
        this.historico = new ArrayList<>();

        historico.add(new HistoricoStatus(Status.ABERTO, "Solicitação criada", "Sistema"));
    }

    private int calcularPrazo(Prioridade prioridade) {
        return switch (prioridade) {
            case ALTA  -> 24;
            case MEDIA -> 48;
            case BAIXA -> 72;
        };
    }

    public void atualizarStatus(Status novoStatus, String comentario, String responsavel) {
        this.status = novoStatus;
        historico.add(new HistoricoStatus(novoStatus, comentario, responsavel));
    }

    public void exibirHistorico() {
        historico.forEach(System.out::println);
    }

    public String resumoCompleto() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        boolean atrasado = estaAtrasado();
        StringBuilder sb = new StringBuilder();
        sb.append("Protocolo  : ").append(protocolo).append("\n");
        sb.append("Categoria  : ").append(categoria).append("\n");
        sb.append("Status     : ").append(status).append("\n");
        sb.append("Localização: ").append(localizacao).append("\n");
        sb.append("Criado em  : ").append(dataCriacao.format(fmt)).append("\n");
        sb.append("Prazo alvo : ").append(prazoAlvo).append(" horas\n");
        sb.append("Situação   : ").append(atrasado ? "⚠ ATRASADO" : "Dentro do prazo").append("\n");
        if (atrasado) {
            sb.append("Justificativa de atraso: verificar com equipe responsável.\n");
        }
        if (!anonimo && nomeRequerente != null) {
            sb.append("Requerente : ").append(nomeRequerente).append("\n");
        } else {
            sb.append("Requerente : Anônimo\n");
        }
        return sb.toString();
    }

    public boolean estaAtrasado() {
        if (status == Status.RESOLVIDO || status == Status.ENCERRADO) return false;
        long horasPassadas = java.time.Duration.between(dataCriacao, LocalDateTime.now()).toHours();
        return horasPassadas > prazoAlvo;
    }

    public String getProtocolo()       { return protocolo; }
    public Status getStatus()          { return status; }
    public Categoria getCategoria()    { return categoria; }
    public Prioridade getPrioridade()  {
        return prazoAlvo == 24 ? Prioridade.ALTA : prazoAlvo == 48 ? Prioridade.MEDIA : Prioridade.BAIXA;
    }
    public String getLocalizacao()     { return localizacao; }
    public int getPrazoAlvo()          { return prazoAlvo; }
    public boolean isAnonimo()         { return anonimo; }

    @Override
    public String toString() {
        return "[" + protocolo + "] " + categoria
                + " | " + getPrioridade()
                + " | " + status
                + " | " + localizacao
                + (estaAtrasado() ? "  ATRASADO" : "");
    }
}
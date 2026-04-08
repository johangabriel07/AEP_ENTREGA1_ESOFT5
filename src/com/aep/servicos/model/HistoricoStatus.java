package com.aep.servicos.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoricoStatus {

    private final LocalDateTime data;
    private final Status status;
    private final String comentario;
    private final String responsavel;

    public HistoricoStatus(Status status, String comentario, String responsavel) {
        this.data = LocalDateTime.now();
        this.status = status;
        this.comentario = comentario;
        this.responsavel = responsavel;
    }

    public Status getStatus() { return status; }
    public String getComentario() { return comentario; }
    public String getResponsavel() { return responsavel; }
    public LocalDateTime getData() { return data; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return data.format(fmt)
                + " | " + status
                + " | Responsável: " + responsavel
                + " | " + comentario;
    }
}
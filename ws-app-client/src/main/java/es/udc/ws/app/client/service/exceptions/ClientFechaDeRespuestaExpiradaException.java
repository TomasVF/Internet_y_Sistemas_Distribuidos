package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

public class ClientFechaDeRespuestaExpiradaException extends Exception{
    private Long eventoId;
    private LocalDateTime fechaAlta;

    public ClientFechaDeRespuestaExpiradaException(Long eventoId, LocalDateTime fechaAlta) {
        super("Ya no se puede responder al evento con id=\"" + eventoId +
                "\" han pasado más de 24h desde la fecha de alta= \"" +
                fechaAlta + "\")");
        this.eventoId = eventoId;
        this.fechaAlta = fechaAlta;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public LocalDateTime getfechaAlta() {
        return fechaAlta;
    }

    public void setfechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }
}
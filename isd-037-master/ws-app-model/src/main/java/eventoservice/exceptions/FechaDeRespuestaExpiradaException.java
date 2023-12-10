package eventoservice.exceptions;

import java.time.LocalDateTime;

public class FechaDeRespuestaExpiradaException extends Exception{
    private Long eventoId;
    private LocalDateTime fechaALta;

    public FechaDeRespuestaExpiradaException(Long eventoId, LocalDateTime fechaALta) {
        super("Ya no se puede responder al evento con id=\"" + eventoId +
                "\" han pasado más de 24h desde la fecha de alta= \"" +
                fechaALta + "\")");
        this.eventoId = eventoId;
        this.fechaALta = fechaALta;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public LocalDateTime getFechaALta() {
        return fechaALta;
    }

    public void setFechaALta(LocalDateTime fechaALta) {
        this.fechaALta = fechaALta;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }
}

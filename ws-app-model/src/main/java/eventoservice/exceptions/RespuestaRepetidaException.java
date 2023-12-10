package eventoservice.exceptions;

public class RespuestaRepetidaException extends Exception{
    private Long eventoId;

    public RespuestaRepetidaException(Long eventoId) {
        super("No puedes responder otra vez al evento con id=\"" + eventoId);
        this.eventoId = eventoId;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }
}

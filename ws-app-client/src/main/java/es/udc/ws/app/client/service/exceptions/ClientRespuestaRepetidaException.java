package es.udc.ws.app.client.service.exceptions;

public class ClientRespuestaRepetidaException extends Exception{
    private Long eventoId;

    public ClientRespuestaRepetidaException(Long eventoId) {
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

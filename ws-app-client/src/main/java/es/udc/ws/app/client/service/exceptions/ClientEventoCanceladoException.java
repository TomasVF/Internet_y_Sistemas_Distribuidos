package es.udc.ws.app.client.service.exceptions;

public class ClientEventoCanceladoException extends Exception{
    private Long eventoId;

    public ClientEventoCanceladoException(Long eventoId) {
        super("Evento con id=\"" + eventoId + "\n ya está cancelado");
        this.eventoId = eventoId;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }
}

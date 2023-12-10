package es.udc.ws.app.client.service.exceptions;

public class ClientEventoCelebradoException extends Exception {
    private Long eventoId;

    public ClientEventoCelebradoException(Long eventoId) {
        super("Evento con id=\"" + eventoId + "\n ya fue celebrado");
        this.eventoId = eventoId;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

}

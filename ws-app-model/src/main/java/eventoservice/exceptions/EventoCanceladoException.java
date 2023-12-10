package eventoservice.exceptions;

public class EventoCanceladoException extends Exception {
    private Long eventoId;

    public EventoCanceladoException(Long eventoId) {
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

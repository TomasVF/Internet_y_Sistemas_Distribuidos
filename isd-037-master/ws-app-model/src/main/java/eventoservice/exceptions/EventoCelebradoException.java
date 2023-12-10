package eventoservice.exceptions;

public class EventoCelebradoException extends Exception {
    private Long eventoId;

    public EventoCelebradoException(Long eventoId) {
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

package dto;

import java.time.LocalDateTime;

public class RestReservaDto {
    private Long reservaId;
    private Long eventoId;
    private String empemail;
    private boolean asistencia;

    //constructor
    public RestReservaDto(Long eventoId,String empemail,boolean asistencia) {
        this.eventoId=eventoId;
        this.empemail=empemail;
        this.asistencia=asistencia;

    }
    public RestReservaDto(Long reservaId, Long eventoId,String empemail,boolean asistencia) {
        this(eventoId, empemail, asistencia);
        this.reservaId = reservaId;

    }
    public boolean isAsistencia() {
        return asistencia;
    }

    public void setAsistencia(boolean asistencia) {
        this.asistencia = asistencia;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public String getEmpemail() {
        return empemail;
    }

    public void setEmpemail(String empemail) {
        this.empemail = empemail;
    }

    public Long getReservaId() {
        return reservaId;
    }

    public void setReservaId(Long reservaId) {
        this.reservaId = reservaId;
    }
}

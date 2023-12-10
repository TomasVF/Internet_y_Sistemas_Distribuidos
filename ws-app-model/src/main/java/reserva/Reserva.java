package reserva;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reserva {
    private long reservaId;
    private long eventoId;
    private String empemail;
    private LocalDateTime fechaemail;
    private boolean asistencia;

    //constructor
    public Reserva(long eventoId,String empemail,LocalDateTime fechaemail,boolean asistencia) {
    this.eventoId=eventoId;
    this.empemail=empemail;
    this.fechaemail=fechaemail;
    this.asistencia=asistencia;

    }

    public Reserva(long reservaId, long eventoId,String empemail,LocalDateTime fechaemail,boolean asistencia) {
        this(eventoId, empemail, fechaemail, asistencia);
        this.reservaId = reservaId;

    }

    public Reserva(long reservaId, long eventoId,String empemail,boolean asistencia){
        this.eventoId=eventoId;
        this.empemail=empemail;
        this.asistencia=asistencia;
        this.reservaId=reservaId;
    }





    public boolean isAsistencia() {
        return asistencia;
    }

    public void setAsistencia(boolean asistencia) {
        this.asistencia = asistencia;
    }

    public long getEventoId() {
        return eventoId;
    }

    public void setEventoId(long eventoId) {
        this.eventoId = eventoId;
    }

    public LocalDateTime getFechaemail() {
        return fechaemail;
    }

    public void setFechaemail(LocalDateTime fechaemail) {
        this.fechaemail = fechaemail;
    }

    public String getEmpemail() {
        return empemail;
    }

    public void setEmpemail(String empemail) {
        this.empemail = empemail;
    }

    public long getReservaId() {
        return reservaId;
    }

    public void setReservaId(long reservaId) {
        this.reservaId = reservaId;
    }
 //equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva)) return false;
        Reserva reserva = (Reserva) o;
        return getEventoId() == reserva.getEventoId() && getReservaId() == reserva.getReservaId() && isAsistencia() == reserva.isAsistencia() && Objects.equals(getEmpemail(), reserva.getEmpemail()) && Objects.equals(getFechaemail(), reserva.getFechaemail());
    }
 //hashcode
    @Override
    public int hashCode() {
        return Objects.hash(getReservaId(), getEventoId(),  getEmpemail(), getFechaemail(), isAsistencia());
    }
}

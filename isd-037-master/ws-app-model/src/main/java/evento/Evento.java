package evento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Evento {
    private Long eventId;
    private String nombre, descr;
    private LocalDateTime fecha, fechaAlta;
    private int numEmpS, numEmpN, duracion;
    private boolean cancelled;

    public Evento(String nombre, String descr, LocalDateTime fecha, int duracion){
        this.nombre=nombre;
        this.fecha=fecha;
        this.descr =descr;
        this.duracion=duracion;
    }

    public Evento(Long eventId, String nombre, LocalDateTime fecha, String
            desc, int numEmpS, int numEmpN, int duracion, boolean cancelled){
        this(nombre, desc, fecha, duracion);
        this.eventId=eventId;
        this.numEmpS=numEmpS;
        this.numEmpN=numEmpN;
        this.cancelled=cancelled;

    }

    public Evento(Long eventId, String nombre, LocalDateTime fecha, LocalDateTime fechaAlta , String
            desc, int numEmpS, int numEmpN, int duracion, boolean cancelled) {
        this(eventId, nombre,fecha, desc, numEmpS, numEmpN, duracion, cancelled);
        this.fechaAlta=fechaAlta;
    }

//getters

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = (fechaAlta != null) ? fechaAlta.withNano(0) : null;
    }

    public long getEventId() {
        return eventId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescr() {
        return descr;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public int getNumEmpS() {
        return numEmpS;
    }
    public int getNumEmpN() {
        return numEmpN;
    }

    public boolean isCancelled() {
        return cancelled;
    }
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setNumEmpS(int numEmpS) {
        this.numEmpS = numEmpS;
    }
    public void setNumEmpN(int numEmpN) {
        this.numEmpN = numEmpN;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = (fecha != null) ? fecha.withNano(0) : null;;
    }
 //equals
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Evento other = (Evento) obj;
        if (fecha == null) {
            if (other.fecha != null)
                return false;
        } else if (!fecha.equals(other.fecha))
            return false;
        if (fechaAlta == null) {
            if (other.fechaAlta != null)
                return false;
        } else if (!fechaAlta.equals(other.fechaAlta))
            return false;
        if (descr == null) {
            if (other.descr != null)
                return false;
        } else if (!descr.equals(other.descr))
            return false;
        if (eventId == null) {
            if (other.eventId != null)
                return false;
        } else if (!eventId.equals(other.eventId))
            return false;
        if (duracion != other.duracion)
            return false;
        if (numEmpS != other.numEmpS)
            return false;
        if (numEmpN != other.numEmpN)
            return false;
        if (cancelled != other.cancelled)
            return false;
        if (nombre == null) {
            if (other.nombre != null)
                return false;
        } else if (!nombre.equals(other.nombre))
            return false;
        return true;
    }
//hashcode
    @Override
    public int hashCode() {
        return Objects.hash(getFechaAlta(), getEventId(), getNombre(), getDescr(), getFecha(), getNumEmpS(),getNumEmpN(), getDuracion(), isCancelled());
    }

    @Override
    public String toString() {
        return "RestEventoDto{eventId=" + eventId +", nombre='" + nombre + '\'' +", descr='" + descr + '\'' + ", fecha=" + fecha + ", numEmpS=" + numEmpS + ", numEmpT=" + numEmpN + ", horaFinal=" + duracion + ", cancelled=" + cancelled + "}";
    }



}
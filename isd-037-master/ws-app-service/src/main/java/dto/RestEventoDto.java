package dto;

import java.time.LocalDateTime;

public class RestEventoDto {
    private Long eventId;
    private String nombre, descr;
    private String fecha;
    private int numEmpS, numEmpT, duracion;
    private boolean cancelled;

public RestEventoDto(String nombre, String descr, String fecha, int duracion){
    this.nombre=nombre;
    this.fecha=fecha;
    this.descr =descr;
    this.duracion=duracion;

}

    public RestEventoDto(Long eventId, String nombre, String fecha, String
            desc, int numEmpS, int numEmpT, int duracion, boolean cancelled){
        this(nombre, desc, fecha, duracion);
        this.eventId=eventId;
        this.numEmpS=numEmpS;
        this.numEmpT=numEmpT;
        this.cancelled=cancelled;

    }
    //getters

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescr() {
        return descr;
    }

    public String getFecha() {
        return fecha;
    }

    public int getNumEmpS() {
        return numEmpS;
    }
    public int getNumEmpT() {
        return numEmpT;
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
    public void setNumEmpT(int numEmpT) {
        this.numEmpT = numEmpT;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = (fecha != null) ? fecha.withNano(0).toString() : null;;
    }


    @Override
    public String toString() {
        return "RestEventoDto{" +
                "eventId=" + eventId +
                ", nombre='" + nombre + '\'' +
                ", descr='" + descr + '\'' +
                ", fecha=" + fecha +
                ", numEmpS=" + numEmpS +
                ", numEmpT=" + numEmpT +
                ", duracion=" + duracion +
                ", cancelled=" + cancelled +
                '}';
    }
}

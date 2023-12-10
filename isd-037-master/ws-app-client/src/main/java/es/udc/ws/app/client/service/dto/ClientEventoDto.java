package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientEventoDto {

    private Long eventId;
    private String nombre, descr;
    private LocalDateTime fecha, horaFinal;
    private int numEmpS, numEmpT;
    private boolean cancelled;

    public ClientEventoDto(String nombre, String descr, LocalDateTime fecha, LocalDateTime horaFinal){
        this.nombre=nombre;
        this.fecha=fecha;
        this.descr =descr;
        this.horaFinal =horaFinal;

    }

    public ClientEventoDto(Long eventId, String nombre, LocalDateTime fecha, String
            desc, int numEmpS, int numEmpT, LocalDateTime horaFinal, boolean cancelled){
        this(nombre, desc, fecha, horaFinal);
        this.eventId=eventId;
        this.numEmpS=numEmpS;
        this.numEmpT=numEmpT;
        this.cancelled=cancelled;

    }
    //getters

    public LocalDateTime getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(LocalDateTime horaFinal) {
        this.horaFinal = horaFinal;
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

    public LocalDateTime getFecha() {
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
        this.fecha = (fecha != null) ? fecha.withNano(0) : null;;
    }


    @Override
    public String toString() {
        return "RestEventoDto{eventId=" + eventId +", nombre='" + nombre + '\'' +", descr='" + descr + '\'' + ", fecha=" + fecha + ", numEmpS=" + numEmpS + ", numEmpT=" + numEmpT + ", horaFinal=" + horaFinal + ", cancelled=" + cancelled + "}";
    }
}

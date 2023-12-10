package dto;

import evento.Evento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class EventoToRestEventoDtoConversor {
    public static List<RestEventoDto> toRestEventoDtos(List<Evento> eventos) {
        List<RestEventoDto> eventoDtos = new ArrayList<>(eventos.size());
        for (int i = 0; i < eventos.size(); i++) {
            Evento evento = eventos.get(i);
            eventoDtos.add(toRestEventoDto(evento));
        }
        return eventoDtos;
    }

    public static RestEventoDto toRestEventoDto(Evento evento) {
        return new RestEventoDto(evento.getEventId(),evento.getNombre(), evento.getFecha().toString(), evento.getDescr(), evento.getNumEmpS(), evento.getNumEmpN()+ evento.getNumEmpS(), evento.getDuracion(), evento.isCancelled());
    }

    public static Evento toEvento(RestEventoDto evento) {
        return new Evento(evento.getEventId(),evento.getNombre(), LocalDateTime.parse(evento.getFecha()), evento.getDescr(), evento.getNumEmpS(), evento.getNumEmpT()- evento.getNumEmpS(), evento.getDuracion(), evento.isCancelled());
    }
}

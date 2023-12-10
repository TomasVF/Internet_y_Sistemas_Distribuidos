package eventoservice;

import java.time.LocalDateTime;
import java.util.List;

import eventoservice.exceptions.RespuestaRepetidaException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import evento.Evento;
import eventoservice.exceptions.EventoCanceladoException;
import eventoservice.exceptions.EventoCelebradoException;
import eventoservice.exceptions.FechaDeRespuestaExpiradaException;
import reserva.Reserva;


public interface EventoService {
    public Evento addEvento(Evento evento) throws InputValidationException;
    public List<Evento> encontrarEventosEntre(LocalDateTime fecha1, LocalDateTime fecha2, String palabrasClave) throws InputValidationException;

    public Evento findEvento(Long eventoId) throws InstanceNotFoundException;

    public Reserva responderEvento(String email, Long eventoId, boolean asistencia) throws InstanceNotFoundException, FechaDeRespuestaExpiradaException, EventoCanceladoException, RespuestaRepetidaException;

    public Evento cancelarEvento(Long eventoId) throws InstanceNotFoundException, EventoCanceladoException, EventoCelebradoException;

    public List<Reserva> encontrarReservas(String email, boolean todas);
}

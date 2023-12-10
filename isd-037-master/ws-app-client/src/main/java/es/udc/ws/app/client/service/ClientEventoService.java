package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientEventoDto;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientEventoService {
    public Long addEvento(ClientEventoDto evento) throws InputValidationException;

    public List<ClientEventoDto> encontrarEventosEntre(LocalDateTime fecha, String palabrasClave) throws InputValidationException;

    public ClientEventoDto findEvento(Long eventoId) throws InstanceNotFoundException;

    public Long responderEvento(String email, Long eventoId, boolean asistencia) throws InstanceNotFoundException, ClientFechaDeRespuestaExpiradaException, ClientEventoCanceladoException, ClientRespuestaRepetidaException;

    public void cancelarEvento(Long eventoId) throws InstanceNotFoundException, ClientEventoCelebradoException, ClientEventoCanceladoException;

    public List<ClientReservaDto> encontrarReservas(String email, boolean todas);
}

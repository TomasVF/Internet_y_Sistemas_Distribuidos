package servlets;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.EventoToRestEventoDtoConversor;
import dto.RestEventoDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestEventoDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import evento.Evento;
import eventoservice.EventoServiceFactory;
import eventoservice.exceptions.EventoCanceladoException;
import eventoservice.exceptions.EventoCelebradoException;
import eventoservice.exceptions.FechaDeRespuestaExpiradaException;
import eventoservice.exceptions.RespuestaRepetidaException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EventoServlet extends RestHttpServletTemplate {
    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        if(req.getPathInfo() == null || req.getPathInfo().equals("/")){
            RestEventoDto eventoDto = JsonToRestEventoDtoConversor.toRestEventoDto(req.getInputStream());
            Evento evento = EventoToRestEventoDtoConversor.toEvento(eventoDto);
            evento = EventoServiceFactory.getService().addEvento(evento);
            eventoDto = EventoToRestEventoDtoConversor.toRestEventoDto(evento);
            String eventoURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + evento.getEventId();
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Location", eventoURL);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                    JsonToRestEventoDtoConversor.toObjectNode(eventoDto), headers);
        }else{
            Long eventId = ServletUtils.getIdFromPath(req, "evento");
            try {
                EventoServiceFactory.getService().cancelarEvento(eventId);
            } catch (EventoCanceladoException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                        AppExceptionToJsonConversor.toEventoCanceladoException(ex),
                        null);
                return;
            }catch (EventoCelebradoException ex){
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                        AppExceptionToJsonConversor.toEventoCelebradoException(ex),
                        null);
                return;
            }

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);

        }

    }
    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        if(req.getPathInfo() == null){
            String keyWords = req.getParameter("palabrasClave");
            String fecha = req.getParameter("fecha");
            List<Evento> eventos = EventoServiceFactory.getService().encontrarEventosEntre(LocalDateTime.now(),LocalDateTime.parse(fecha),keyWords);

            List<RestEventoDto> eventoDtos = EventoToRestEventoDtoConversor.toRestEventoDtos(eventos);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestEventoDtoConversor.toArrayNode(eventoDtos), null);
        }else{
            Long eventId = ServletUtils.getIdFromPath(req, "evento");
            Evento evento = EventoServiceFactory.getService().findEvento(eventId);
            RestEventoDto eventoDto = EventoToRestEventoDtoConversor.toRestEventoDto(evento);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, JsonToRestEventoDtoConversor.toObjectNode(eventoDto), null);

        }

    }
}

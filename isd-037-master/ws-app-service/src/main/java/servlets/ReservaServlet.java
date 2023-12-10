package servlets;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.ReservaToRestReservaDtoConversor;
import dto.RestReservaDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestReservaDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import eventoservice.EventoServiceFactory;
import eventoservice.exceptions.EventoCanceladoException;
import eventoservice.exceptions.EventoCelebradoException;
import eventoservice.exceptions.FechaDeRespuestaExpiradaException;
import eventoservice.exceptions.RespuestaRepetidaException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reserva.Reserva;

public class ReservaServlet extends RestHttpServletTemplate{

        @Override
        protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
                InputValidationException, InstanceNotFoundException {
            ServletUtils.checkEmptyPath(req);
            Long reservaId = ServletUtils.getMandatoryParameterAsLong(req,"eventId");
            String email = ServletUtils.getMandatoryParameter(req,"email");
            boolean asistencia = Boolean.parseBoolean(ServletUtils.getMandatoryParameter(req,"asistencia"));

            Reserva reserva = null;
            try {
                reserva = EventoServiceFactory.getService().responderEvento(email,reservaId,asistencia);
            } catch (FechaDeRespuestaExpiradaException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toFechaDeRespuestaExpiradaException(ex),
                        null);
                return;
            }catch (EventoCanceladoException ex){
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                        AppExceptionToJsonConversor.toEventoCanceladoException(ex),
                        null);
                return;
            }catch (RespuestaRepetidaException ex){
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
                        AppExceptionToJsonConversor.toRespuestaRepetidaException(ex),
                        null);
                return;
            }

            assert reserva != null;
            RestReservaDto reservaDto =  ReservaToRestReservaDtoConversor.toRestReservaDto(reserva);
            String reservaURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + reserva.getReservaId();
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Location", reservaURL);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                    JsonToRestReservaDtoConversor.toObjectNode(reservaDto), headers);
        }

        @Override
        protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
                InputValidationException, InstanceNotFoundException {
            String email = ServletUtils.getMandatoryParameter(req,"email");
            boolean asistencia = Boolean.parseBoolean(ServletUtils.getMandatoryParameter(req,"asistencia"));

            List<Reserva> reserva;

            reserva = EventoServiceFactory.getService().encontrarReservas(email,asistencia);

            List<RestReservaDto> reservaDto =  ReservaToRestReservaDtoConversor.toRestReservaDtos(reserva);
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestReservaDtoConversor.toArrayNode(reservaDto), null);
        }


}

package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.ClientEventoService;
import es.udc.ws.app.client.service.dto.ClientEventoDto;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.ClientEventoCanceladoException;
import es.udc.ws.app.client.service.exceptions.ClientEventoCelebradoException;
import es.udc.ws.app.client.service.exceptions.ClientFechaDeRespuestaExpiradaException;
import es.udc.ws.app.client.service.exceptions.ClientRespuestaRepetidaException;
import es.udc.ws.app.client.service.rest.json.JsonToClientEventoDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientReservaDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class RestClientEventoService implements ClientEventoService {
    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientEventoService.endpointAddress";
    private String endpointAddress;

    @Override
    public Long addEvento(ClientEventoDto evento) throws InputValidationException {

        try {
            if(evento.getFecha().isAfter(evento.getHoraFinal()) || evento.getFecha().isBefore(LocalDateTime.now()) || evento.getHoraFinal().isBefore(LocalDateTime.now())){
                throw new InputValidationException("fechas inválidas");
            }
            HttpResponse response = Request.Post(getEndpointAddress() + "eventos").
                    bodyStream(toInputStream(evento), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientEventoDtoConversor.toClientEventoDto(response.getEntity().getContent()).getEventId();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ClientEventoDto> encontrarEventosEntre(LocalDateTime fecha, String palabrasClave) throws InputValidationException {
        try{
            if (fecha == null) {
                throw new InputValidationException("Date can not be null");
            }
            HttpResponse response = null;
            String dateStr = fecha.toString();
            String requestStr = getEndpointAddress() + "eventos?fecha="
                    + dateStr;
            if(Objects.equals(palabrasClave, "")){

            }else{
                requestStr = requestStr +"&palabrasClave=";
            }
            requestStr = requestStr+ URLEncoder.encode(palabrasClave, "UTF-8");

            response = Request.Get(requestStr).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientEventoDtoConversor.toClientEventoDtos(
                    response.getEntity().getContent());

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ClientEventoDto findEvento(Long eventoId) throws InstanceNotFoundException {
        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "eventos/"+eventoId).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientEventoDtoConversor.toClientEventoDto(response.getEntity()
                    .getContent());
        } catch (InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long responderEvento(String email, Long eventoId, boolean asistencia) throws InstanceNotFoundException, ClientFechaDeRespuestaExpiradaException, ClientEventoCanceladoException, ClientRespuestaRepetidaException {
        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "reservas").
                    bodyForm(
                            Form.form().
                                    add("eventId", Long.toString(eventoId)).
                                    add("email", email).
                                    add("asistencia", Boolean.toString(asistencia)).
                                    build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientReservaDtoConversor.toClientReservaDto(
                    response.getEntity().getContent()).getReservaId();

        } catch (InstanceNotFoundException | ClientFechaDeRespuestaExpiradaException | ClientEventoCanceladoException | ClientRespuestaRepetidaException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelarEvento(Long eventoId) throws InstanceNotFoundException, ClientEventoCelebradoException, ClientEventoCanceladoException {
        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "eventos/" + eventoId).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InstanceNotFoundException | ClientEventoCanceladoException | ClientEventoCelebradoException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ClientReservaDto> encontrarReservas(String email, boolean todas) {
        try{
            HttpResponse response = null;

            if(todas){
                response = Request.Get(getEndpointAddress() + "reservas?email="
                                + URLEncoder.encode(email, "UTF-8")+ "&asistencia=false").
                        execute().returnResponse();
            }else{
                response = Request.Get(getEndpointAddress() + "reservas?email="
                                + URLEncoder.encode(email, "UTF-8") + "&asistencia=true").
                        execute().returnResponse();
            }

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientReservaDtoConversor.toClientReservaDtos(response.getEntity()
                    .getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientEventoDto evento) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientEventoDtoConversor.toObjectNode(evento));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, HttpResponse response) throws Exception {

        try {

            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {

                case HttpStatus.SC_NOT_FOUND:
                    throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_FORBIDDEN:
                    throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_GONE:
                    throw JsonToClientExceptionConversor.fromGoneErrorCode(
                            response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

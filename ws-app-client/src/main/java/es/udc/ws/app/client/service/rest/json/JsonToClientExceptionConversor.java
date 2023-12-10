package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.ClientEventoCanceladoException;
import es.udc.ws.app.client.service.exceptions.ClientEventoCelebradoException;
import es.udc.ws.app.client.service.exceptions.ClientFechaDeRespuestaExpiradaException;
import es.udc.ws.app.client.service.exceptions.ClientRespuestaRepetidaException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.time.LocalDateTime;

public class JsonToClientExceptionConversor {
    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("FechaDeRespuestaExpirada")) {
                    return toFechaDeRespuestaExpiradaException(rootNode);
                } else if(errorType.equals("RespuestaRepetida")) {
                    return toRespuestaRepetidaException(rootNode);
                }else{
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
    private static ClientRespuestaRepetidaException toRespuestaRepetidaException(JsonNode rootNode) {
        Long eventId = rootNode.get("eventoId").longValue();
        return new ClientRespuestaRepetidaException(eventId);
    }
    private static ClientFechaDeRespuestaExpiradaException toFechaDeRespuestaExpiradaException(JsonNode rootNode) {
        Long eventId = rootNode.get("eventoId").longValue();
        String fechaAltaAsString = rootNode.get("fechaALta").textValue();
        LocalDateTime fechaAlta = null;
        if (fechaAltaAsString != null) {
            fechaAlta = LocalDateTime.parse(fechaAltaAsString);
        }
        return new ClientFechaDeRespuestaExpiradaException(eventId, fechaAlta);
    }

    public static Exception fromGoneErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("EventoCelebrado")) {
                    return toEventoCelebradoException(rootNode);
                } else if(errorType.equals("EventoCancelado")) {
                    return toEventoCanceladoException(rootNode);
                }else{
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
    private static ClientEventoCanceladoException toEventoCanceladoException(JsonNode rootNode) {
        Long eventoId = rootNode.get("eventoId").longValue();
        return new ClientEventoCanceladoException(eventoId);
    }
    private static ClientEventoCelebradoException toEventoCelebradoException(JsonNode rootNode) {
        Long eventoId = rootNode.get("eventoId").longValue();
        return new ClientEventoCelebradoException(eventoId);
    }
}

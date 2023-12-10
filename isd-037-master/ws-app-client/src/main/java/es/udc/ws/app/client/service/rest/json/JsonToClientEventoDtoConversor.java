package es.udc.ws.app.client.service.rest.json;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.client.service.dto.ClientEventoDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonToClientEventoDtoConversor {
    public static ObjectNode toObjectNode(ClientEventoDto evento) throws IOException {

        ObjectNode eventoObject = JsonNodeFactory.instance.objectNode();
        if (evento.getEventId() != null) {
            eventoObject.put("eventId", evento.getEventId());
        }
        eventoObject.put("nombre", evento.getNombre()).
                put("descr", evento.getDescr()).
                put("fecha", evento.getFecha().toString()).
                put("duracion", evento.getFecha().getHour() - evento.getHoraFinal().getHour()).
                put("numEmpS", evento.getNumEmpS()).
                put("numEmpT", evento.getNumEmpT()).
                put("cancelled", evento.isCancelled());

        return eventoObject;
    }

    public static ClientEventoDto toClientEventoDto(InputStream jsonEventos) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonEventos);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientEventoDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientEventoDto> toClientEventoDtos(InputStream jsonEventos) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonEventos);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode eventosArray = (ArrayNode) rootNode;
                List<ClientEventoDto> eventoDtos = new ArrayList<>(eventosArray.size());
                for (JsonNode eventoNode : eventosArray) {
                    eventoDtos.add(toClientEventoDto(eventoNode));
                }

                return eventoDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientEventoDto toClientEventoDto(JsonNode eventoNode) throws ParsingException {
        if (eventoNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode eventObject = (ObjectNode) eventoNode;

            JsonNode eventIdNode = eventObject.get("eventId");
            Long eventId = (eventIdNode != null) ? eventIdNode.longValue() : null;

            String nombre   = eventObject.get("nombre").textValue().trim();
            String description = eventObject.get("descr").textValue().trim();
            String fecha1 = eventObject.get("fecha").textValue().trim();
            LocalDateTime fecha =LocalDateTime.parse(fecha1);
            int duracion = eventObject.get("duracion").asInt();
            int numEmpS = eventObject.get("numEmpS").asInt();
            int numEmpT = eventObject.get("numEmpT").asInt();
            boolean cancelled = eventObject.get("cancelled").asBoolean();

            return new ClientEventoDto(eventId,nombre,fecha,description, numEmpS, numEmpT, fecha.plusHours(duracion), cancelled);
        }
    }

}

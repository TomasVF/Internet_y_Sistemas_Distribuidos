package es.udc.ws.app.restservice.json;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import dto.RestEventoDto;
import evento.Evento;

public class JsonToRestEventoDtoConversor {

    public static ObjectNode toObjectNode(RestEventoDto evento) {

        ObjectNode eventoObject = JsonNodeFactory.instance.objectNode();

        if (evento.getEventId() != null) {
            eventoObject.put("eventId", evento.getEventId());
        }

        eventoObject.put("nombre",evento.getNombre()).
        put("fecha",evento.getFecha().toString()).
        put("descr",evento.getDescr()).
        put("numEmpS",evento.getNumEmpS()).
        put("numEmpT",evento.getNumEmpT()).
        put("duracion",evento.getDuracion()).
        put("cancelled",evento.isCancelled());

        return eventoObject;
    }

    public static ArrayNode toArrayNode(List<RestEventoDto> evento){

        ArrayNode eventoNode = JsonNodeFactory.instance.arrayNode();
        for (RestEventoDto eventoDto : evento) {
            ObjectNode eventoObject = toObjectNode(eventoDto);
            eventoNode.add(eventoObject);
        }

        return eventoNode;

    }

    public static RestEventoDto toRestEventoDto(InputStream jsonEvento) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonEvento);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode eventoObject = (ObjectNode) rootNode;

                JsonNode eventoIdNode = eventoObject.get("eventoId");
                Long eventId = (eventoIdNode != null) ? eventoIdNode.longValue() : null;
                String nombre = eventoObject.get("nombre").textValue().trim();
                String descr = eventoObject.get("descr").textValue().trim();
                String fecha = eventoObject.get("fecha").textValue().trim();
                int numEmpS = eventoObject.get("numEmpS").intValue();
                int numEmpT = eventoObject.get("numEmpT").intValue();
                int duracion = eventoObject.get("duracion").intValue();
                boolean cancelled = eventoObject.get("cancelled").asBoolean();
                return new RestEventoDto(eventId, nombre, fecha, descr,
                        numEmpS, numEmpT, duracion, cancelled);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }


}

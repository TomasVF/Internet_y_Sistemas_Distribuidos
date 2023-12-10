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

import dto.RestEventoDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import dto.RestReservaDto;

public class JsonToRestReservaDtoConversor {

    public static ObjectNode toObjectNode(RestReservaDto reserva){

        ObjectNode reservaNode = JsonNodeFactory.instance.objectNode();

        if (reserva != null){
            reservaNode.put("reservaId",reserva.getReservaId())
                    .put("eventID", reserva.getEventoId())
                    .put("empemail",reserva.getEmpemail())
                    .put("asistencia", reserva.isAsistencia());
        }

        return reservaNode;

    }

    public static ArrayNode toArrayNode(List<RestReservaDto> reserva){

        ArrayNode reservaNode = JsonNodeFactory.instance.arrayNode();
        for (RestReservaDto reservaDto : reserva) {
            ObjectNode reservaObject = toObjectNode(reservaDto);
            reservaNode.add(reservaObject);
        }

        return reservaNode;

    }

    public static RestReservaDto toRestReservaDto(InputStream jsonReserva) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonReserva);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode reservaObject = (ObjectNode) rootNode;

                JsonNode reservaIdNode = reservaObject.get("reservaId");
                Long reservaId = (reservaIdNode != null) ? reservaIdNode.longValue() : null;

                Long eventId = reservaObject.get("eventId").longValue();
                String empemail = reservaObject.get("empemail").textValue().trim();
                String strfecha = reservaObject.get("fechaemail").textValue().trim();
                LocalDateTime fechaemail = LocalDateTime.parse(strfecha);
                boolean asistencia = reservaObject.get("asistencia").asBoolean();

                return new RestReservaDto(reservaId, eventId, empemail, asistencia);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}

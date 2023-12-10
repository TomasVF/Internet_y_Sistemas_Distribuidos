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
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.app.client.service.dto.*;

public class JsonToClientReservaDtoConversor {

    public static ObjectNode toObjectNode(ClientReservaDto reserva) throws IOException {

        ObjectNode reservaObject = JsonNodeFactory.instance.objectNode();

        if (reserva.getReservaId() != null) {
            reservaObject.put("reservaId", reserva.getReservaId());
        }
        reservaObject.put("eventoId", reserva.getEventoId()).
                put("empemail", reserva.getEmpemail()).
                put("asistencia", reserva.isAsistencia());

        return reservaObject;
    }

    public static ClientReservaDto toClientReservaDto(InputStream jsonReserva) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonReserva);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientReservaDto(rootNode);

            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientReservaDto> toClientReservaDtos(InputStream jsonReservas) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonReservas);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode reservasArray = (ArrayNode) rootNode;
                List<ClientReservaDto> reservaDtos = new ArrayList<>(reservasArray.size());
                for (JsonNode reservaNode : reservasArray) {
                    reservaDtos.add(toClientReservaDto(reservaNode));
                }

                return reservaDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientReservaDto toClientReservaDto(JsonNode reservaNode) throws ParsingException {
        if (reservaNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode reservaObject = (ObjectNode) reservaNode;

            JsonNode reservaIdNode = reservaObject.get("reservaId");
            Long reservaId = (reservaIdNode != null) ? reservaIdNode.longValue() : null;

            Long eventoId = reservaObject.get("eventID").longValue();
            String empemail = reservaObject.get("empemail").textValue().trim();
            boolean asistencia = reservaObject.get("asistencia").asBoolean();

            return new ClientReservaDto(reservaId,eventoId, empemail, asistencia);
        }
    }

}

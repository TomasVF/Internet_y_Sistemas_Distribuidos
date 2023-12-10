package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eventoservice.exceptions.EventoCanceladoException;
import eventoservice.exceptions.EventoCelebradoException;
import eventoservice.exceptions.FechaDeRespuestaExpiradaException;
import eventoservice.exceptions.RespuestaRepetidaException;

public class AppExceptionToJsonConversor {

    public static ObjectNode toEventoCanceladoException(EventoCanceladoException ex){

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "EventoCancelado");
        exceptionObject.put("eventoId", (ex.getEventoId() != null) ? ex.getEventoId() : null);

        return exceptionObject;
    }

    public static ObjectNode toEventoCelebradoException(EventoCelebradoException ex){

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "EventoCelebrado");
        exceptionObject.put("eventoId", (ex.getEventoId() != null) ? ex.getEventoId() : null);

        return exceptionObject;
    }

    public static ObjectNode toFechaDeRespuestaExpiradaException(FechaDeRespuestaExpiradaException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "FechaDeRespuestaExpirada");
        exceptionObject.put("eventoId", (ex.getEventoId() != null) ? ex.getEventoId() : null);
        if (ex.getFechaALta() != null) {
            exceptionObject.put("fechaALta", ex.getFechaALta().toString());
        } else {
            exceptionObject.set("fechaALta", null);
        }

        return exceptionObject;
    }

    public static ObjectNode toRespuestaRepetidaException(RespuestaRepetidaException ex){

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
        exceptionObject.put("errorType", "RespuestaRepetida");
        exceptionObject.put("eventoId", (ex.getEventoId() != null) ? ex.getEventoId() : null);

        return exceptionObject;
    }
}

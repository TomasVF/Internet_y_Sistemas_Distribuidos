package evento;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
public interface SqlEventoDao {
        public Evento create(Connection connection, Evento evento);
        public Evento find(Connection connection, Long eventId)
                throws InstanceNotFoundException;
        public List<Evento> findByDatesAndKeywords(Connection connection,
                                                   LocalDateTime date1,
                                                   LocalDateTime date2,
                                                   String keywords) throws InputValidationException;
        public Evento update(Connection connection, Evento evento)
                throws InstanceNotFoundException;
        public void remove(Connection connection, Long eventId)
                throws InstanceNotFoundException;

    }


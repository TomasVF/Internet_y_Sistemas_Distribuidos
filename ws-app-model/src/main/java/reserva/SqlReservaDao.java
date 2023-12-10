package reserva;

import java.util.List;
import java.sql.Connection;

import es.udc.ws.util.exceptions.InstanceNotFoundException;
import evento.Evento;

public interface SqlReservaDao {
    public Reserva create(Connection connection, Reserva reserva);

    public boolean existsReservation(Connection connection, String email, Long eventoId);

    public List<Reserva> findByUser(Connection connection, String empemail, boolean todos);

    public void remove(Connection connection, Long reservaId)
            throws InstanceNotFoundException;
}

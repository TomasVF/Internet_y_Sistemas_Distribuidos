package reserva;

import  es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class AbstractSqlReservaDao implements SqlReservaDao {
    protected AbstractSqlReservaDao() {
    }

    @Override
    public List<Reserva> findByUser(Connection connection, String empemail, boolean todos) {

        String queryString = "SELECT reservaId, eventId, fechaemail, asistencia"
                + " FROM Reserva WHERE empemail = ?";

        if(todos != true) {
            queryString += " AND";
            queryString += " asistencia = true";
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
            int i = 1;
            preparedStatement.setString(i++, empemail);

            ResultSet resultSet = preparedStatement.executeQuery();

            List<Reserva> reservas = new ArrayList<>();

            while (resultSet.next()){
                i = 1;
                Long reservaId = resultSet.getLong(i++);
                Long eventoId = resultSet.getLong(i++);
                Timestamp fechaemailAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime fechaemail = fechaemailAsTimestamp.toLocalDateTime();
                boolean asistencia = resultSet.getBoolean(i++);

                reservas.add(new Reserva(reservaId,eventoId, empemail, fechaemail, asistencia));
            }
            return reservas;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean existsReservation(Connection connection, String email, Long eventId) {
        String queryString = "SELECT COUNT(*) FROM Reserva WHERE empemail = ? AND eventId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, email);
            preparedStatement.setLong(i++, eventId);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new SQLException("Error retrieving the number of reservations for the user with email" + email + "for the event with id" + eventId);
            }

            /* Get results. */
            i = 1;
            Long numberOfSales = resultSet.getLong(i++);

            /* Return result. */
            return numberOfSales > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long reservaId) throws InstanceNotFoundException {
        String queryString = "DELETE FROM Reserva WHERE" + " reservaId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reservaId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(reservaId,
                        Reserva.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


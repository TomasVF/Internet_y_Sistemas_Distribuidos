package reserva;

import java.sql.*;

public class Jbdc3CcSqlReservaDao extends AbstractSqlReservaDao{

    @Override
    public Reserva create(Connection connection, Reserva reserva) {
        String queryString = "INSERT INTO Reserva"
                + " (eventId, empemail, fechaemail, asistencia) VALUES (?, ?, ?, ?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, reserva.getEventoId());
            preparedStatement.setString(i++, reserva.getEmpemail());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(reserva.getFechaemail()));
            preparedStatement.setBoolean(i++, reserva.isAsistencia());

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long reservaId = resultSet.getLong(1);

            /* Return sale. */
            return new Reserva(reservaId, reserva.getEventoId(), reserva.getEmpemail(), reserva.getFechaemail(), reserva.isAsistencia());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

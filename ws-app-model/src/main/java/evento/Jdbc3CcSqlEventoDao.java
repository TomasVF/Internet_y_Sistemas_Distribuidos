package evento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlEventoDao extends AbstractSqlEventoDao {

    @Override
    public Evento create(Connection connection, Evento evento) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Evento"
                + " (nombre, fecha,fechaAlta,descr,numEmpS,numEmpN,duracion, cancelled )"
                + " VALUES (?, ?,?, ?, ?,?, ?,?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, evento.getNombre());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(evento.getFecha()));
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(evento.getFechaAlta()));
            preparedStatement.setString(i++, evento.getDescr());
            preparedStatement.setInt(i++, evento.getNumEmpS());
            preparedStatement.setInt(i++, evento.getNumEmpN());
            preparedStatement.setInt(i++, evento.getDuracion());
            preparedStatement.setBoolean(i++, evento.isCancelled());


            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long eventId = resultSet.getLong(1);

            /* Return movie. */
            return new Evento(eventId, evento.getNombre(), evento.getFecha(), evento.getFechaAlta(),
                    evento.getDescr(),
                    evento.getNumEmpS(),evento.getNumEmpN(),evento.getDuracion(),evento.isCancelled());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
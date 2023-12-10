package evento;

import es.udc.ws.util.exceptions.InputValidationException;
import  es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlEventoDao implements SqlEventoDao {

    protected AbstractSqlEventoDao() {
    }

    @Override
    public Evento find(Connection connection, Long eventId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT nombre, fecha, fechaAlta, "
                + " descr,numEmpS,numEmpN, duracion,  cancelled FROM Evento WHERE eventId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, eventId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(eventId,
                        Evento.class.getName());
            }

            /* Get results. */
            i = 1;
            String nombre = resultSet.getString(i++);
            Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime fecha = creationDateAsTimestamp.toLocalDateTime();
            Timestamp fechaAltaAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime fechaAlta = fechaAltaAsTimestamp.toLocalDateTime();
            String descr = resultSet.getString(i++);
            int numEmpS=resultSet.getInt(i++);
            int numEmpN=resultSet.getInt(i++);
            int duracion=resultSet.getInt(i++);
            boolean cancelled = resultSet.getBoolean(i++);
            /* Return evento. */
            return new Evento(eventId,nombre, fecha, fechaAlta, descr,numEmpS,numEmpN,duracion, cancelled);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Evento> findByDatesAndKeywords(Connection connection,LocalDateTime date1,LocalDateTime date2,String keywords) throws InputValidationException{

        /* Create "queryString". */
        String[] words = keywords != null ? keywords.split(" ") : null;
        String queryString = "SELECT eventId, nombre, fecha, fechaAlta, " +
                "descr,numEmpS,numEmpN,duracion, cancelled FROM Evento WHERE fecha BETWEEN ? AND ?";
        if (words != null && words.length > 0) {
            queryString += " AND";
            for (int i = 0; i < words.length; i++) {
                if (i > 0) {
                    queryString += " AND";
                }
                queryString += " LOWER (descr) LIKE LOWER (?)";
            }
        }

        Timestamp timestamp = Timestamp.valueOf(date1);
        Timestamp timestamp2 = Timestamp.valueOf(date2);

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            int j = 1;
            if(timestamp2.before(timestamp)){
                throw new InputValidationException("fechas incorrectas");
            }

            preparedStatement.setTimestamp(j++,timestamp);
            preparedStatement.setTimestamp(j++,timestamp2);

            if (words != null) {
                /* Fill "preparedStatement". */
                for (int i = 0; i < words.length; i++) {
                    preparedStatement.setString(j++, "%"+words[i]+"%");
                }
            }

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read eventos. */
            List<Evento> lista = new ArrayList<Evento>();

            while (resultSet.next()) {

                int i = 1;
                Long eventId = resultSet.getLong(i++);
                String nombre = resultSet.getString(i++);
                Timestamp creationDateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime fecha = creationDateAsTimestamp.toLocalDateTime();
                Timestamp fechaAltaAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime fechaAlta = fechaAltaAsTimestamp.toLocalDateTime();
                String descr = resultSet.getString(i++);
                int numEmpS=resultSet.getInt(i++);
                int numEmpN=resultSet.getInt(i++);
                int duracion=resultSet.getInt(i++);
                boolean cancelled = resultSet.getBoolean(i++);

                lista.add(new Evento(eventId,nombre, fecha, fechaAlta, descr,numEmpS,numEmpN,duracion,cancelled));

            }

            /* Return eventos. */
            return lista;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    @Override
    public Evento update(Connection connection, Evento evento)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Evento"
                + " SET nombre = ?, descr = ?, fecha = ?, "
                + "numEmpS = ?,numEmpN = ?, duracion = ?, cancelled = ? WHERE eventId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, evento.getNombre());
            preparedStatement.setString(i++, evento.getDescr());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(evento.getFecha()));
            preparedStatement.setInt(i++, evento.getNumEmpS());
            preparedStatement.setInt(i++, evento.getNumEmpN());
            preparedStatement.setInt(i++, evento.getDuracion());
            preparedStatement.setBoolean(i++, evento.isCancelled());
            preparedStatement.setLong(i++, evento.getEventId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(evento.getEventId(),
                        Evento.class.getName());
            }
            return evento;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(Connection connection, Long eventId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Evento WHERE" + " eventId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, eventId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(eventId,
                        Evento.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}


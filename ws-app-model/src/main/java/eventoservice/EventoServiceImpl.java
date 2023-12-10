package eventoservice;

import evento.SqlEventoDaoFactory;
import eventoservice.exceptions.RespuestaRepetidaException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.sql.DataSourceLocator;
import evento.SqlEventoDao;
import reserva.SqlReservaDao;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import evento.Evento;
import eventoservice.exceptions.EventoCanceladoException;
import eventoservice.exceptions.EventoCelebradoException;
import eventoservice.exceptions.FechaDeRespuestaExpiradaException;
import reserva.Reserva;
import es.udc.ws.util.validation.PropertyValidator;
import reserva.SqlReservaDaoFactory;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;

import javax.sql.ConnectionEvent;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventoServiceImpl implements EventoService{

    private final DataSource dataSource;
    private SqlEventoDao eventoDao = null;
    private SqlReservaDao reservaDao = null;

    public EventoServiceImpl() {
        this.dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        eventoDao = SqlEventoDaoFactory.getDao();
        reservaDao = SqlReservaDaoFactory.getDao();
    }

    private void validateEvento(Evento evento) throws InputValidationException {
        PropertyValidator.validateMandatoryString("nombre", evento.getNombre());
        PropertyValidator.validateMandatoryString("descr", evento.getDescr());
    }

    @Override
    public Evento addEvento(Evento evento) throws InputValidationException{
        validateEvento(evento);
        evento.setFechaAlta(LocalDateTime.now());
        evento.setCancelled(false);
        evento.setNumEmpS(0);
        evento.setNumEmpN(0);
        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Evento createdEvento = eventoDao.create(connection, evento);

                /* Commit. */
                connection.commit();

                return createdEvento;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Evento> encontrarEventosEntre(LocalDateTime fecha1, LocalDateTime fecha2, String palabrasClave) throws InputValidationException{
        try (Connection connection = dataSource.getConnection()){
            return eventoDao.findByDatesAndKeywords(connection,fecha1,fecha2,palabrasClave);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Evento findEvento(Long eventoId) throws InstanceNotFoundException {
        try (Connection connection = dataSource.getConnection()) {
            return eventoDao.find(connection, eventoId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reserva responderEvento(String email, Long eventoId, boolean asistencia) throws InstanceNotFoundException, FechaDeRespuestaExpiradaException, EventoCanceladoException, RespuestaRepetidaException {
        try (Connection connection = dataSource.getConnection()) {

            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Evento evento = eventoDao.find(connection, eventoId);
                LocalDateTime expirationDate = evento.getFecha().minusHours(24);
                if(evento.isCancelled()){
                    connection.commit();
                    throw new EventoCanceladoException(eventoId);
                }
                if (LocalDateTime.now().isAfter(expirationDate)) {
                    connection.commit();
                    throw new FechaDeRespuestaExpiradaException(eventoId, evento.getFechaAlta());
                }
                if(reservaDao.existsReservation(connection, email, eventoId)){
                    connection.commit();
                    throw new RespuestaRepetidaException(eventoId);
                }
                Reserva reserva = reservaDao.create(connection, new Reserva(eventoId, email, LocalDateTime.now().withNano(0), asistencia));

                if(asistencia){
                    evento.setNumEmpS(evento.getNumEmpS()+1);
                }else {
                    evento.setNumEmpN(evento.getNumEmpN()+1);
                }
                eventoDao.update(connection, evento);
                /* Commit. */
                connection.commit();

                return reserva;

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Evento cancelarEvento(Long eventoId) throws InstanceNotFoundException, EventoCanceladoException, EventoCelebradoException {
        try (Connection connection = dataSource.getConnection()) {

            try {

                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Evento evento = eventoDao.find(connection, eventoId);

                if (evento.isCancelled()) {
                    throw new EventoCanceladoException(eventoId);
                }
                if(LocalDateTime.now().isAfter(evento.getFecha())){
                    throw new EventoCelebradoException(eventoId);
                }
                evento.setCancelled(true);

                Evento eventoUpdated= eventoDao.update(connection, evento);

                connection.commit();
                return eventoUpdated;
            } catch (InstanceNotFoundException | EventoCanceladoException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
            }



    }

    @Override
    public List<Reserva> encontrarReservas(String email, boolean todas) {
        try (Connection connection = dataSource.getConnection()){
            return reservaDao.findByUser(connection,email,todas);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

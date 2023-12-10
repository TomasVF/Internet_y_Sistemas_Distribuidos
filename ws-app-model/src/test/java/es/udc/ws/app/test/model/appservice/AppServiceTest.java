package es.udc.ws.app.test.model.appservice;
import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;
import static org.junit.jupiter.api.Assertions.*;

import es.udc.ws.util.sql.DataSourceLocator;
import evento.Evento;
import evento.SqlEventoDao;
import evento.SqlEventoDaoFactory;
import eventoservice.EventoService;
import eventoservice.EventoServiceFactory;
import eventoservice.exceptions.EventoCanceladoException;
import eventoservice.exceptions.EventoCelebradoException;
import eventoservice.exceptions.FechaDeRespuestaExpiradaException;
import eventoservice.exceptions.RespuestaRepetidaException;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reserva.Reserva;
import reserva.SqlReservaDao;
import reserva.SqlReservaDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.SimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class AppServiceTest {

    private final long NON_EXISTENT_EVENT_ID = -1;
    private static EventoService eventoService = null;
    private static SqlReservaDao reservaDao = null;
    private static SqlEventoDao eventoDao = null;

    @BeforeAll
    public static void init(){
        DataSource dataSource = new SimpleDataSource();
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);
        eventoService = EventoServiceFactory.getService();
        reservaDao = SqlReservaDaoFactory.getDao();
        eventoDao = SqlEventoDaoFactory.getDao();
    }

    private Evento createEvento(Evento evento) {

        Evento addedEvento = null;
        try {
            addedEvento = eventoService.addEvento(evento);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
        return addedEvento;

    }

    private Evento getValidEvento() {
        return new Evento("Evento 1", "descripcion", LocalDateTime.now().plusDays(10).withNano(0), 10);
    }

    private void removeEvento(Long eventoId) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                eventoDao.remove(connection, eventoId);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    private void removeReserva(Long reservaId) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                reservaDao.remove(connection, reservaId);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    @Test
    public void testAddEventoAndFindEvento() throws InstanceNotFoundException, InputValidationException {
        Evento evento = getValidEvento();
        Evento addedEvento = null;
        try{
            LocalDateTime beforeCreationDate = LocalDateTime.now().withNano(0);
            addedEvento = eventoService.addEvento(evento);
            LocalDateTime afterCreationDate = LocalDateTime.now().withNano(0);

            Evento foundEvento = eventoService.findEvento(addedEvento.getEventId());

            assertEquals(addedEvento, foundEvento);
            assertEquals(foundEvento.getNombre(), evento.getNombre());
            assertEquals(foundEvento.getDuracion(), evento.getDuracion());
            assertEquals(foundEvento.getDescr(), evento.getDescr());
            assertEquals(foundEvento.getFecha(), evento.getFecha());
            assertTrue((foundEvento.getFechaAlta().compareTo(beforeCreationDate) >= 0) && (foundEvento.getFechaAlta().compareTo(afterCreationDate) <= 0));

        }finally {
            if(addedEvento != null){
                removeEvento(addedEvento.getEventId());
            }
        }
    }

    @Test
    public void testAddInvalidEvento() {

        assertThrows(InputValidationException.class, () -> {
            Evento evento = getValidEvento();
            evento.setNombre(null);
            Evento addeEvento = eventoService.addEvento(evento);
            removeEvento(addeEvento.getEventId());
        });
        assertThrows(InputValidationException.class, () -> {
            Evento evento = getValidEvento();
            evento.setNombre("");
            Evento addeEvento = eventoService.addEvento(evento);
            removeEvento(addeEvento.getEventId());
        });

        assertThrows(InputValidationException.class, () -> {
            Evento evento = getValidEvento();
            evento.setDescr(null);
            Evento addeEvento = eventoService.addEvento(evento);
            removeEvento(addeEvento.getEventId());
        });
        assertThrows(InputValidationException.class, () -> {
            Evento evento = getValidEvento();
            evento.setDescr("");
            Evento addeEvento = eventoService.addEvento(evento);
            removeEvento(addeEvento.getEventId());
        });

    }

    @Test
    public void testResponderEventoYEncontrarReservas() throws InstanceNotFoundException, FechaDeRespuestaExpiradaException, EventoCanceladoException, RespuestaRepetidaException {
        Evento evento = createEvento(getValidEvento());
        Evento evento2 = createEvento(getValidEvento());
        Reserva reserva1 = null;
        Reserva reserva2 = null;
        Evento eventoFound = null;
        Evento eventoFound2 = null;
        try{
            LocalDateTime beforeMailDate = LocalDateTime.now().withNano(0);

            reserva1 = eventoService.responderEvento("user1@gmail.com", evento.getEventId(), false);
            reserva2 = eventoService.responderEvento("user1@gmail.com", evento2.getEventId(), true);

            LocalDateTime afterMailDate = LocalDateTime.now().withNano(0);

            List<Reserva> foundReservas = eventoService.encontrarReservas("user1@gmail.com", true);
            List<Reserva> foundReservasTrue = eventoService.encontrarReservas("user1@gmail.com", false);

            assertEquals(reserva1, foundReservas.get(0));
            assertEquals(reserva2, foundReservas.get(1));
            assertEquals(reserva2, foundReservasTrue.get(0));
            assertFalse(foundReservasTrue.contains(reserva1));
            assertEquals(reserva1.getEventoId(), evento.getEventId());
            assertEquals(reserva1.getEmpemail(), "user1@gmail.com");
            assertFalse(reserva1.isAsistencia());
            assertTrue((reserva1.getFechaemail().compareTo(beforeMailDate) >= 0) && (reserva1.getFechaemail().compareTo(afterMailDate) <= 0));
            eventoFound = eventoService.findEvento(evento.getEventId());
            eventoFound2 = eventoService.findEvento(evento2.getEventId());
            assertEquals(evento2.getNumEmpS()+1, eventoFound2.getNumEmpS());
            assertEquals(evento.getNumEmpN()+1, eventoFound.getNumEmpN());
        }finally {
            if(reserva1 != null){
                removeReserva(reserva1.getReservaId());
            }
            if(reserva2 != null){
                removeReserva(reserva2.getReservaId());
            }
            removeEvento(evento.getEventId());
            removeEvento(evento2.getEventId());
        }
    }

    @Test
    public void testResponderEventoErrores() throws InstanceNotFoundException, FechaDeRespuestaExpiradaException, EventoCanceladoException, RespuestaRepetidaException, EventoCelebradoException {
        Evento evento = createEvento(getValidEvento());
        Evento eventoCelebrado = createEvento(new Evento("Evento 1", "descripcion", LocalDateTime.now().plusHours(10), 10));
        eventoCelebrado.setFecha(LocalDateTime.now().plusHours(10));
        Evento eventoCancelado = createEvento(getValidEvento());
        eventoService.cancelarEvento(eventoCancelado.getEventId());
        Reserva reserva1 = null;
        try {
            assertThrows(InstanceNotFoundException.class, () ->{
                Reserva reserva = eventoService.responderEvento("user1@gmail.com", NON_EXISTENT_EVENT_ID, false);
                removeReserva(reserva.getReservaId());
            });
            assertThrows(FechaDeRespuestaExpiradaException.class, () ->{
                Reserva reserva = eventoService.responderEvento("user1@gmail.com", eventoCelebrado.getEventId(), false);
                removeReserva(reserva.getReservaId());
            });
            assertThrows(EventoCanceladoException.class, () ->{
                Reserva reserva = eventoService.responderEvento("user1@gmail.com", eventoCancelado.getEventId(), false);
                removeReserva(reserva.getReservaId());
            });
            reserva1 = eventoService.responderEvento("user1@gmail.com", evento.getEventId(), true);
            assertThrows(RespuestaRepetidaException.class, () ->{
                Reserva reserva = eventoService.responderEvento("user1@gmail.com", evento.getEventId(), false);
                removeReserva(reserva.getReservaId());
            });
        }finally {
            if(reserva1 != null){
                removeReserva(reserva1.getReservaId());
            }
            removeEvento(evento.getEventId());
            removeEvento(eventoCelebrado.getEventId());
            removeEvento(eventoCancelado.getEventId());
        }
    }

    @Test
    public void testFindNonExistentEvento() {
        assertThrows(InstanceNotFoundException.class, () -> eventoService.findEvento(NON_EXISTENT_EVENT_ID));
    }
    @Test
    public void testCancelarEvento() throws InstanceNotFoundException, EventoCanceladoException, EventoCelebradoException {
        Evento evento = createEvento(getValidEvento());
        try {
            eventoService.cancelarEvento(evento.getEventId());
            Evento eventofound= eventoService.findEvento(evento.getEventId());
            assertTrue(eventofound.isCancelled());
        }finally {
            removeEvento(evento.getEventId());
        }
    }
    @Test
    public void testCancelarEventoErrores() throws EventoCanceladoException, EventoCelebradoException, InstanceNotFoundException {
        //que no exista, que esté cancelado y que ya se haya celebrado
        Evento evento = createEvento(getValidEvento());
        Evento eventoCelebrado = createEvento(new Evento("Evento 1", "descripcion", LocalDateTime.now().minusHours(10), 10));
        eventoCelebrado.setFecha(LocalDateTime.now().plusHours(10));
        Evento eventoCancelado = createEvento(getValidEvento());
        eventoService.cancelarEvento(eventoCancelado.getEventId());

        try {
            assertThrows(InstanceNotFoundException.class, () ->{
                Evento eventocancelled = eventoService.cancelarEvento( NON_EXISTENT_EVENT_ID);
                removeEvento(eventocancelled.getEventId());
            });
            assertThrows(EventoCelebradoException.class, () ->{
                Evento eventocel = eventoService.cancelarEvento( eventoCelebrado.getEventId());
                removeEvento(eventocel.getEventId());
            });
            assertThrows(EventoCanceladoException.class, () ->{
                Evento eventocan = eventoService.cancelarEvento( eventoCancelado.getEventId());
                removeEvento(eventocan.getEventId());
            });

        }finally {

            removeEvento(evento.getEventId());
            removeEvento(eventoCelebrado.getEventId());
            removeEvento(eventoCancelado.getEventId());
        }
    }
    @Test
    public void testFindEventos() throws InputValidationException {
        // Add movies
        List<Evento> eventos = new LinkedList<Evento>();
        Evento evento1 = createEvento(getValidEvento());
        eventos.add(evento1);
        Evento evento2 = createEvento(getValidEvento());
        eventos.add(evento2);
        Evento evento3 = createEvento(getValidEvento());
        eventos.add(evento3);

        try {
            List<Evento> foundEventos = eventoService.encontrarEventosEntre(LocalDateTime.now().minusDays(1).withNano(0),LocalDateTime.now().plusDays(20).withNano(0), null);
            assertEquals(eventos, foundEventos);

            List<Evento> foundEventos1 = eventoService.encontrarEventosEntre(LocalDateTime.now().minusDays(1).withNano(0),LocalDateTime.now().minusHours(5).withNano(0), null);
            assertEquals(0, foundEventos1.size());

            List<Evento> foundEventos2 = eventoService.encontrarEventosEntre(LocalDateTime.now().minusDays(1).withNano(0),LocalDateTime.now().plusDays(20).withNano(0),"descripcion");
            assertEquals(eventos, foundEventos2);

            List<Evento> foundEventos3 = eventoService.encontrarEventosEntre(LocalDateTime.now().minusDays(1).withNano(0),LocalDateTime.now().minusHours(5).withNano(0),"Descrip");
            assertEquals(0, foundEventos3.size());

        } finally {
            // Clear Database
            removeEvento(evento1.getEventId());
            removeEvento(evento2.getEventId());
            removeEvento(evento3.getEventId());
        }

    }

}

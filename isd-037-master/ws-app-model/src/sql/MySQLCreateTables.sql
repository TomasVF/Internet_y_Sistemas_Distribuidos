-- ----------------------------------------------------------------------------
-- Model
-------------------------------------------------------------------------------

DROP TABLE Reserva;
DROP TABLE Evento;

-- --------------------------------- Evento ------------------------------------

CREATE TABLE Evento ( eventId BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) COLLATE latin1_bin NOT NULL,
    descr VARCHAR(1024) COLLATE latin1_bin NOT NULL,
    fecha DATETIME NOT NULL,
    fechaAlta DATETIME NOT NULL,
    numEmpS SMALLINT NOT NULL,
    numEmpN SMALLINT NOT NULL,
    duracion SMALLINT NOT NULL,
    cancelled BIT NOT NULL,
    CONSTRAINT EventoPK PRIMARY KEY (eventId)) ENGINE = InnoDB;


-- --------------------------------- Reserva ------------------------------------

CREATE TABLE Reserva ( reservaId BIGINT NOT NULL AUTO_INCREMENT,
    eventId BIGINT NOT NULL,
    empemail VARCHAR(255) COLLATE latin1_bin NOT NULL,
    fechaemail DATETIME NOT NULL,
    asistencia BIT,
    CONSTRAINT ReservaPK PRIMARY KEY(reservaId),
    CONSTRAINT ReservaEventoIdFO FOREIGN KEY(eventId)
                     REFERENCES Evento(eventId) ON DELETE CASCADE ) ENGINE = InnoDB

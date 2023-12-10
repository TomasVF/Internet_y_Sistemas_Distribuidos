package dto;

import evento.Evento;
import reserva.Reserva;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaToRestReservaDtoConversor {
    public static List<RestReservaDto> toRestReservaDtos(List<Reserva> reservas) {
        List<RestReservaDto> reservaDtos = new ArrayList<>(reservas.size());
        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);
            reservaDtos.add(toRestReservaDto(reserva));
        }
        return reservaDtos;
    }

    public static RestReservaDto toRestReservaDto(Reserva reserva) {
        return new RestReservaDto(reserva.getReservaId(), reserva.getEventoId(), reserva.getEmpemail(), reserva.isAsistencia());
    }

    public static Reserva toReserva(RestReservaDto reserva) {
        return new Reserva(reserva.getReservaId(), reserva.getEventoId(), reserva.getEmpemail(), reserva.isAsistencia());
    }
}

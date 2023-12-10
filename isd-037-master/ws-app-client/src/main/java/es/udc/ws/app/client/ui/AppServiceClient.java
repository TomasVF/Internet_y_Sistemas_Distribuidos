package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientEventoService;
import es.udc.ws.app.client.service.ClientEventoServiceFactory;
import es.udc.ws.app.client.service.dto.ClientEventoDto;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.ClientEventoCanceladoException;
import es.udc.ws.app.client.service.exceptions.ClientEventoCelebradoException;
import es.udc.ws.app.client.service.exceptions.ClientFechaDeRespuestaExpiradaException;
import es.udc.ws.app.client.service.exceptions.ClientRespuestaRepetidaException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public class AppServiceClient {
    public static void main(String[] args) {

        if(args.length == 0) {
            printUsageAndExit();
        }
        ClientEventoService clientEventoService =
                ClientEventoServiceFactory.getService();
        if("-addEvent".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[]{});

            try {
                Long eventoId = clientEventoService.addEvento(new ClientEventoDto(args[1], args[2], LocalDateTime.parse(args[3]), LocalDateTime.parse(args[4])));

                System.out.println("Evento " + eventoId + " created sucessfully");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-respond".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {2});

            try {
                Long reservaId= clientEventoService.responderEvento(args[1], Long.parseLong(args[2]), Boolean.parseBoolean(args[3]));

                System.out.println("Usuario con email: "+args[1]+" ha respondido con: " + args[3] +
                        " al evento con id: "+args[2]+" respuesta guardada con id: "+reservaId);

            } catch (NumberFormatException | InstanceNotFoundException | ClientFechaDeRespuestaExpiradaException | ClientEventoCanceladoException | ClientRespuestaRepetidaException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if("-cancel".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {1});

            try {
                clientEventoService.cancelarEvento(Long.parseLong(args[1]));

                System.out.println("Evento con id:  " + args[1] + " cancelado con éxito");

            } catch (NumberFormatException | ClientEventoCelebradoException | ClientEventoCanceladoException |
                    InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }else if("-findEvents".equalsIgnoreCase(args[0])) {
            if(args.length==2){
                validateArgs(args, 2, new int[] {});
                try {
                    List<ClientEventoDto> eventos = clientEventoService.encontrarEventosEntre(LocalDateTime.parse(args[1].replace("'","")+"T00:00"), "");

                    System.out.println("Encontrados " + eventos.size() + " evento(s)");
                    for (int i = 0; i < eventos.size(); i++) {
                        ClientEventoDto eventoDto = eventos.get(i);
                        System.out.println("Id: " + eventoDto.getEventId() +
                                ", Nombre: " + eventoDto.getNombre() +
                                ", Descripción: " + eventoDto.getDescr()+
                                ", Fecha: " + eventoDto.getFecha() +
                                ", Respuestas afirmativas: " + eventoDto.getNumEmpS() +
                                ", Respuestas totales: " + eventoDto.getNumEmpT() +
                                ", Cancelado: " + eventoDto.isCancelled() +
                                ", Hora final: " + eventoDto.getHoraFinal());
                    }

                } catch (NumberFormatException |InputValidationException ex) {
                    ex.printStackTrace(System.err);
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }
            }else{
                validateArgs(args, 3, new int[] {});
                try {
                    List<ClientEventoDto> eventos = clientEventoService.encontrarEventosEntre(LocalDateTime.parse(args[1].replace("'","")+"T00:00"), args[2]);

                    System.out.println("Encontrados " + eventos.size() +
                            " evento(s) con keywords '" + args[2] + "'");
                    for (int i = 0; i < eventos.size(); i++) {
                        ClientEventoDto eventoDto = eventos.get(i);
                        System.out.println("Id: " + eventoDto.getEventId() +
                                ", Nombre: " + eventoDto.getNombre() +
                                ", Descripción: " + eventoDto.getDescr()+
                                ", Fecha: " + eventoDto.getFecha() +
                                ", Respuestas afirmativas: " + eventoDto.getNumEmpS() +
                                ", Respuestas totales: " + eventoDto.getNumEmpT() +
                                ", Cancelado: " + eventoDto.isCancelled() +
                                ", Hora final: " + eventoDto.getHoraFinal());
                    }

                } catch (NumberFormatException |InputValidationException ex) {
                    ex.printStackTrace(System.err);
                } catch (Exception ex) {
                    ex.printStackTrace(System.err);
                }
            }

        }else if("-findEvent".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {1});

            try {
                ClientEventoDto eventoDto = clientEventoService.findEvento(Long.parseLong(args[1]));
                System.out.println("Id: " + eventoDto.getEventId() +
                        ", Nombre: " + eventoDto.getNombre() +
                        ", Descripción: " + eventoDto.getDescr() +
                        ", Fecha: " + eventoDto.getFecha() +
                        ", Respuestas afirmativas: " + eventoDto.getNumEmpS() +
                        ", Respuestas totales: " + eventoDto.getNumEmpT() +
                        ", Cancelado: " + eventoDto.isCancelled() +
                        ", Hora final: " + eventoDto.getHoraFinal());

            } catch (NumberFormatException |InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }else if("-findResponses".equalsIgnoreCase(args[0])) {
            validateArgs(args, 3, new int[] {});

            try {
                List<ClientReservaDto> reservas = clientEventoService.encontrarReservas(args[1], Boolean.parseBoolean(args[2]));
                if(!Boolean.parseBoolean(args[2])){
                    System.out.println("Encontradas " + reservas.size() +
                            " reserva(s) del usuario con email '" + args[1] + "'");
                    for (int i = 0; i < reservas.size(); i++) {
                        ClientReservaDto reservaDto = reservas.get(i);
                        System.out.println("Id: " + reservaDto.getReservaId() +
                                ", EventId: " + reservaDto.getEventoId() +
                                ", Empemail: " + reservaDto.getEmpemail() +
                                ", Asistencia: " + reservaDto.isAsistencia());
                    }
                }else{
                    System.out.println("Encontradas " + reservas.size() +
                            " reserva(s) del usuario con email '" + args[1] + "'");
                    for (int i = 0; i < reservas.size(); i++) {
                        ClientReservaDto reservaDto = reservas.get(i);
                        if(reservaDto.isAsistencia()){
                            System.out.println("Id: " + reservaDto.getReservaId() +
                            ", EventId: " + reservaDto.getEventoId() +
                                    ", Empemail: " + reservaDto.getEmpemail() +
                                    ", Asistencia: " + reservaDto.isAsistencia());
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }

    }

    public static void validateArgs(String[] args, int expectedArgs,
                                    int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [addEvent]         EventoServiceClient -addEvent <name> <description> <fecha> <horaFinal>\n" +
                "    [respond]          EventoServiceClient -respond <userEmail> <eventId> <response>\n" +
                "    [cancel]           EventoServiceClient -cancel <eventId>\n" +
                "    [findEvents]       EventoServiceClient -findEvents <fecha> <keyword>\n" +
                "    [findEvent]        EventoServiceClient -findEvent <eventId>\n" +
                "    [findResponses]    EventoServiceClient -findResponses <userEmail> <onlyAffirmative>\n");
    }
}
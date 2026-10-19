package consola.subconsolas;

import clubdeportivo.ClubDeportivo;
import clubdeportivo.deportes.Modalidad;
import clubdeportivo.instalaciones.Instalacion;
import clubdeportivo.instalaciones.Reserva;
import clubdeportivo.instalaciones.TipoInstalacion;
import consola.Subconsola;
import usuarios.tipos.Socio;
import utilidades.excepciones.ReservaNoDisponibleException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import static consola.InputHandler.*;

public class ConsolaInstalaciones extends Subconsola {
    public ConsolaInstalaciones(ClubDeportivo club) {
        super(club);
    }

    @Override
    public String getTitulo() {
        return "Instalaciones y reservas";
    }

    @Override
    protected String[] getOpciones() {
        return new String[] {
                "Registrar reserva",
                "Ver reservas"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                registrarReserva();
                break;
            case 2:
                verReservas();
                break;
        }
    }

    private void registrarReserva() {
        System.out.println();
        System.out.println("--- REGISTRAR RESERVA ---");

        Socio socio = seleccionarSocio("ID del socio: ");

        if (socio == null) {
            return;
        }

        String idInstalacion = readString("ID de la instalación: ");
        Instalacion instalacion = club.buscarInstalacion(idInstalacion);

        try {
            if (instalacion == null) {
                System.out.println("La instalación no existe. Se creará una nueva.");
                mostrarTiposInstalacion();

                String tipoTexto = readString("Tipo de instalación: ").toUpperCase();
                TipoInstalacion tipo = TipoInstalacion.valueOf(tipoTexto);
                int capacidad = readInt("Capacidad máxima: ");
                boolean techada = readBoolean("¿Es techada? (s/n): ");

                instalacion = new Instalacion(idInstalacion, tipo, capacidad, techada);
                club.registrarInstalacion(instalacion);
            }

            String fechaTexto = readString("Fecha de la reserva (AAAA-MM-DD): ");
            String horaTexto = readString("Hora de inicio (HH:MM): ");
            int duracion = readInt("Duración en minutos: ");
            int numeroJugadores = readInt("Número de jugadores: ");
            String modalidadTexto = readString("Modalidad (SENCILLOS/DOBLES): ");

            LocalDate fecha = LocalDate.parse(fechaTexto);
            LocalTime hora = LocalTime.parse(horaTexto);
            Modalidad modalidad = Modalidad.valueOf(modalidadTexto.toUpperCase());

            Reserva reserva = new Reserva(socio, instalacion, fecha, hora,
                    duracion, numeroJugadores, modalidad);

            club.registrarReserva(reserva);

            System.out.println("Reserva registrada correctamente.");
            System.out.println("Horario: " + hora + " - " + reserva.calcularHoraFin());
        } catch (ReservaNoDisponibleException e) {
            System.out.println("No fue posible reservar: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Fecha u hora inválida. Use AAAA-MM-DD y HH:MM.");
        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());
        }
    }

    private void mostrarTiposInstalacion() {
        System.out.println("Tipos disponibles:");

        for (TipoInstalacion tipo : TipoInstalacion.values()) {
            System.out.println("- " + tipo);
        }
    }

    private void verReservas() {
        System.out.println();
        System.out.println("--- RESERVAS REGISTRADAS ---");

        if (club.getReservas().isEmpty()) {
            System.out.println("No hay reservas registradas.");
            return;
        }

        int numero = 1;

        for (Reserva reserva : club.getReservas()) {
            System.out.println();
            System.out.println("Reserva #" + numero);
            System.out.println("Socio: " + reserva.getSocio().getNombre());
            System.out.println("Instalación: " + reserva.getInstalacion().getId());
            System.out.println("Tipo: " + reserva.getInstalacion().getTipo());
            System.out.println("Fecha: " + reserva.getFecha());
            System.out.println("Horario: " + reserva.getHoraInicio()
                    + " - " + reserva.calcularHoraFin());
            System.out.println("Jugadores: " + reserva.getNumeroJugadores());
            System.out.println("Modalidad: " + reserva.getModalidad());
            numero++;
        }
    }
}

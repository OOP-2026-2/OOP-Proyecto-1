package consola.subconsolas;

import clubdeportivo.ClubDeportivo;
import clubdeportivo.turnos.DiaSemana;
import clubdeportivo.turnos.SolicitudCambioTurno;
import clubdeportivo.turnos.Turno;
import consola.Subconsola;
import usuarios.Usuario;
import usuarios.tipos.Empleado;
import usuarios.tipos.empleados.Administrador;
import usuarios.tipos.empleados.Entrenador;
import usuarios.tipos.empleados.Fisioterapeuta;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import static consola.InputHandler.*;

public class ConsolaEmpleados extends Subconsola {
    public ConsolaEmpleados(ClubDeportivo club) {
        super(club);
    }

    @Override
    public String getTitulo() {
        return "Empleados y turnos";
    }

    @Override
    protected String[] getOpciones() {
        return new String[] {
                "Registrar entrenador",
                "Registrar administrador",
                "Registrar fisioterapeuta",
                "Asignar turno a empleado",
                "Solicitar cambio de turno",
                "Decidir solicitud de cambio",
                "Ver turnos y solicitudes"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                registrarEntrenador();
                break;
            case 2:
                registrarAdministrador();
                break;
            case 3:
                registrarFisioterapeuta();
                break;
            case 4:
                asignarTurnoEmpleado();
                break;
            case 5:
                solicitarCambioTurno();
                break;
            case 6:
                decidirSolicitudCambio();
                break;
            case 7:
                verTurnosYSolicitudes();
                break;
        }
    }

    private void mostrarDiasSemana() {
        System.out.println("Días disponibles:");

        for (DiaSemana dia : DiaSemana.values()) {
            System.out.println("- " + dia);
        }
    }

    private void registrarEntrenador() {
        registrarEmpleado("ENTRENADOR");
    }

    private void registrarAdministrador() {
        registrarEmpleado("ADMINISTRADOR");
    }

    private void registrarFisioterapeuta() {
        registrarEmpleado("FISIOTERAPEUTA");
    }

    private void registrarEmpleado(String tipoEmpleado) {
        System.out.println();
        System.out.println("--- REGISTRAR " + tipoEmpleado + " ---");

        String id = readString("ID: ");
        String nombre = readString("Nombre: ");
        String fechaTexto = readString(
                "Fecha de nacimiento (AAAA-MM-DD): ");
        String login = readString("Login: ");
        String contrasena = readString("Contraseña: ");
        String codigoDescuento = readString(
                "Código de descuento: ");

        try {
            LocalDate fechaNacimiento = LocalDate.parse(
                    fechaTexto);

            Empleado empleado;

            if (tipoEmpleado.equals("ENTRENADOR")) {
                empleado = new Entrenador(
                        id, nombre, fechaNacimiento,
                        login, contrasena, codigoDescuento);
            } else if (tipoEmpleado.equals("ADMINISTRADOR")) {
                empleado = new Administrador(
                        id, nombre, fechaNacimiento,
                        login, contrasena, codigoDescuento);
            } else {
                empleado = new Fisioterapeuta(
                        id, nombre, fechaNacimiento,
                        login, contrasena, codigoDescuento);
            }

            club.registrarUsuario(empleado);

            System.out.println(
                    "Empleado registrado correctamente.");
        } catch (DateTimeParseException e) {
            System.out.println(
                    "Fecha inválida. Use AAAA-MM-DD.");
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "No fue posible registrar el empleado: "
                            + e.getMessage());
        }
    }

    private void asignarTurnoEmpleado() {
        System.out.println();
        System.out.println("--- ASIGNAR TURNO ---");

        Empleado empleado = seleccionarEmpleado();

        if (empleado == null) {
            return;
        }

        mostrarDiasSemana();
        String diaTexto = readString("Día: ").toUpperCase();
        String inicioTexto = readString("Hora de inicio (HH:MM): ");
        String finTexto = readString("Hora final (HH:MM): ");

        try {
            Turno turno = new Turno(
                    DiaSemana.valueOf(diaTexto),
                    LocalTime.parse(inicioTexto),
                    LocalTime.parse(finTexto));

            club.asignarTurno(empleado, turno);

            System.out.println(
                    "Turno asignado correctamente.");
        } catch (DateTimeParseException e) {
            System.out.println(
                    "Hora inválida. Use HH:MM.");
        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            System.out.println(
                    "No fue posible asignar el turno: "
                            + e.getMessage());
        }
    }

    private void solicitarCambioTurno() {
        System.out.println();
        System.out.println("--- SOLICITAR CAMBIO DE TURNO ---");

        Empleado empleado = seleccionarEmpleado();

        if (empleado == null) {
            return;
        }

        Turno turno = seleccionarTurno(empleado);

        if (turno == null) {
            return;
        }

        mostrarDiasSemana();
        String diaTexto = readString("Nuevo día: ").toUpperCase();
        String inicioTexto = readString(
                "Nueva hora de inicio (HH:MM): ");
        String finTexto = readString(
                "Nueva hora final (HH:MM): ");

        try {
            SolicitudCambioTurno solicitud = club.solicitarCambioTurno(
                    empleado,
                    turno,
                    DiaSemana.valueOf(diaTexto),
                    LocalTime.parse(inicioTexto),
                    LocalTime.parse(finTexto));

            System.out.println(
                    "Solicitud registrada correctamente.");
            System.out.println(solicitud);
        } catch (DateTimeParseException e) {
            System.out.println(
                    "Hora inválida. Use HH:MM.");
        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            System.out.println(
                    "No fue posible registrar la solicitud: "
                            + e.getMessage());
        }
    }

    private void decidirSolicitudCambio() {
        System.out.println();
        System.out.println("--- DECIDIR SOLICITUD DE CAMBIO ---");

        SolicitudCambioTurno solicitud = seleccionarSolicitud();

        if (solicitud == null) {
            return;
        }

        String idAdministrador = readString(
                "ID del administrador: ");

        Usuario usuario = club.buscarUsuario(idAdministrador);

        if (!(usuario instanceof Administrador)) {
            System.out.println(
                    "No existe un administrador con ese ID.");
            return;
        }

        boolean aprobar = readBoolean(
                "¿Desea aprobar la solicitud? (s/n): ");

        try {
            if (aprobar) {
                club.aprobarCambioTurno(
                        solicitud,
                        (Administrador) usuario);

                System.out.println(
                        "Solicitud aprobada y turno actualizado.");
            } else {
                club.rechazarCambioTurno(
                        solicitud,
                        (Administrador) usuario);

                System.out.println(
                        "Solicitud rechazada.");
            }
        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            System.out.println(
                    "No fue posible decidir la solicitud: "
                            + e.getMessage());
        }
    }

    private void verTurnosYSolicitudes() {
        System.out.println();
        System.out.println("--- TURNOS Y SOLICITUDES ---");

        boolean hayEmpleados = false;

        for (Usuario usuario : club.getUsuarios()) {
            if (!(usuario instanceof Empleado)) {
                continue;
            }

            hayEmpleados = true;
            Empleado empleado = (Empleado) usuario;

            System.out.println();
            System.out.println(
                    empleado.getNombre()
                            + " | "
                            + empleado.getClass().getSimpleName());

            if (empleado.getTurnos().isEmpty()) {
                System.out.println("Sin turnos asignados.");
            } else {
                for (Turno turno : empleado.getTurnos()) {
                    System.out.println("- " + turno);
                }
            }
        }

        if (!hayEmpleados) {
            System.out.println("No hay empleados registrados.");
        }

        System.out.println();
        System.out.println("Solicitudes:");

        if (club.getSolicitudesCambioTurno().isEmpty()) {
            System.out.println("No hay solicitudes registradas.");
        } else {
            for (SolicitudCambioTurno solicitud : club.getSolicitudesCambioTurno()) {

                System.out.println("- " + solicitud);
            }
        }
    }

    private Turno seleccionarTurno(Empleado empleado) {
        if (empleado.getTurnos().isEmpty()) {
            System.out.println(
                    "El empleado no tiene turnos asignados.");
            return null;
        }

        int numero = 1;
        for (Turno turno : empleado.getTurnos()) {
            System.out.println(numero + ". " + turno);
            numero++;
        }

        int seleccion = readInt("Número del turno: ");

        if (seleccion < 1
                || seleccion > empleado.getTurnos().size()) {

            System.out.println("El turno seleccionado no es válido.");
            return null;
        }

        return empleado.getTurnos().get(seleccion - 1);
    }

    private SolicitudCambioTurno seleccionarSolicitud() {
        if (club.getSolicitudesCambioTurno().isEmpty()) {
            System.out.println(
                    "No hay solicitudes registradas.");
            return null;
        }

        int numero = 1;
        for (SolicitudCambioTurno solicitud : club.getSolicitudesCambioTurno()) {

            System.out.println(numero + ". " + solicitud);
            numero++;
        }

        int seleccion = readInt(
                "Número de la solicitud: ");

        if (seleccion < 1
                || seleccion > club.getSolicitudesCambioTurno().size()) {

            System.out.println(
                    "La solicitud seleccionada no es válida.");
            return null;
        }

        return club.getSolicitudesCambioTurno().get(
                seleccion - 1);
    }
}

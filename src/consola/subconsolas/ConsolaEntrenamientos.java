package consola.subconsolas;

import clubdeportivo.ClubDeportivo;
import clubdeportivo.deportes.DeporteIndividual;
import clubdeportivo.deportes.Equipo;
import clubdeportivo.entrenamientos.EstadoAsistencia;
import clubdeportivo.entrenamientos.RegistroAsistencia;
import clubdeportivo.entrenamientos.SesionEntrenamiento;
import clubdeportivo.entrenamientos.TipoSesion;
import clubdeportivo.instalaciones.Instalacion;
import consola.Subconsola;
import usuarios.Usuario;
import usuarios.tipos.Socio;
import usuarios.tipos.empleados.Entrenador;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import static consola.InputHandler.*;

public class ConsolaEntrenamientos extends Subconsola {
    public ConsolaEntrenamientos(ClubDeportivo club) {
        super(club);
    }

    @Override
    public String getTitulo() {
        return "Entrenamientos";
    }

    @Override
    protected String[] getOpciones() {
        return new String[] {
                "Programar sesión de entrenamiento",
                "Registrar socio en una sesión",
                "Registrar asistencia",
                "Registrar evaluación",
                "Ver sesiones y asistencias"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                programarSesionEntrenamiento();
                break;
            case 2:
                registrarSocioEnSesion();
                break;
            case 3:
                registrarAsistencia();
                break;
            case 4:
                registrarEvaluacion();
                break;
            case 5:
                verSesionesYAsistencias();
                break;
        }
    }

    private void programarSesionEntrenamiento() {
        System.out.println();
        System.out.println(
                "--- PROGRAMAR SESIÓN DE ENTRENAMIENTO ---");

        String idEntrenador = readString(
                "ID del entrenador: ");

        Usuario usuario = club.buscarUsuario(idEntrenador);

        if (!(usuario instanceof Entrenador)) {
            System.out.println(
                    "No existe un entrenador con ese ID.");
            return;
        }

        String idInstalacion = readString(
                "ID de la instalación: ");

        Instalacion instalacion = club.buscarInstalacion(
                idInstalacion);

        if (instalacion == null) {
            System.out.println(
                    "No existe una instalación con ese ID.");
            return;
        }

        String fechaTexto = readString(
                "Fecha de la sesión (AAAA-MM-DD): ");

        String horaTexto = readString(
                "Hora de inicio (HH:MM): ");

        int duracion = readInt(
                "Duración en minutos: ");

        System.out.println("Tipos de sesión disponibles:");
        for (TipoSesion tipo : TipoSesion.values()) {
            System.out.println("- " + tipo);
        }

        String tipoTexto = readString(
                "Tipo de sesión: ").toUpperCase();

        try {
            TipoSesion tipo = TipoSesion.valueOf(
                    tipoTexto);

            SesionEntrenamiento sesion = new SesionEntrenamiento(
                    LocalDate.parse(fechaTexto),
                    LocalTime.parse(horaTexto),
                    duracion,
                    (Entrenador) usuario,
                    instalacion,
                    tipo);

            if (tipo == TipoSesion.GRUPAL) {
                String nombreEquipo = readString(
                        "Nombre del equipo: ");

                Equipo equipo = club.buscarEquipo(
                        nombreEquipo);

                if (equipo == null) {
                    System.out.println(
                            "No existe ese equipo.");
                    return;
                }

                sesion.asignarEquipo(equipo);
            } else {
                String nombreDeporte = readString(
                        "Nombre del deporte individual: ");

                DeporteIndividual deporte = club
                        .buscarDeporteIndividual(
                                nombreDeporte);

                if (deporte == null) {
                    System.out.println(
                            "No existe ese deporte individual.");
                    return;
                }

                sesion.asignarDeporteIndividual(
                        deporte);
            }

            club.programarSesion(sesion);

            System.out.println(
                    "Sesión programada correctamente.");
        } catch (DateTimeParseException e) {
            System.out.println(
                    "Fecha u hora inválida. Use AAAA-MM-DD y HH:MM.");
        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            System.out.println(
                    "No fue posible programar la sesión: "
                            + e.getMessage());
        }
    }

    private void registrarSocioEnSesion() {
        System.out.println();
        System.out.println(
                "--- REGISTRAR SOCIO EN UNA SESIÓN ---");

        SesionEntrenamiento sesion = seleccionarSesion();

        if (sesion == null) {
            return;
        }

        Socio socio = seleccionarSocio("ID del socio: ");

        if (socio == null) {
            return;
        }

        try {
            RegistroAsistencia registro = club.registrarAsistencia(
                    sesion,
                    socio);

            System.out.println(
                    "Socio registrado en la sesión.");
            System.out.println(registro);
        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            System.out.println(
                    "No fue posible registrar al socio: "
                            + e.getMessage());
        }
    }

    private void registrarAsistencia() {
        System.out.println();
        System.out.println("--- REGISTRAR ASISTENCIA ---");

        RegistroAsistencia registro = seleccionarRegistro();

        if (registro == null) {
            return;
        }

        System.out.println("Estados disponibles:");
        System.out.println("- ASISTIO");
        System.out.println("- AUSENTE");
        System.out.println("- TARDE");

        String estadoTexto = readString(
                "Estado: ").toUpperCase();

        try {
            EstadoAsistencia estado = EstadoAsistencia.valueOf(
                    estadoTexto);

            if (estado == EstadoAsistencia.PENDIENTE) {
                throw new IllegalArgumentException(
                        "Debe registrar un estado definitivo");
            }

            registro.registrarAsistencia(estado);

            System.out.println(
                    "Asistencia actualizada correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "No fue posible registrar la asistencia: "
                            + e.getMessage());
        }
    }

    private void registrarEvaluacion() {
        System.out.println();
        System.out.println("--- REGISTRAR EVALUACIÓN ---");

        RegistroAsistencia registro = seleccionarRegistro();

        if (registro == null) {
            return;
        }

        double nota = readDouble(
                "Nota de desempeño (1 a 5): ");

        String comentario = readString("Comentario: ");

        try {
            registro.registrarEvaluacion(
                    nota,
                    comentario);

            System.out.println(
                    "Evaluación registrada correctamente.");
        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            System.out.println(
                    "No fue posible registrar la evaluación: "
                            + e.getMessage());
        }
    }

    private void verSesionesYAsistencias() {
        System.out.println();
        System.out.println("--- SESIONES Y ASISTENCIAS ---");

        if (club.getSesionesEntrenamiento().isEmpty()) {
            System.out.println(
                    "No hay sesiones registradas.");
            return;
        }

        int numero = 1;

        for (SesionEntrenamiento sesion : club.getSesionesEntrenamiento()) {

            System.out.println();
            System.out.println(
                    "Sesión #" + numero + ": " + sesion);

            System.out.println(
                    "Entrenador: "
                            + sesion.getEntrenador().getNombre());

            if (sesion.getEquipo() != null) {
                System.out.println(
                        "Equipo: "
                                + sesion.getEquipo().getNombre());
            } else {
                System.out.println(
                        "Deporte: "
                                + sesion.getDeporteIndividual()
                                .getNombre());
            }

            if (sesion.getRegistros().isEmpty()) {
                System.out.println(
                        "No tiene asistencias registradas.");
            } else {
                for (RegistroAsistencia registro : sesion.getRegistros()) {

                    System.out.println(
                            "- " + registro);
                }
            }

            numero++;
        }
    }

    private SesionEntrenamiento seleccionarSesion() {
        if (club.getSesionesEntrenamiento().isEmpty()) {
            System.out.println(
                    "No hay sesiones registradas.");
            return null;
        }

        System.out.println("Sesiones disponibles:");

        int numero = 1;
        for (SesionEntrenamiento sesion : club.getSesionesEntrenamiento()) {

            System.out.println(numero + ". " + sesion);
            numero++;
        }

        int seleccion = readInt(
                "Número de la sesión: ");

        if (seleccion < 1
                || seleccion > club.getSesionesEntrenamiento().size()) {

            System.out.println(
                    "La sesión seleccionada no es válida.");
            return null;
        }

        return club.getSesionesEntrenamiento().get(
                seleccion - 1);
    }

    private RegistroAsistencia seleccionarRegistro() {
        SesionEntrenamiento sesion = seleccionarSesion();

        if (sesion == null) {
            return null;
        }

        String idSocio = readString("ID del socio: ");

        for (RegistroAsistencia registro : sesion.getRegistros()) {

            if (registro.getSocio().getId()
                    .equalsIgnoreCase(idSocio)) {

                return registro;
            }
        }

        System.out.println(
                "El socio no está registrado en esa sesión.");
        return null;
    }
}

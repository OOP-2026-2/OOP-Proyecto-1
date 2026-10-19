package clubdeportivo.entrenamientos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import clubdeportivo.deportes.DeporteIndividual;
import clubdeportivo.deportes.Equipo;
import clubdeportivo.deportes.InscripcionAcademia;
import clubdeportivo.deportes.InscripcionEquipo;
import clubdeportivo.instalaciones.Instalacion;
import usuarios.tipos.Socio;
import usuarios.tipos.empleados.Entrenador;

public class SesionEntrenamiento {
    private final LocalDate fecha;
    private final LocalTime horaInicio;
    private final int duracionMinutos;
    private final Entrenador entrenador;
    private final Instalacion instalacion;
    private Equipo equipo;
    private DeporteIndividual deporteIndividual;
    private TipoSesion tipo;
    private final List<RegistroAsistencia> registros;

    public SesionEntrenamiento(LocalDate fecha, LocalTime horaInicio,
            int duracionMinutos, Entrenador entrenador,
            Instalacion instalacion) {

        this(fecha, horaInicio, duracionMinutos,
                entrenador, instalacion,
                TipoSesion.GRUPAL, true);
    }

    public SesionEntrenamiento(LocalDate fecha, LocalTime horaInicio,
            int duracionMinutos, Entrenador entrenador,
            Instalacion instalacion, TipoSesion tipo) {

        this(fecha, horaInicio, duracionMinutos,
                entrenador, instalacion,
                tipo, true);
    }

    private SesionEntrenamiento(LocalDate fecha,
            LocalTime horaInicio,
            int duracionMinutos,
            Entrenador entrenador,
            Instalacion instalacion,
            TipoSesion tipo,
            boolean validarFecha) {

        if (fecha == null || horaInicio == null
                || entrenador == null
                || instalacion == null
                || tipo == null) {

            throw new IllegalArgumentException(
                    "Los datos de la sesión son obligatorios");
        }

        if (validarFecha
                && fecha.isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "No se pueden programar sesiones en fechas pasadas");
        }

        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException(
                    "La duración debe ser mayor que cero");
        }

        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracionMinutos = duracionMinutos;
        this.entrenador = entrenador;
        this.instalacion = instalacion;
        this.tipo = tipo;
        this.registros = new ArrayList<RegistroAsistencia>();
    }

    public static SesionEntrenamiento crearDesdePersistencia(
            LocalDate fecha,
            LocalTime horaInicio,
            int duracionMinutos,
            Entrenador entrenador,
            Instalacion instalacion,
            TipoSesion tipo) {

        return new SesionEntrenamiento(
                fecha,
                horaInicio,
                duracionMinutos,
                entrenador,
                instalacion,
                tipo,
                false);
    }

    public void asignarEquipo(Equipo equipo) {
        if (equipo == null) {
            throw new IllegalArgumentException(
                    "El equipo es obligatorio");
        }

        if (!equipo.isActivo()) {
            throw new IllegalArgumentException(
                    "El equipo debe estar activo");
        }

        asignarEquipoInternamente(equipo);
    }

    public void asignarEquipoDesdePersistencia(
            Equipo equipo) {

        if (equipo == null) {
            throw new IllegalArgumentException(
                    "El equipo es obligatorio");
        }

        asignarEquipoInternamente(equipo);
    }

    private void asignarEquipoInternamente(
            Equipo equipo) {

        this.equipo = equipo;
        this.deporteIndividual = null;
        this.tipo = TipoSesion.GRUPAL;
    }

    public void asignarDeporteIndividual(
            DeporteIndividual deporteIndividual) {

        if (deporteIndividual == null) {
            throw new IllegalArgumentException(
                    "El deporte individual es obligatorio");
        }

        this.deporteIndividual = deporteIndividual;
        this.equipo = null;

        if (tipo == TipoSesion.GRUPAL) {
            tipo = TipoSesion.INDIVIDUAL;
        }
    }

    public LocalTime calcularHoraFin() {
        return horaInicio.plusMinutes(
                duracionMinutos);
    }

    public boolean seCruzaCon(
            SesionEntrenamiento otra) {

        if (otra == null) {
            return false;
        }

        if (!fecha.equals(otra.getFecha())) {
            return false;
        }

        boolean horariosSuperpuestos = horaInicio.isBefore(
                otra.calcularHoraFin())
                && otra.getHoraInicio()
                        .isBefore(
                                calcularHoraFin());

        if (!horariosSuperpuestos) {
            return false;
        }

        boolean mismaInstalacion = instalacion == otra.getInstalacion();

        boolean mismoEntrenador = entrenador == otra.getEntrenador();

        return mismaInstalacion
                || mismoEntrenador;
    }

    public void agregarRegistro(
            RegistroAsistencia registro) {

        if (registro == null) {
            throw new IllegalArgumentException(
                    "El registro de asistencia no puede ser nulo");
        }

        if (registro.getSesion() != this) {
            throw new IllegalArgumentException(
                    "El registro pertenece a otra sesión");
        }

        if (!estaHabilitado(
                registro.getSocio())) {

            throw new IllegalArgumentException(
                    "El socio no tiene una inscripción activa "
                            + "para esta sesión");
        }

        for (RegistroAsistencia existente : registros) {
            if (existente.getSocio() == registro.getSocio()) {

                throw new IllegalArgumentException(
                        "El socio ya tiene un registro en esta sesión");
            }
        }

        registros.add(registro);
    }

    public void agregarRegistroDesdePersistencia(
            RegistroAsistencia registro) {

        if (registro == null) {
            throw new IllegalArgumentException(
                    "El registro de asistencia no puede ser nulo");
        }

        if (registro.getSesion() != this) {
            throw new IllegalArgumentException(
                    "El registro pertenece a otra sesión");
        }

        for (RegistroAsistencia existente : registros) {
            if (existente.getSocio() == registro.getSocio()) {

                throw new IllegalArgumentException(
                        "El socio ya tiene un registro en esta sesión");
            }
        }

        registros.add(registro);
    }

    public boolean estaHabilitado(
            Socio socio) {

        if (socio == null) {
            return false;
        }

        if (equipo != null) {
            InscripcionEquipo inscripcion = equipo.buscarInscripcion(socio);

            return equipo.isActivo()
                    && inscripcion != null
                    && inscripcion.estaActiva();
        }

        if (deporteIndividual != null) {
            for (InscripcionAcademia inscripcion : deporteIndividual.getInscripciones()) {

                if (inscripcion.getSocio() == socio
                        && inscripcion.estaActiva()) {

                    return true;
                }
            }
        }

        return false;
    }

    public boolean cumpleCoberturaMinima() {
        int registrosActivos = 0;

        for (RegistroAsistencia registro : registros) {
            if (registro.getEstado() != EstadoAsistencia.AUSENTE) {

                registrosActivos++;
            }
        }

        switch (tipo) {
            case INDIVIDUAL:
                return deporteIndividual != null
                        && registrosActivos == 1;

            case GRUPO_PEQUENO:
                return deporteIndividual != null
                        && registrosActivos >= 2;

            case GRUPAL:
                return equipo != null
                        && equipo.getDeporte()
                                .cumpleMinimoJugadores(
                                        registrosActivos);

            default:
                return false;
        }
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public Entrenador getEntrenador() {
        return entrenador;
    }

    public Instalacion getInstalacion() {
        return instalacion;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public DeporteIndividual getDeporteIndividual() {
        return deporteIndividual;
    }

    public TipoSesion getTipo() {
        return tipo;
    }

    public List<RegistroAsistencia> getRegistros() {
        return new ArrayList<RegistroAsistencia>(
                registros);
    }

    @Override
    public String toString() {
        return fecha
                + " " + horaInicio
                + " - " + calcularHoraFin()
                + " - " + tipo
                + " - " + instalacion.getId();
    }
}
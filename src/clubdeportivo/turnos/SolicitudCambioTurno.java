package clubdeportivo.turnos;

import java.time.LocalDate;
import java.time.LocalTime;
import usuarios.tipos.Empleado;
import usuarios.tipos.empleados.Administrador;

public class SolicitudCambioTurno {
    private final Empleado empleadoSolicitante;
    private final Turno turnoOriginal;
    private final LocalDate fechaSolicitud;
    private final DiaSemana nuevoDia;
    private final LocalTime nuevaHoraInicio;
    private final LocalTime nuevaHoraFin;
    private EstadoSolicitud estado;
    private Administrador administradorDecision;

    public SolicitudCambioTurno(Empleado empleadoSolicitante,
            Turno turnoOriginal, DiaSemana nuevoDia,
            LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin) {

        if (empleadoSolicitante == null) {
            throw new IllegalArgumentException(
                    "El empleado solicitante es obligatorio");
        }

        if (turnoOriginal == null) {
            throw new IllegalArgumentException(
                    "El turno original es obligatorio");
        }

        if (!empleadoSolicitante.getTurnos()
                .contains(turnoOriginal)) {

            throw new IllegalArgumentException(
                    "El turno original no pertenece al empleado");
        }

        if (nuevoDia == null) {
            throw new IllegalArgumentException(
                    "El nuevo día es obligatorio");
        }

        if (nuevaHoraInicio == null
                || nuevaHoraFin == null) {

            throw new IllegalArgumentException(
                    "Las horas solicitadas son obligatorias");
        }

        if (!nuevaHoraFin.isAfter(nuevaHoraInicio)) {
            throw new IllegalArgumentException(
                    "La hora final debe ser posterior a la inicial");
        }

        this.empleadoSolicitante = empleadoSolicitante;
        this.turnoOriginal = turnoOriginal;
        this.fechaSolicitud = LocalDate.now();
        this.nuevoDia = nuevoDia;
        this.nuevaHoraInicio = nuevaHoraInicio;
        this.nuevaHoraFin = nuevaHoraFin;
        this.estado = EstadoSolicitud.PENDIENTE;
        this.administradorDecision = null;
    }

    public void aprobar(Administrador administrador) {
        validarDecision(administrador);
        this.administradorDecision = administrador;
        this.estado = EstadoSolicitud.APROBADA;
    }

    public void rechazar(Administrador administrador) {
        validarDecision(administrador);
        this.administradorDecision = administrador;
        this.estado = EstadoSolicitud.RECHAZADA;
    }

    private void validarDecision(
            Administrador administrador) {

        if (administrador == null) {
            throw new IllegalArgumentException(
                    "El administrador es obligatorio");
        }

        if (!estaPendiente()) {
            throw new IllegalStateException(
                    "La solicitud ya fue decidida");
        }
    }

    public Turno crearTurnoSolicitado() {
        return new Turno(
                nuevoDia,
                nuevaHoraInicio,
                nuevaHoraFin);
    }

    public boolean estaPendiente() {
        return estado == EstadoSolicitud.PENDIENTE;
    }

    public Empleado getEmpleadoSolicitante() {
        return empleadoSolicitante;
    }

    public Turno getTurnoOriginal() {
        return turnoOriginal;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public DiaSemana getNuevoDia() {
        return nuevoDia;
    }

    public LocalTime getNuevaHoraInicio() {
        return nuevaHoraInicio;
    }

    public LocalTime getNuevaHoraFin() {
        return nuevaHoraFin;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public Administrador getAdministradorDecision() {
        return administradorDecision;
    }

    @Override
    public String toString() {
        return empleadoSolicitante.getNombre()
                + " | " + turnoOriginal
                + " -> " + nuevoDia
                + " " + nuevaHoraInicio
                + " - " + nuevaHoraFin
                + " | " + estado;
    }
}
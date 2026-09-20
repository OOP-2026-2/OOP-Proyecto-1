package clubdeportivo.turnos;

import usuarios.tipos.empleados.Administrador;
import usuarios.tipos.Empleado;

import java.time.LocalDate;
import java.time.LocalTime;

public class SolicitudCambioTurno {
    private LocalDate fechaSolicitud;
    private EstadoSolicitud estado;
    private String nuevoDiaSemana;
    private LocalTime nuevaHoraInicio;
    private LocalTime nuevaHoraFin;
    private Empleado empleado;
    private Turno turno;
    private Administrador administrador;

    public SolicitudCambioTurno(Empleado empleado, Turno turno, String nuevoDiaSemana, LocalTime nuevaHoraInicio,
            LocalTime nuevaHoraFin) {
        this.fechaSolicitud = LocalDate.now();
        this.estado = EstadoSolicitud.PENDIENTE;
        this.empleado = empleado;
        this.turno = turno;
        this.nuevoDiaSemana = nuevoDiaSemana;
        this.nuevaHoraInicio = nuevaHoraInicio;
        this.nuevaHoraFin = nuevaHoraFin;
    }

    public void aprobar(Administrador administrador) {
        this.administrador = administrador;
        this.estado = EstadoSolicitud.APROBADA;
    }

    public void rechazar(Administrador administrador) {
        this.administrador = administrador;
        this.estado = EstadoSolicitud.RECHAZADA;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public String getNuevoDiaSemana() {
        return nuevoDiaSemana;
    }

    public LocalTime getNuevaHoraInicio() {
        return nuevaHoraInicio;
    }

    public LocalTime getNuevaHoraFin() {
        return nuevaHoraFin;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Turno getTurno() {
        return turno;
    }

    public Administrador getAdministrador() {
        return administrador;
    }
}

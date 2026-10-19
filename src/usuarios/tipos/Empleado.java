package usuarios.tipos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import clubdeportivo.turnos.DiaSemana;
import clubdeportivo.turnos.EstadoSolicitud;
import clubdeportivo.turnos.SolicitudCambioTurno;
import clubdeportivo.turnos.Turno;
import usuarios.Comprador;
import usuarios.Usuario;

public abstract class Empleado extends Usuario implements Comprador {
    private final String codigoDescuento;
    private final List<Turno> turnos;
    private final List<SolicitudCambioTurno> solicitudesCambio;

    public Empleado(String id, String nombre, LocalDate fechaNacimiento,
            String login, String contrasena, String codigoDescuento) {

        super(id, nombre, fechaNacimiento, login, contrasena);

        if (codigoDescuento == null
                || codigoDescuento.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El código de descuento es obligatorio");
        }

        this.codigoDescuento = codigoDescuento;
        this.turnos = new ArrayList<Turno>();
        this.solicitudesCambio = new ArrayList<SolicitudCambioTurno>();
    }

    public void agregarTurno(Turno turno) {
        if (turno == null) {
            throw new IllegalArgumentException(
                    "El turno no puede ser nulo");
        }

        for (Turno existente : turnos) {
            if (existente.seCruzaCon(turno)) {
                throw new IllegalArgumentException(
                        "El turno se cruza con otro turno del empleado");
            }
        }

        if (!turnos.contains(turno)) {
            turnos.add(turno);
        }
    }

    public SolicitudCambioTurno solicitarCambioTurno(
            Turno turno, DiaSemana nuevoDia,
            LocalTime nuevaHoraInicio,
            LocalTime nuevaHoraFin) {

        if (turno == null || !turnos.contains(turno)) {
            throw new IllegalArgumentException(
                    "El turno no pertenece al empleado");
        }

        for (SolicitudCambioTurno existente : solicitudesCambio) {

            if (existente.getTurnoOriginal() == turno
                    && existente.estaPendiente()) {

                throw new IllegalStateException(
                        "Ya existe una solicitud pendiente "
                                + "para este turno");
            }
        }

        Turno turnoSolicitado = new Turno(
                nuevoDia,
                nuevaHoraInicio,
                nuevaHoraFin);

        for (Turno existente : turnos) {
            if (existente != turno
                    && existente.seCruzaCon(
                            turnoSolicitado)) {

                throw new IllegalArgumentException(
                        "El nuevo horario se cruza "
                                + "con otro turno del empleado");
            }
        }

        SolicitudCambioTurno solicitud = new SolicitudCambioTurno(
                this,
                turno,
                nuevoDia,
                nuevaHoraInicio,
                nuevaHoraFin);

        solicitudesCambio.add(solicitud);
        return solicitud;
    }

    public void aplicarCambioTurno(
            SolicitudCambioTurno solicitud) {

        if (solicitud == null) {
            throw new IllegalArgumentException(
                    "La solicitud es obligatoria");
        }

        if (solicitud.getEmpleadoSolicitante() != this) {
            throw new IllegalArgumentException(
                    "La solicitud pertenece a otro empleado");
        }

        if (solicitud.getEstado() != EstadoSolicitud.APROBADA) {

            throw new IllegalStateException(
                    "La solicitud debe estar aprobada");
        }

        if (!turnos.contains(
                solicitud.getTurnoOriginal())) {

            throw new IllegalStateException(
                    "El turno original ya no pertenece al empleado");
        }

        Turno nuevoTurno = solicitud.crearTurnoSolicitado();

        for (Turno existente : turnos) {
            if (existente != solicitud.getTurnoOriginal()
                    && existente.seCruzaCon(nuevoTurno)) {

                throw new IllegalStateException(
                        "El nuevo turno se cruza con otro turno");
            }
        }

        turnos.remove(solicitud.getTurnoOriginal());
        turnos.add(nuevoTurno);
    }

    @Override
    public double getPorcentajeDescuento() {
        return 0.15;
    }

    public String getCodigoDescuento() {
        return codigoDescuento;
    }

    public List<Turno> getTurnos() {
        return new ArrayList<Turno>(turnos);
    }

    public List<SolicitudCambioTurno> getSolicitudesCambio() {
        return new ArrayList<SolicitudCambioTurno>(
                solicitudesCambio);
    }
}
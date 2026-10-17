package usuarios.tipos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import clubdeportivo.turnos.DiaSemana;
import clubdeportivo.turnos.SolicitudCambioTurno;
import clubdeportivo.turnos.Turno;
import usuarios.Comprador;
import usuarios.Usuario;

public abstract class Empleado extends Usuario implements Comprador {
    private final String codigoDescuento;
    private ArrayList<Turno> turnos;
    private ArrayList<SolicitudCambioTurno> solicitudesCambio;

    public Empleado(String id, String nombre, LocalDate fechaNacimiento, String login, String contrasena,
            String codigoDescuento) {
        super(id, nombre, fechaNacimiento, login, contrasena);
        if (codigoDescuento == null || codigoDescuento.trim().isEmpty()) {
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

        if (!turnos.contains(turno)) {
            turnos.add(turno);
        }
    }

    public SolicitudCambioTurno solicitarCambioTurno(
            Turno turno,
            DiaSemana nuevoDia,
            LocalTime nuevaHoraInicio,
            LocalTime nuevaHoraFin) {

        if (turno == null || !turnos.contains(turno)) {
            throw new IllegalArgumentException(
                    "El turno no pertenece al empleado");
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

    @Override
    public double getPorcentajeDescuento() {
        return 0.15;
    }

    public String getCodigoDescuento() {
        return codigoDescuento;
    }

    public ArrayList<Turno> getTurnos() {
        return new ArrayList<Turno>(turnos);
    }

    public ArrayList<SolicitudCambioTurno> getSolicitudesCambio() {

        return new ArrayList<SolicitudCambioTurno>(
                solicitudesCambio);
    }
}
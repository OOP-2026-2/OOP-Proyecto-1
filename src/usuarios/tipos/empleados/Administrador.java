package usuarios.tipos.empleados;

import java.time.LocalDate;

import clubdeportivo.turnos.SolicitudCambioTurno;
import usuarios.Usuario;

public class Administrador extends Usuario {
    public Administrador(String id, String nombre,
            LocalDate fechaNacimiento, String login,
            String contrasena) {

        super(id, nombre, fechaNacimiento, login, contrasena);
    }

    public void aprobarSolicitud(
            SolicitudCambioTurno solicitud) {

        if (solicitud == null) {
            throw new IllegalArgumentException(
                    "La solicitud no puede ser nula");
        }

        solicitud.aprobar(this);
    }

    public void rechazarSolicitud(
            SolicitudCambioTurno solicitud) {

        if (solicitud == null) {
            throw new IllegalArgumentException(
                    "La solicitud no puede ser nula");
        }

        solicitud.rechazar(this);
    }
}
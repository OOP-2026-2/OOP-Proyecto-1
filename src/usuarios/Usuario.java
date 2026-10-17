package usuarios;

import java.time.LocalDate;

public abstract class Usuario {
    private final String id;
    private final String nombre;
    private final LocalDate fechaNacimiento;
    private final String login;
    private String contrasena;

    public Usuario(
            String id,
            String nombre,
            LocalDate fechaNacimiento,
            String login,
            String contrasena) {

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El ID es obligatorio");
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio");
        }

        if (fechaNacimiento == null) {
            throw new IllegalArgumentException(
                    "La fecha de nacimiento es obligatoria");
        }

        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El login es obligatorio");
        }

        if (contrasena == null
                || contrasena.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "La contraseña es obligatoria");
        }

        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.login = login;
        this.contrasena = contrasena;
    }

    public boolean validarContrasena(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    public void cambiarContrasena(
            String contrasenaActual,
            String nuevaContrasena) {

        if (!validarContrasena(contrasenaActual)) {
            throw new IllegalArgumentException(
                    "La contraseña actual es incorrecta");
        }

        if (nuevaContrasena == null
                || nuevaContrasena.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "La nueva contraseña no puede estar vacía");
        }

        this.contrasena = nuevaContrasena;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getLogin() {
        return login;
    }

    public String getContrasena() {
        return contrasena;
    }
}
package ClubDeportivo.usuarios;

public abstract class Usuario {
    private String id;
    private String nombre;
    private String login;
    private String contrasena;

    public Usuario(String id, String nombre, String login, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.login = login;
        this.contrasena = contrasena;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLogin() {
        return login;
    }

    public String getContrasena() {
        return contrasena;
    }

    public boolean validarContrasena(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    public void cambiarContrasena(String contrasenaActual, String nuevaContrasena) {
        if (!validarContrasena(contrasenaActual)) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }
        if (nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña no puede estar vacía");
        }
        this.contrasena = nuevaContrasena;
    }
}

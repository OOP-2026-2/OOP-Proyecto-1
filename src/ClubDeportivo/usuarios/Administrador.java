package ClubDeportivo.usuarios;

public class Administrador extends Usuario {
    public Administrador(String id, String nombre, String login, String contrasena) {
        super(id, nombre, login, contrasena);
    }

    public Administrador(String id, String nombre) {
        this(id, nombre, id, id);
    }
}

package usuarios.tipos;

import usuarios.Usuario;

public class Socio extends Usuario {
    private int categoriaEdad;

    public Socio(String id, String nombre, int fechaNacimiento, String login, String password) {
        super(id, nombre, fechaNacimiento, login, password);
    }
}

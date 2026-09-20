package usuarios;

public abstract class Usuario {
    private final String id;
    private final String nombre;
    private final String login;
    private final int fechaNacimiento;
    private String password;


    public Usuario(String id, String nombre, int fechaNacimiento, String login, String password) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.login = login;
        this.password = password;
    }


    //Métodos
    public boolean validarContrasena(String password) {return this.password.equals(password);}

    public void cambiarContrasena(String currentPassword, String newPassword) throws IllegalArgumentException {

        if (!validarContrasena(currentPassword)) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña no puede estar vacía");
        }

        this.password = newPassword;
    }

    //Getters
    public String getId() {return id;}
    public String getNombre() {return nombre;}
    public int getFechaNacimiento(){return fechaNacimiento;}
    public String getLogin() {return login;}
    public String getPassword() {return password;}

}

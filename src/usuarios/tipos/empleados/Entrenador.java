package usuarios.tipos.empleados;

import usuarios.tipos.Empleado;

public class Entrenador extends Empleado {

    public Entrenador(String id, String nombre, int fechaNacimiento,
                         String login, String contrasena, String codigoDescuento) {

        super(id, nombre, fechaNacimiento, login, contrasena, codigoDescuento);
    }
}

package usuarios.tipos.empleados;

import usuarios.tipos.Empleado;

public class Fisioterapeuta extends Empleado {

    public Fisioterapeuta(String id, String nombre, int fechaNacimiento,
                      String login, String contrasena, String codigoDescuento) {

        super(id, nombre, fechaNacimiento, login, contrasena, codigoDescuento);
    }
}
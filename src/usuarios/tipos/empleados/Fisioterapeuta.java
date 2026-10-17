package usuarios.tipos.empleados;

import java.time.LocalDate;
import usuarios.tipos.Empleado;

public class Fisioterapeuta extends Empleado {
    public Fisioterapeuta(String id, String nombre, LocalDate fechaNacimiento, String login, String contrasena,
            String codigoDescuento) {
        super(id, nombre, fechaNacimiento, login, contrasena, codigoDescuento);
    }
}
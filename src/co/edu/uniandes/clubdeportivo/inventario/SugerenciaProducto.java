package co.edu.uniandes.clubdeportivo.inventario;

import co.edu.uniandes.clubdeportivo.usuarios.Administrador;
import co.edu.uniandes.clubdeportivo.usuarios.Empleado;
import co.edu.uniandes.clubdeportivo.usuarios.EstadoSolicitud;

public class SugerenciaProducto {
    private String nombre;
    private String descripcion;
    private EstadoSolicitud estado;
    private Empleado empleado;
    private Administrador administrador;

    public SugerenciaProducto(String nombre, String descripcion, Empleado empleado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.empleado = empleado;
        this.estado = EstadoSolicitud.PENDIENTE;
        this.administrador = null;
    }

    public void aprobar(Administrador administrador) {
        this.administrador = administrador;
        this.estado = EstadoSolicitud.APROBADA;
    }

    public void rechazar(Administrador administrador) {
        this.administrador = administrador;
        this.estado = EstadoSolicitud.RECHAZADA;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Administrador getAdministrador() {
        return administrador;
    }
}

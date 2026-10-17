package tiendaclub.inventario;

import clubdeportivo.turnos.EstadoSolicitud;
import usuarios.tipos.Empleado;
import usuarios.tipos.empleados.Administrador;

public class SugerenciaProducto {
    private final String nombre;
    private final String descripcion;
    private final Empleado proponente;
    private EstadoSolicitud estado;
    private Administrador administrador;
    private Producto productoCreado;

    public SugerenciaProducto(String nombre, String descripcion, Empleado proponente) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del producto es obligatorio");
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La descripción es obligatoria");
        }

        if (proponente == null) {
            throw new IllegalArgumentException(
                    "El empleado proponente es obligatorio");
        }
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.proponente = proponente;
        this.estado = EstadoSolicitud.PENDIENTE;
        this.administrador = null;
        this.productoCreado = null;
    }

    public void aprobar(
            Administrador administrador,
            Producto productoCreado) {

        if (!estaPendiente()) {
            throw new IllegalStateException(
                    "La sugerencia ya fue decidida");
        }

        if (administrador == null) {
            throw new IllegalArgumentException(
                    "El administrador es obligatorio");
        }

        if (productoCreado == null) {
            throw new IllegalArgumentException(
                    "El producto creado es obligatorio");
        }

        this.administrador = administrador;
        this.productoCreado = productoCreado;
        this.estado = EstadoSolicitud.APROBADA;
    }

    public void rechazar(
            Administrador administrador) {

        if (!estaPendiente()) {
            throw new IllegalStateException(
                    "La sugerencia ya fue decidida");
        }

        if (administrador == null) {
            throw new IllegalArgumentException(
                    "El administrador es obligatorio");
        }

        this.administrador = administrador;
        this.productoCreado = null;
        this.estado = EstadoSolicitud.RECHAZADA;
    }

    public boolean estaPendiente() {
        return estado == EstadoSolicitud.PENDIENTE;
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

    public Empleado getProponente() {
        return proponente;
    }

    /*
     * Se conserva temporalmente para no romper
     * referencias del código anterior.
     */
    public Empleado getEmpleado() {
        return proponente;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public Producto getProductoCreado() {
        return productoCreado;
    }

    @Override
    public String toString() {
        return nombre
                + " - " + estado
                + " - propuesto por: "
                + proponente.getNombre();
    }
}
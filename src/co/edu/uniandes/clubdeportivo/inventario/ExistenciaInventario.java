package co.edu.uniandes.clubdeportivo.inventario;

public class ExistenciaInventario {
    private Producto producto;
    private UbicacionInventario ubicacion;
    private int cantidad;

    public ExistenciaInventario(Producto producto, UbicacionInventario ubicacion, int cantidadInicial) {
        if (cantidadInicial < 0) {
            throw new IllegalArgumentException(
                    "La cantidad inicial no puede ser negativa");
        }

        this.producto = producto;
        this.ubicacion = ubicacion;
        this.cantidad = cantidadInicial;
    }

    public Producto getProducto() {
        return producto;
    }

    public UbicacionInventario getUbicacion() {
        return ubicacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void agregarUnidades(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException(
                    "Las unidades deben ser mayores que cero");
        }

        cantidad += unidades;
    }

    public void retirarUnidades(int unidades) {
        if (unidades <= 0) {
            throw new IllegalArgumentException(
                    "Las unidades deben ser mayores que cero");
        }

        if (unidades > cantidad) {
            throw new IllegalArgumentException(
                    "No hay suficientes unidades disponibles");
        }

        cantidad -= unidades;
    }

    @Override
    public String toString() {
        return producto.getNombre() + " en " + ubicacion.getNombre()
                + ": " + cantidad;
    }
}

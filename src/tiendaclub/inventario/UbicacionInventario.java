package tiendaclub.inventario;

public class UbicacionInventario {
    private String nombre;

    public UbicacionInventario(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

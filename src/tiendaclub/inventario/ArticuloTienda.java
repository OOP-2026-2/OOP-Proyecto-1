package tiendaclub.inventario;

public class ArticuloTienda extends Producto {
    private final String categoria;

    public ArticuloTienda(String nombre, double precio, String categoria) {
        super(nombre, precio);
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La categoría del artículo es obligatoria");
        }

        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return super.toString()
                + " - categoría: " + categoria;
    }
}

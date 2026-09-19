package co.edu.uniandes.clubdeportivo.inventario;

public class ArticuloTienda extends Producto {
    private String categoria;

    public ArticuloTienda(String nombre, double precio, String categoria) {
        super(nombre, precio);
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }
}

package tiendaclub.inventario;

import java.util.ArrayList;

public class Snack extends Producto {
    private ArrayList<String> alergenos;

    public Snack(String nombre, double precio, ArrayList<String> alergenos) {
        super(nombre, precio);
        this.alergenos = new ArrayList<String>(alergenos);
    }

    public ArrayList<String> getAlergenos() {
        return new ArrayList<String>(alergenos);
    }

    public boolean contieneAlergeno(String alergeno) {
        return alergenos.contains(alergeno);
    }
}

package tiendaclub.inventario;

import java.util.ArrayList;

public class Snack extends Producto {
    private final ArrayList<String> alergenos;

    public Snack(String nombre, double precio, ArrayList<String> alergenos) {
        super(nombre, precio);
        this.alergenos = new ArrayList<String>();
        if (alergenos != null) {
            for (String alergeno : alergenos) {
                if (alergeno != null
                        && !alergeno.trim().isEmpty()) {
                    this.alergenos.add(
                            alergeno.trim());
                }
            }
        }
    }

    public boolean contieneAlergeno(
            String alergenoBuscado) {

        if (alergenoBuscado == null || alergenoBuscado.trim().isEmpty()) {
            return false;
        }

        for (String alergeno : alergenos) {

            if (alergeno.equalsIgnoreCase(alergenoBuscado.trim())) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<String> getAlergenos() {
        return new ArrayList<String>(alergenos);
    }

    @Override
    public String toString() {

        if (alergenos.isEmpty()) {
            return super.toString()
                    + " - sin alérgenos registrados";
        }

        return super.toString()
                + " - alérgenos: " + alergenos;
    }
}

package clubdeportivo.deportes;

public class Posicion {
    private String nombre;

    public Posicion(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre de la posición es obligatorio");
        }

        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object objeto) {

        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Posicion)) {
            return false;
        }

        Posicion otra = (Posicion) objeto;

        return nombre.equalsIgnoreCase(
                otra.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return nombre;
    }
}
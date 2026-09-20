package clubdeportivo.deportes;

public class Equipo {
    private String nombre;
    private Categoria categoria;
    private int cupoMaximo;

    public Equipo(String nombre, Categoria categoria, int cupoMaximo) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.cupoMaximo = cupoMaximo;
    }

    public String getNombre() {
        return nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public boolean tieneCupo(int cantidadInscritos) {
        return cantidadInscritos < cupoMaximo;
    }

    @Override
    public String toString() {
        return nombre + " - " + categoria;
    }
}

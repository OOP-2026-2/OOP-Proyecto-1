package co.edu.uniandes.clubdeportivo.deportes;

public class DeporteIndividual {
    private String nombre;
    private int maximoInstalaciones;

    public DeporteIndividual(String nombre, int maximoInstalaciones) {
        this.nombre = nombre;
        this.maximoInstalaciones = maximoInstalaciones;
    }

    public String getNombre() {
        return nombre;
    }

    public int getMaximoInstalaciones() {
        return maximoInstalaciones;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

package clubdeportivo.instalaciones;

public abstract class Instalacion {
    private String id;
    private int capacidadMaxima;
    private boolean techada;

    public Instalacion(String id, int capacidadMaxima, boolean techada) {
        this.id = id;
        this.capacidadMaxima = capacidadMaxima;
        this.techada = techada;
    }

    public String getId() {
        return id;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public boolean isTechada() {
        return techada;
    }

    @Override
    public String toString() {
        return id + " - capacidad: " + capacidadMaxima;
    }
}

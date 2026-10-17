package clubdeportivo.deportes;

import java.util.ArrayList;

public class DeporteIndividual {
    private String nombre;
    private int maximoInstalaciones;
    private ArrayList<InscripcionAcademia> inscripciones;

    public DeporteIndividual(String nombre, int maximoInstalaciones) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del deporte es obligatorio");
        }

        if (maximoInstalaciones <= 0) {
            throw new IllegalArgumentException(
                    "El máximo de inscripciones debe ser "
                            + "mayor que cero");
        }

        this.nombre = nombre;
        this.maximoInstalaciones = maximoInstalaciones;
        this.inscripciones = new ArrayList<InscripcionAcademia>();
    }

    public void agregarInscripcion(
            InscripcionAcademia inscripcion) {

        if (inscripcion == null) {
            throw new IllegalArgumentException(
                    "La inscripción no puede ser nula");
        }

        if (inscripcion.getDeporte() != this) {
            throw new IllegalArgumentException(
                    "La inscripción pertenece a otro deporte");
        }

        if (inscripciones.contains(inscripcion)) {
            throw new IllegalArgumentException(
                    "La inscripción ya está registrada");
        }

        if (!permiteNuevaInscripcion()) {
            throw new IllegalStateException(
                    "El deporte alcanzó el máximo "
                            + "de inscripciones activas");
        }

        inscripciones.add(inscripcion);
    }

    public boolean permiteNuevaInscripcion() {

        int cantidadActivas = 0;

        for (InscripcionAcademia inscripcion : inscripciones) {
            if (inscripcion.estaActiva()) {
                cantidadActivas++;
            }
        }

        return cantidadActivas < maximoInstalaciones;
    }

    public String getNombre() {
        return nombre;
    }

    public int getMaximoInstalaciones() {
        return maximoInstalaciones;
    }

    public ArrayList<InscripcionAcademia> getInscripciones() {

        return new ArrayList<InscripcionAcademia>(
                inscripciones);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
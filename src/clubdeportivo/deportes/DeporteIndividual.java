package clubdeportivo.deportes;

import java.util.ArrayList;

public class DeporteIndividual {
    private String nombre;
    private int maximoInscripciones;
    private ArrayList<InscripcionAcademia> inscripciones;

    public DeporteIndividual(String nombre, int maximoInscripciones) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del deporte es obligatorio");
        }

        if (maximoInscripciones <= 0) {
            throw new IllegalArgumentException(
                    "El máximo de inscripciones debe ser mayor que cero");
        }

        this.nombre = nombre;
        this.maximoInscripciones = maximoInscripciones;
        this.inscripciones = new ArrayList<InscripcionAcademia>();
    }

    public void agregarInscripcion(InscripcionAcademia inscripcion) {
        if (inscripcion == null) {
            throw new IllegalArgumentException(
                    "La inscripción no puede ser nula");
        }

        if (inscripcion.getDeporte() != this) {
            throw new IllegalArgumentException(
                    "La inscripción pertenece a otro deporte");
        }

        for (InscripcionAcademia existente : inscripciones) {
            boolean mismoSocio = existente.getSocio() == inscripcion.getSocio();

            if (mismoSocio && existente.estaActiva()) {
                throw new IllegalArgumentException(
                        "El socio ya tiene una inscripción activa en este deporte");
            }
        }

        if (!permiteNuevaInscripcion()) {
            throw new IllegalStateException(
                    "El deporte alcanzó el máximo de inscripciones activas");
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

        return cantidadActivas < maximoInscripciones;
    }

    public String getNombre() {
        return nombre;
    }

    public int getMaximoInscripciones() {
        return maximoInscripciones;
    }

    public ArrayList<InscripcionAcademia> getInscripciones() {
        return new ArrayList<InscripcionAcademia>(inscripciones);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
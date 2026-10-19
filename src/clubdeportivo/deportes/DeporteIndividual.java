package clubdeportivo.deportes;

import java.util.ArrayList;
import java.util.List;
import clubdeportivo.instalaciones.Instalacion;

public class DeporteIndividual {
    private String nombre;
    private int maximoInstalaciones;
    private List<Instalacion> instalaciones;
    private List<InscripcionAcademia> inscripciones;

    public DeporteIndividual(String nombre, int maximoInstalaciones) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del deporte es obligatorio");
        }

        if (maximoInstalaciones <= 0) {
            throw new IllegalArgumentException(
                    "El máximo de instalaciones debe ser mayor que cero");
        }

        this.nombre = nombre;
        this.maximoInstalaciones = maximoInstalaciones;
        this.instalaciones = new ArrayList<Instalacion>();
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

        inscripciones.add(inscripcion);
    }

    public void agregarInstalacion(Instalacion instalacion) {
        if (instalacion == null) {
            throw new IllegalArgumentException(
                    "La instalación no puede ser nula");
        }

        if (!instalaciones.contains(instalacion)) {
            instalaciones.add(instalacion);
        }
    }

    public boolean permiteNuevaReserva(int reservasActivas) {
        return reservasActivas < maximoInstalaciones;
    }

    public String getNombre() {
        return nombre;
    }

    public int getMaximoInstalaciones() {
        return maximoInstalaciones;
    }

    public List<Instalacion> getInstalaciones() {
        return new ArrayList<Instalacion>(instalaciones);
    }

    public List<InscripcionAcademia> getInscripciones() {
        return new ArrayList<InscripcionAcademia>(inscripciones);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
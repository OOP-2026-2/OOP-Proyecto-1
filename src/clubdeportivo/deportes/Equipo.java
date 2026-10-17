package clubdeportivo.deportes;

import java.util.ArrayList;
import usuarios.tipos.Socio;

public class Equipo {
    private String nombre;
    private Categoria categoria;
    private int cupoMaximo;
    private boolean activo;
    private DeporteConjunto deporte;
    private ArrayList<InscripcionEquipo> inscripciones;

    public Equipo(String nombre, Categoria categoria, int cupoMaximo, DeporteConjunto deporte) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del equipo es obligatorio");
        }

        if (categoria == null) {
            throw new IllegalArgumentException(
                    "La categoría es obligatoria");
        }

        if (cupoMaximo <= 0) {
            throw new IllegalArgumentException(
                    "El cupo máximo debe ser mayor que cero");
        }

        if (deporte == null) {
            throw new IllegalArgumentException(
                    "El deporte es obligatorio");
        }

        this.nombre = nombre;
        this.categoria = categoria;
        this.cupoMaximo = cupoMaximo;
        this.activo = true;
        this.deporte = deporte;
        this.inscripciones = new ArrayList<InscripcionEquipo>();
    }

    public void agregarInscripcion(
            InscripcionEquipo inscripcion) {

        if (inscripcion == null) {
            throw new IllegalArgumentException(
                    "La inscripción no puede ser nula");
        }

        if (inscripcion.getEquipo() != this) {
            throw new IllegalArgumentException(
                    "La inscripción pertenece a otro equipo");
        }

        if (buscarInscripcion(
                inscripcion.getSocio()) != null) {

            throw new IllegalArgumentException(
                    "El socio ya está inscrito en el equipo");
        }

        if (!tieneCupo()) {
            throw new IllegalStateException(
                    "El equipo no tiene cupos disponibles");
        }

        inscripciones.add(inscripcion);
    }

    public boolean tieneCupo() {

        int cantidadActivas = 0;

        for (InscripcionEquipo inscripcion : inscripciones) {

            if (inscripcion.estaActiva()) {
                cantidadActivas++;
            }
        }

        return cantidadActivas < cupoMaximo;
    }

    public InscripcionEquipo buscarInscripcion(
            Socio socio) {

        if (socio == null) {
            return null;
        }

        for (InscripcionEquipo inscripcion : inscripciones) {

            if (inscripcion.getSocio() == socio) {
                return inscripcion;
            }
        }

        return null;
    }

    public ArrayList<Socio> getJugadoresHabilitados() {

        ArrayList<Socio> jugadoresHabilitados = new ArrayList<Socio>();

        for (InscripcionEquipo inscripcion : inscripciones) {

            if (inscripcion.estaActiva()) {
                jugadoresHabilitados.add(
                        inscripcion.getSocio());
            }
        }

        return jugadoresHabilitados;
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

    public boolean isActivo() {
        return activo;
    }

    public DeporteConjunto getDeporte() {
        return deporte;
    }

    public ArrayList<InscripcionEquipo> getInscripciones() {

        return new ArrayList<InscripcionEquipo>(
                inscripciones);
    }

    public void activar() {
        activo = true;
    }

    public void desactivar() {
        activo = false;
    }

    @Override
    public String toString() {
        return nombre + " - " + categoria
                + " - " + deporte.getNombre();
    }
}
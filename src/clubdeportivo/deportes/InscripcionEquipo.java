package clubdeportivo.deportes;

import java.time.LocalDate;
import usuarios.tipos.Socio;

public class InscripcionEquipo {
    private Socio socio;
    private Equipo equipo;
    private Posicion posicion;
    private int numeroCamiseta;
    private LocalDate fechaInscripcion;
    private EstadoInscripcion estado;

    public InscripcionEquipo(Socio socio, Equipo equipo, Posicion posicion, int numeroCamiseta) {
        if (socio == null) {
            throw new IllegalArgumentException(
                    "El socio es obligatorio");
        }

        if (equipo == null) {
            throw new IllegalArgumentException(
                    "El equipo es obligatorio");
        }

        if (posicion == null) {
            throw new IllegalArgumentException(
                    "La posición es obligatoria");
        }

        if (numeroCamiseta <= 0) {
            throw new IllegalArgumentException(
                    "El número de camiseta debe ser "
                            + "mayor que cero");
        }

        if (!equipo.getDeporte()
                .admitePosicion(posicion)) {

            throw new IllegalArgumentException(
                    "La posición no corresponde al deporte");
        }

        for (InscripcionEquipo existente : equipo.getInscripciones()) {

            boolean mismoNumero = existente.getNumeroCamiseta() == numeroCamiseta;

            if (mismoNumero
                    && existente.estaActiva()) {

                throw new IllegalArgumentException(
                        "El número de camiseta ya está "
                                + "asignado en el equipo");
            }
        }

        this.socio = socio;
        this.equipo = equipo;
        this.posicion = posicion;
        this.numeroCamiseta = numeroCamiseta;
        this.fechaInscripcion = LocalDate.now();
        this.estado = EstadoInscripcion.ACTIVA;
    }

    public boolean estaActiva() {
        return estado == EstadoInscripcion.ACTIVA;
    }

    public void suspender() {

        if (estado == EstadoInscripcion.RETIRADA) {
            throw new IllegalStateException(
                    "No se puede suspender una inscripción retirada");
        }

        estado = EstadoInscripcion.SUSPENDIDA;
    }

    public void reactivar() {

        if (estado == EstadoInscripcion.RETIRADA) {
            throw new IllegalStateException(
                    "No se puede reactivar una inscripción retirada");
        }

        estado = EstadoInscripcion.ACTIVA;
    }

    public void retirar() {
        estado = EstadoInscripcion.RETIRADA;
    }

    public Socio getSocio() {
        return socio;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public int getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public EstadoInscripcion getEstado() {
        return estado;
    }

    @Override
    public String toString() {

        return socio.getNombre()
                + " - "
                + equipo.getNombre()
                + " - camiseta "
                + numeroCamiseta
                + " - "
                + estado;
    }
}
package clubdeportivo.instalaciones;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import clubdeportivo.deportes.DeporteIndividual;

public class Instalacion {
    private final String id;
    private final TipoInstalacion tipo;
    private final int capacidadMaxima;
    private final boolean techada;
    private final DeporteIndividual disciplina;
    private final List<Reserva> reservas;

    public Instalacion(String id, TipoInstalacion tipo, int capacidadMaxima, boolean techada) {
        this(id, tipo, capacidadMaxima, techada, null);
    }

    public Instalacion(String id, TipoInstalacion tipo, int capacidadMaxima, boolean techada,
            DeporteIndividual disciplina) {

        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El ID de la instalación es obligatorio");
        }

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "El tipo de instalación es obligatorio");
        }

        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad máxima debe ser mayor que cero");
        }

        this.id = id;
        this.tipo = tipo;
        this.capacidadMaxima = capacidadMaxima;
        this.techada = techada;
        this.disciplina = disciplina;
        this.reservas = new ArrayList<Reserva>();
    }

    public boolean admiteJugadores(int cantidad) {
        return cantidad > 0 && cantidad <= capacidadMaxima;
    }

    public boolean estaDisponible(
            LocalDate fecha,
            LocalTime horaInicio,
            int duracionMinutos) {

        if (fecha == null || horaInicio == null || duracionMinutos <= 0) {
            return false;
        }

        LocalTime horaFin = horaInicio.plusMinutes(duracionMinutos);

        for (Reserva reserva : reservas) {

            if (!reserva.getFecha().equals(fecha)) {
                continue;
            }

            boolean seCruzan = horaInicio.isBefore(reserva.calcularHoraFin())
                    && reserva.getHoraInicio().isBefore(horaFin);

            if (seCruzan) {
                return false;
            }
        }

        return true;
    }

    public void agregarReserva(Reserva reserva) {

        if (reserva == null) {
            throw new IllegalArgumentException(
                    "La reserva no puede ser nula");
        }

        if (reserva.getInstalacion() != this) {
            throw new IllegalArgumentException(
                    "La reserva corresponde a otra instalación");
        }

        if (!admiteJugadores(reserva.getNumeroJugadores())) {
            throw new IllegalArgumentException(
                    "La cantidad de jugadores supera la capacidad");
        }

        if (!estaDisponible(
                reserva.getFecha(),
                reserva.getHoraInicio(),
                reserva.getDuracionMinutos())) {

            throw new IllegalArgumentException(
                    "La instalación no está disponible en ese horario");
        }

        if (!reservas.contains(reserva)) {
            reservas.add(reserva);
        }
    }

    public String getId() {
        return id;
    }

    public TipoInstalacion getTipo() {
        return tipo;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public boolean isTechada() {
        return techada;
    }

    public DeporteIndividual getDisciplina() {
        return disciplina;
    }

    public List<Reserva> getReservas() {
        return new ArrayList<Reserva>(reservas);
    }

    @Override
    public String toString() {
        return id
                + " - " + tipo
                + " - capacidad: " + capacidadMaxima
                + (techada ? " - techada" : " - no techada");
    }
}

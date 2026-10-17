package clubdeportivo.turnos;

import java.time.LocalTime;

public class Turno {
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public Turno(DiaSemana diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        if (diaSemana == null) {
            throw new IllegalArgumentException(
                    "El día de la semana es obligatorio");
        }
        if (horaInicio == null || horaFin == null) {
            throw new IllegalArgumentException(
                    "Las horas del turno son obligatorias");
        }
        if (!horaFin.isAfter(horaInicio)) {
            throw new IllegalArgumentException(
                    "La hora final debe ser posterior a la inicial");
        }
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public boolean seCruzaCon(Turno otro) {
        if (otro == null) {
            return false;
        }
        if (diaSemana != otro.diaSemana) {
            return false;
        }
        return horaInicio.isBefore(otro.horaFin)
                && otro.horaInicio.isBefore(horaFin);
    }

    public boolean contieneHorario(
            LocalTime inicio,
            LocalTime fin) {
        if (inicio == null || fin == null) {
            return false;
        }
        if (!fin.isAfter(inicio)) {
            return false;
        }

        boolean iniciaDentro = !inicio.isBefore(horaInicio);

        boolean terminaDentro = !fin.isAfter(horaFin);

        return iniciaDentro && terminaDentro;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }
}
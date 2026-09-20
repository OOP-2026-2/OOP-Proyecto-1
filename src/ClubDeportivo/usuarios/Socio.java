package ClubDeportivo.usuarios;

import java.time.LocalDate;

public class Socio extends Usuario {
    private LocalDate fechaNacimiento;
    private double puntosFidelidad;

    public Socio(String id, String nombre, String login, String contrasena, LocalDate fechaNacimiento) {
        super(id, nombre, login, contrasena);
        this.fechaNacimiento = fechaNacimiento;
        this.puntosFidelidad = 0;
    }

    public Socio(String id, String nombre, LocalDate fechaNacimiento) {
        this(id, nombre, id, id, fechaNacimiento);
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public double getPuntosFidelidad() {
        return puntosFidelidad;
    }

    public void agregarPuntos(double puntos) {
        if (puntos > 0) {
            puntosFidelidad += puntos;
        }
    }

    public boolean redimirPuntos(double puntos) {
        if (puntos <= 0 || puntos > puntosFidelidad) {
            return false;
        }

        puntosFidelidad -= puntos;
        return true;
    }

    public void setPuntosFidelidad(double puntosFidelidad) {
        if (puntosFidelidad >= 0) {
            this.puntosFidelidad = puntosFidelidad;
        }
    }
}

package co.edu.uniandes.clubdeportivo.usuarios;

import java.time.LocalDate;

public class Socio extends Usuario {
    private LocalDate fechaNacimiento;
    private double puntosFidelidad;

    public Socio(String id, String nombre, LocalDate fechaNacimiento) {
        super(id, nombre);
        this.fechaNacimiento = fechaNacimiento;
        this.puntosFidelidad = 0;
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
}

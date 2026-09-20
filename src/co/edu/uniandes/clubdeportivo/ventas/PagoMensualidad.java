package co.edu.uniandes.clubdeportivo.ventas;

import java.time.LocalDate;
import co.edu.uniandes.clubdeportivo.usuarios.Socio;

public class PagoMensualidad {
    private LocalDate fecha;
    private String periodo;
    private double valor;
    private Socio socio;

    public PagoMensualidad(LocalDate fecha, String periodo, double valor, Socio socio) {
        if (fecha == null || socio == null) {
            throw new IllegalArgumentException("La fecha y el socio son obligatorios");
        }
        if (periodo == null || periodo.trim().isEmpty()) {
            throw new IllegalArgumentException("El periodo es obligatorio");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("El valor debe ser mayor que cero");
        }
        this.fecha = fecha;
        this.periodo = periodo;
        this.valor = valor;
        this.socio = socio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getPeriodo() {
        return periodo;
    }

    public double getValor() {
        return valor;
    }

    public Socio getSocio() {
        return socio;
    }

    public boolean correspondeAlPeriodo(String periodo) {
        return this.periodo.equalsIgnoreCase(periodo);
    }
}
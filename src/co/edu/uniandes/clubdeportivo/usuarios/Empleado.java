package co.edu.uniandes.clubdeportivo.usuarios;

public abstract class Empleado extends Usuario {
    private String codigoDescuento;
    private double puntosFidelidad;

    public Empleado(String id, String nombre, String codigoDescuento) {
        super(id, nombre);
        this.codigoDescuento = codigoDescuento;
        this.puntosFidelidad = 0;
    }

    public String getCodigoDescuento() {
        return codigoDescuento;
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

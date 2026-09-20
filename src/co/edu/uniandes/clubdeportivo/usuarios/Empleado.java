package co.edu.uniandes.clubdeportivo.usuarios;

public abstract class Empleado extends Usuario {
    private String codigoDescuento;
    private double puntosFidelidad;

    public Empleado(String id, String nombre, String login, String contrasena, String codigoDescuento) {
        super(id, nombre, login, contrasena);
        this.codigoDescuento = codigoDescuento;
        this.puntosFidelidad = 0;
    }

    public Empleado(String id, String nombre, String codigoDescuento) {
        this(id, nombre, id, id, codigoDescuento);
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
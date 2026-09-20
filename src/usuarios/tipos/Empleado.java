package usuarios.tipos;

import usuarios.Usuario;

public abstract class Empleado extends Usuario {
    private final String codigoDescuento;
    private double puntosFidelidad;

    public Empleado(String id, String nombre, int fechaNacimiento,
                    String login, String contrasena, String codigoDescuento) {

        super(id, nombre, fechaNacimiento, login, contrasena);
        this.codigoDescuento = codigoDescuento;
        this.puntosFidelidad = 0;
    }

    //Métodos
    public boolean redimirPuntos(double puntos) {
        if (puntos <= 0 || puntos > puntosFidelidad) {
            return false;
        }

        puntosFidelidad -= puntos;
        return true;
    }

    public void agregarPuntos(double puntos) {
        if (puntos > 0) {
            puntosFidelidad += puntos;
        }
    }

    //Getters
    public String getCodigoDescuento() {return codigoDescuento;}
    public double getPuntosFidelidad() {return puntosFidelidad;}

    //Setters
    public void setPuntosFidelidad(double puntosFidelidad) {
        if (puntosFidelidad >= 0) {
            this.puntosFidelidad = puntosFidelidad;
        }
    }
}
package usuarios.tipos;

import usuarios.Usuario;

public class Socio extends Usuario {
    private double puntosFidelidad;

    public Socio(String id, String nombre, int fechaNacimiento, String login, String password) {
        super(id, nombre, fechaNacimiento, login, password);
        this.puntosFidelidad = 0;
    }

    //Métodos
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

    //Getters
    public double getPuntosFidelidad() {return puntosFidelidad;}

    //Setters
    public void setPuntosFidelidad(double puntosFidelidad) {
        if (puntosFidelidad >= 0) {
            this.puntosFidelidad = puntosFidelidad;
        }
    }
}

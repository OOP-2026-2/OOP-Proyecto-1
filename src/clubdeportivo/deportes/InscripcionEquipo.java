package clubdeportivo.deportes;

import usuarios.tipos.Socio;

public class InscripcionEquipo {
    private Socio socio;
    private Equipo equipo;
    private String posicion;
    private int numeroCamiseta;
    private boolean habilitado;

    public InscripcionEquipo(Socio socio, Equipo equipo, String posicion, int numeroCamiseta) {
        this.socio = socio;
        this.equipo = equipo;
        this.posicion = posicion;
        this.numeroCamiseta = numeroCamiseta;
        this.habilitado = true;
    }

    public Socio getSocio() {
        return socio;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public String getPosicion() {
        return posicion;
    }

    public int getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void deshabilitar() {
        habilitado = false;
    }

    public void habilitar() {
        habilitado = true;
    }

    @Override
    public String toString() {
        return socio.getNombre() + " - " + equipo.getNombre()
                + " - camiseta " + numeroCamiseta;
    }
}

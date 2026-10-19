package usuarios.tipos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import clubdeportivo.membresias.PagoMensualidad;
import usuarios.Comprador;
import usuarios.Usuario;

public class Socio extends Usuario implements Comprador {
    private double puntosFidelidad;
    private List<PagoMensualidad> pagos;

    public Socio(String id, String nombre, LocalDate fechaNacimiento, String login, String contrasena) {
        super(id, nombre, fechaNacimiento, login, contrasena);
        this.puntosFidelidad = 0;
        this.pagos = new ArrayList<PagoMensualidad>();
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

    public void agregarPago(PagoMensualidad pago) {
        if (pago != null && !pagos.contains(pago)) {
            pagos.add(pago);
        }
    }

    @Override
    public double getPorcentajeDescuento() {
        return 0;
    }

    public double getPuntosFidelidad() {
        return puntosFidelidad;
    }

    public List<PagoMensualidad> getPagos() {
        return new ArrayList<PagoMensualidad>(pagos);
    }
}
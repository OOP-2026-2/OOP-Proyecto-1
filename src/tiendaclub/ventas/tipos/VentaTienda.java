package tiendaclub.ventas.tipos;

import java.time.LocalDate;
import tiendaclub.ventas.Venta;
import tiendaclub.ventas.descuentos.DescuentoCodigoCompartido;
import tiendaclub.ventas.descuentos.DescuentoEmpleado;
import tiendaclub.ventas.descuentos.PoliticaDescuento;
import tiendaclub.ventas.descuentos.SinDescuento;
import usuarios.Comprador;

public class VentaTienda extends Venta {
    private static final double IVA = 0.19;

    public VentaTienda(LocalDate fecha, Comprador comprador) {
        super(fecha, comprador);
    }

    public VentaTienda(
            LocalDate fecha,
            Comprador comprador,
            PoliticaDescuento politicaDescuento) {

        super(
                fecha,
                comprador,
                politicaDescuento);
    }

    @Override
    public double calcularImpuesto() {
        return calcularBaseConDescuento() * IVA;
    }

    public void finalizarVenta() {
        calcularTotal();
    }

    /*
     * Método temporal para mantener compatibilidad
     * con la consola anterior.
     */
    public void finalizarVenta(
            boolean usaCodigoCompartido) {

        if (usaCodigoCompartido) {

            cambiarPoliticaDescuento(
                    new DescuentoCodigoCompartido());

        } else if (getComprador()
                .getPorcentajeDescuento() > 0) {

            cambiarPoliticaDescuento(
                    new DescuentoEmpleado());

        } else {

            cambiarPoliticaDescuento(
                    new SinDescuento());
        }

        calcularTotal();
    }
}
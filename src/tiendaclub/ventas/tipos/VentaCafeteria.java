package tiendaclub.ventas.tipos;

import java.time.LocalDate;

import tiendaclub.ventas.Venta;
import tiendaclub.ventas.descuentos.DescuentoCodigoCompartido;
import tiendaclub.ventas.descuentos.DescuentoEmpleado;
import tiendaclub.ventas.descuentos.PoliticaDescuento;
import tiendaclub.ventas.descuentos.SinDescuento;
import usuarios.Comprador;

public class VentaCafeteria extends Venta {
    private static final double IMPUESTO_CONSUMO = 0.08;
    private static final double PROPINA_SUGERIDA = 0.10;
    private boolean incluirPropina;
    private double propina;

    public VentaCafeteria(LocalDate fecha, Comprador comprador) {
        super(fecha, comprador);
        this.incluirPropina = false;
        this.propina = 0;
    }

    public VentaCafeteria(LocalDate fecha, Comprador comprador, PoliticaDescuento politicaDescuento) {
        super(fecha, comprador, politicaDescuento);
        this.incluirPropina = false;
        this.propina = 0;
    }

    @Override
    public double calcularImpuesto() {
        return calcularBaseConDescuento()
                * IMPUESTO_CONSUMO;
    }

    public double calcularPropina() {
        return calcularBaseConDescuento()
                * PROPINA_SUGERIDA;
    }

    @Override
    public double calcularTotal() {

        double totalSinPropina = super.calcularTotal();

        if (incluirPropina) {
            propina = calcularPropina();
        } else {
            propina = 0;
        }

        establecerTotal(
                totalSinPropina + propina);

        return getTotal();
    }

    public void finalizarVenta(
            boolean incluirPropina) {

        this.incluirPropina = incluirPropina;
        calcularTotal();
    }

    /*
     * Método temporal para mantener compatibilidad
     * con la consola anterior.
     */
    public void finalizarVenta(
            boolean usaCodigoCompartido,
            boolean incluirPropina) {

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

        this.incluirPropina = incluirPropina;
        calcularTotal();
    }

    public boolean isIncluirPropina() {
        return incluirPropina;
    }

    public double getPropina() {
        return propina;
    }
}
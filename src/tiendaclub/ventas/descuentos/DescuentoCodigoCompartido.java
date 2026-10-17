package tiendaclub.ventas.descuentos;

public class DescuentoCodigoCompartido
        implements PoliticaDescuento {

    private static final double PORCENTAJE = 0.08;

    @Override
    public double calcularDescuento(
            double subtotal) {

        return subtotal * PORCENTAJE;
    }
}
package tiendaclub.ventas.descuentos;

public class DescuentoEmpleado
        implements PoliticaDescuento {

    private static final double PORCENTAJE = 0.15;

    @Override
    public double calcularDescuento(
            double subtotal) {

        return subtotal * PORCENTAJE;
    }
}
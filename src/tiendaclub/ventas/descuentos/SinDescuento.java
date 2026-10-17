package tiendaclub.ventas.descuentos;

public class SinDescuento
        implements PoliticaDescuento {

    @Override
    public double calcularDescuento(
            double subtotal) {

        return 0;
    }
}
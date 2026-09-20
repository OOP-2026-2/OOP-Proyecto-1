package tiendaclub.ventas;

import java.time.LocalDate;
import usuarios.Usuario;

public class VentaTienda extends Venta {
    private static final double IVA = 0.19;

    public VentaTienda(LocalDate fecha, Usuario comprador) {
        super(fecha, comprador);
    }

    public void finalizarVenta(boolean usaCodigoCompartido) {
        double porcentajeDescuento = determinarPorcentajeDescuento(usaCodigoCompartido);

        calcularTotales(porcentajeDescuento, IVA);
    }
}

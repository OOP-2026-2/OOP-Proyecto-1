package TiendaClub.ventas;

import java.time.LocalDate;

import ClubDeportivo.usuarios.Usuario;

public class VentaCafeteria extends Venta {
    private static final double IMPUESTO_CONSUMO = 0.08;
    private static final double PROPINA_SUGERIDA = 0.10;

    private double propina;

    public VentaCafeteria(LocalDate fecha, Usuario comprador) {
        super(fecha, comprador);
    }

    public void finalizarVenta(
            boolean usaCodigoCompartido,
            boolean incluirPropina) {
        double porcentajeDescuento = determinarPorcentajeDescuento(usaCodigoCompartido);

        calcularTotales(porcentajeDescuento, IMPUESTO_CONSUMO);

        if (incluirPropina) {
            double basePropina = getSubtotal() - getDescuento();
            propina = basePropina * PROPINA_SUGERIDA;
            agregarAlTotal(propina);
        }
    }

    public double getPropina() {
        return propina;
    }
}

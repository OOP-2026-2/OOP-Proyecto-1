package co.edu.uniandes.clubdeportivo.consola;

import java.time.LocalDate;
import co.edu.uniandes.clubdeportivo.inventario.ArticuloTienda;
import co.edu.uniandes.clubdeportivo.usuarios.Socio;
import co.edu.uniandes.clubdeportivo.ventas.DetalleVenta;
import co.edu.uniandes.clubdeportivo.ventas.VentaTienda;

public class Main {
    public static void main(String[] args) {
        Socio socio = new Socio(
                "S001",
                "Samuel",
                LocalDate.of(2007, 5, 10));

        ArticuloTienda camiseta = new ArticuloTienda(
                "Camiseta del club",
                10000,
                "Ropa");

        DetalleVenta detalle = new DetalleVenta(camiseta, 2);

        VentaTienda venta = new VentaTienda(
                LocalDate.now(),
                socio);

        venta.agregarDetalle(detalle);
        venta.finalizarVenta(true);

        socio.agregarPuntos(venta.getPuntosGenerados());

        System.out.println("Subtotal: " + venta.getSubtotal());
        System.out.println("Descuento: " + venta.getDescuento());
        System.out.println("IVA: " + venta.getImpuesto());
        System.out.println("Total: " + venta.getTotal());
        System.out.println("Puntos: " + socio.getPuntosFidelidad());
    }
}

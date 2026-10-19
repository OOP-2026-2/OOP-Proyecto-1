package tiendaclub.ventas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import tiendaclub.inventario.Producto;
import tiendaclub.ventas.descuentos.PoliticaDescuento;
import tiendaclub.ventas.descuentos.SinDescuento;
import tiendaclub.ventas.detalles.DetalleVenta;
import usuarios.Comprador;

public abstract class Venta {

    private final LocalDate fecha;
    private final Comprador comprador;
    private final List<DetalleVenta> detalles;
    private PoliticaDescuento politicaDescuento;
    private double subtotal;
    private double descuento;
    private double impuesto;
    private double total;
    private double puntosGenerados;

    public Venta(LocalDate fecha, Comprador comprador) {
        this(fecha, comprador, new SinDescuento());
    }

    public Venta(LocalDate fecha, Comprador comprador, PoliticaDescuento politicaDescuento) {
        if (fecha == null) {
            throw new IllegalArgumentException(
                    "La fecha es obligatoria");
        }

        if (comprador == null) {
            throw new IllegalArgumentException(
                    "El comprador es obligatorio");
        }

        if (politicaDescuento == null) {
            throw new IllegalArgumentException(
                    "La política de descuento es obligatoria");
        }

        this.fecha = fecha;
        this.comprador = comprador;
        this.politicaDescuento = politicaDescuento;
        this.detalles = new ArrayList<DetalleVenta>();
    }

    public void agregarDetalle(
            Producto producto,
            int cantidad) {

        agregarDetalle(
                new DetalleVenta(producto, cantidad));
    }

    /*
     * Se conserva esta sobrecarga para mantener
     * compatibilidad con el código anterior.
     */
    public void agregarDetalle(
            DetalleVenta detalle) {

        if (detalle == null) {
            throw new IllegalArgumentException(
                    "El detalle no puede ser nulo");
        }

        detalles.add(detalle);
        recalcularTotales();
    }

    public double calcularSubtotal() {

        double resultado = 0;

        for (DetalleVenta detalle : detalles) {
            resultado += detalle.calcularSubtotal();
        }

        subtotal = resultado;
        return subtotal;
    }

    public double calcularDescuento() {

        descuento = politicaDescuento.calcularDescuento(
                calcularSubtotal());

        return descuento;
    }

    public abstract double calcularImpuesto();

    public double calcularTotal() {

        calcularSubtotal();
        calcularDescuento();
        impuesto = calcularImpuesto();
        total = subtotal - descuento + impuesto;
        calcularPuntos();

        return total;
    }

    public double calcularPuntos() {
        puntosGenerados = total * 0.02;
        return puntosGenerados;
    }

    protected void recalcularTotales() {
        calcularTotal();
    }

    protected double calcularBaseConDescuento() {
        return calcularSubtotal()
                - calcularDescuento();
    }

    protected void establecerImpuesto(
            double impuesto) {

        if (impuesto < 0) {
            throw new IllegalArgumentException(
                    "El impuesto no puede ser negativo");
        }

        this.impuesto = impuesto;
    }

    protected void establecerTotal(double total) {

        if (total < 0) {
            throw new IllegalArgumentException(
                    "El total no puede ser negativo");
        }

        this.total = total;
        calcularPuntos();
    }

    public void cambiarPoliticaDescuento(
            PoliticaDescuento politicaDescuento) {

        if (politicaDescuento == null) {
            throw new IllegalArgumentException(
                    "La política de descuento es obligatoria");
        }

        this.politicaDescuento = politicaDescuento;
        recalcularTotales();
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Comprador getComprador() {
        return comprador;
    }

    public List<DetalleVenta> getDetalles() {
        return new ArrayList<DetalleVenta>(detalles);
    }

    public PoliticaDescuento getPoliticaDescuento() {

        return politicaDescuento;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public double getTotal() {
        return total;
    }

    public double getPuntosGenerados() {
        return puntosGenerados;
    }
}
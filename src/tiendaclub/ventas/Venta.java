package tiendaclub.ventas;

import java.time.LocalDate;
import java.util.ArrayList;

import tiendaclub.ventas.detalles.DetalleVenta;
import tiendaclub.ventas.detalles.TipoDescuento;
import usuarios.tipos.Empleado;
import usuarios.tipos.Socio;
import usuarios.Usuario;

public abstract class Venta {
    private LocalDate fecha;
    private Usuario comprador;
    private ArrayList<DetalleVenta> detalles;
    private double subtotal;
    private double descuento;
    private double impuesto;
    private double total;
    private double puntosGenerados;
    private TipoDescuento tipoDescuento;

    public Venta(LocalDate fecha, Usuario comprador) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        if (!(comprador instanceof Socio) && !(comprador instanceof Empleado)) {
            throw new IllegalArgumentException("El comprador debe ser un socio o un empleado");
        }
        this.fecha = fecha;
        this.comprador = comprador;
        this.detalles = new ArrayList<DetalleVenta>();
        this.tipoDescuento = TipoDescuento.NINGUNO;
    }

    public void agregarDetalle(DetalleVenta detalle) {
        if (detalle != null) {
            detalles.add(detalle);
            subtotal += detalle.getSubtotal();
        }
    }

    protected void calcularTotales(double porcentajeDescuento, double porcentajeImpuesto) {
        descuento = subtotal * porcentajeDescuento;
        double baseConDescuento = subtotal - descuento;
        impuesto = baseConDescuento * porcentajeImpuesto;
        total = baseConDescuento + impuesto;
        puntosGenerados = total * 0.02;
    }

    protected double determinarPorcentajeDescuento(boolean usaCodigoCompartido) {
        if (comprador instanceof Empleado) {
            tipoDescuento = TipoDescuento.EMPLEADO;
            return 0.15;
        }
        if (usaCodigoCompartido) {
            tipoDescuento = TipoDescuento.CODIGO_COMPARTIDO;
            return 0.08;
        }
        tipoDescuento = TipoDescuento.NINGUNO;
        return 0;
    }

    protected void agregarAlTotal(double valor) {
        total += valor;
        puntosGenerados = total * 0.02;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public ArrayList<DetalleVenta> getDetalles() {
        return new ArrayList<DetalleVenta>(detalles);
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

    public TipoDescuento getTipoDescuento() {
        return tipoDescuento;
    }
}
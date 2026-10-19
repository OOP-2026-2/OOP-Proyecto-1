package consola.subconsolas;

import clubdeportivo.ClubDeportivo;
import consola.Subconsola;
import tiendaclub.inventario.*;
import tiendaclub.ventas.Venta;
import tiendaclub.ventas.tipos.VentaTienda;
import usuarios.Comprador;
import usuarios.Usuario;
import usuarios.tipos.Empleado;
import usuarios.tipos.empleados.Administrador;
import utilidades.excepciones.InventarioInsuficienteException;
import java.time.LocalDate;

import static consola.InputHandler.*;

public class ConsolaComercial extends Subconsola {
    public ConsolaComercial(ClubDeportivo club) {
        super(club);
    }

    @Override
    public String getTitulo() {
        return "Tienda e inventario";
    }

    @Override
    protected String[] getOpciones() {
        return new String[] {
                "Ver inventario",
                "Reabastecer producto",
                "Registrar venta",
                "Ver ventas",
                "Ver movimientos de inventario",
                "Registrar sugerencia de producto",
                "Ver sugerencias de productos",
                "Decidir sugerencia de producto"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                verInventario();
                break;
            case 2:
                reabastecerProducto();
                break;
            case 3:
                registrarVenta();
                break;
            case 4:
                verVentas();
                break;
            case 5:
                verMovimientosInventario();
                break;
            case 6:
                registrarSugerenciaProducto();
                break;
            case 7:
                verSugerenciasProducto();
                break;
            case 8:
                decidirSugerenciaProducto();
                break;
        }
    }

    private void verInventario() {
        System.out.println();
        System.out.println("--- INVENTARIO ---");

        boolean hayExistencias = false;

        for (UbicacionInventario ubicacion : club.getUbicacionesInventario()) {
            for (ExistenciaInventario existencia : ubicacion.getExistencias()) {
                hayExistencias = true;

                System.out.println("Producto: " + existencia.getProducto().getNombre()
                        + " | Ubicación: " + ubicacion.getNombre()
                        + " | Cantidad: " + existencia.getCantidad());
            }
        }

        if (!hayExistencias) {
            System.out.println("No hay productos registrados en el inventario.");
        }
    }

    private void reabastecerProducto() {
        System.out.println();
        System.out.println("--- REABASTECER PRODUCTO ---");

        String nombreProducto = readString("Nombre del producto: ");
        Producto producto = club.buscarProducto(nombreProducto);

        try {
            if (producto == null) {
                System.out.println("El producto no existe. Se creará uno nuevo.");

                double precio = readDouble("Precio: ");
                String categoria = readString("Categoría: ");

                producto = new ArticuloTienda(nombreProducto, precio, categoria);
                club.registrarProducto(producto);
            }

            String nombreUbicacion = readString("Nombre de la ubicación: ");
            UbicacionInventario ubicacion = club.buscarUbicacionInventario(nombreUbicacion);

            if (ubicacion == null) {
                ubicacion = new UbicacionInventario(nombreUbicacion);
                club.registrarUbicacionInventario(ubicacion);
            }

            int cantidad = readInt("Cantidad que desea agregar: ");
            club.reabastecerProducto(producto, ubicacion, cantidad);

            System.out.println("Inventario actualizado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("No fue posible actualizar el inventario: " + e.getMessage());
        }
    }

    private void registrarVenta() {
        System.out.println();
        System.out.println("--- REGISTRAR VENTA ---");

        String id = readString("ID del comprador: ");
        Usuario usuario = club.buscarUsuario(id);

        if (!(usuario instanceof Comprador)) {
            System.out.println("No existe un comprador con ese ID.");
            return;
        }

        Comprador comprador = (Comprador) usuario;
        String nombreProducto = readString("Nombre del producto: ");
        Producto producto = club.buscarProducto(nombreProducto);

        if (producto == null) {
            System.out.println("No existe ese producto.");
            return;
        }

        String nombreUbicacion = readString("Nombre de la ubicación: ");
        UbicacionInventario ubicacion = club.buscarUbicacionInventario(nombreUbicacion);

        if (ubicacion == null) {
            System.out.println("No existe esa ubicación.");
            return;
        }

        int cantidad = readInt("Cantidad: ");
        boolean usaCodigoCompartido = readBoolean("¿Usa código compartido? (s/n): ");

        try {
            VentaTienda venta = new VentaTienda(LocalDate.now(), comprador);

            venta.agregarDetalle(producto, cantidad);
            venta.finalizarVenta(usaCodigoCompartido);
            club.procesarVenta(venta, ubicacion);
            System.out.println("Venta registrada correctamente.");
            mostrarDatosVenta(venta);
        } catch (InventarioInsuficienteException e) {
            System.out.println("No fue posible realizar la venta: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());
        }
    }

    private void mostrarDatosVenta(Venta venta) {
        System.out.println("Subtotal: " + venta.getSubtotal());
        System.out.println("Descuento: " + venta.getDescuento());
        System.out.println("Política de descuento: "
                + venta.getPoliticaDescuento().getClass().getSimpleName());
        System.out.println("Impuesto: " + venta.getImpuesto());
        System.out.println("Total: " + venta.getTotal());
        System.out.println("Puntos generados: " + venta.getPuntosGenerados());
    }

    private void verVentas() {
        System.out.println();
        System.out.println("--- VENTAS REGISTRADAS ---");

        if (club.getVentas().isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        int numero = 1;

        for (Venta venta : club.getVentas()) {
            System.out.println();
            System.out.println("Venta #" + numero);
            System.out.println("Fecha: " + venta.getFecha());
            System.out.println("Comprador: " + venta.getComprador().getNombre());
            mostrarDatosVenta(venta);
            numero++;
        }
    }

    private void verMovimientosInventario() {
        System.out.println();
        System.out.println("--- MOVIMIENTOS DE INVENTARIO ---");

        if (club.getMovimientosInventario().isEmpty()) {
            System.out.println("No hay movimientos registrados.");
            return;
        }

        int numero = 1;

        for (MovimientoInventario movimiento : club.getMovimientosInventario()) {
            System.out.println();
            System.out.println("Movimiento #" + numero);
            System.out.println("Fecha: " + movimiento.getFecha());
            System.out.println("Tipo: " + movimiento.getTipo());
            System.out.println("Producto: " + movimiento.getProducto().getNombre());
            System.out.println("Cantidad: " + movimiento.getCantidad());

            if (movimiento.getOrigen() != null) {
                System.out.println("Origen: " + movimiento.getOrigen().getNombre());
            }

            if (movimiento.getDestino() != null) {
                System.out.println("Destino: " + movimiento.getDestino().getNombre());
            }

            numero++;
        }
    }

    private void registrarSugerenciaProducto() {
        System.out.println();
        System.out.println("--- REGISTRAR SUGERENCIA DE PRODUCTO ---");

        Empleado empleado = seleccionarEmpleado();

        if (empleado == null) {
            return;
        }

        String nombre = readString("Nombre del producto sugerido: ");
        String descripcion = readString("Descripción: ");

        try {
            SugerenciaProducto sugerencia = new SugerenciaProducto(
                    nombre,
                    descripcion,
                    empleado);

            club.registrarSugerencia(sugerencia);

            System.out.println(
                    "Sugerencia registrada correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "No fue posible registrar la sugerencia: "
                            + e.getMessage());
        }
    }

    private void verSugerenciasProducto() {
        System.out.println();
        System.out.println("--- SUGERENCIAS DE PRODUCTOS ---");

        if (club.getSugerenciasProducto().isEmpty()) {
            System.out.println(
                    "No hay sugerencias registradas.");
            return;
        }

        int numero = 1;

        for (SugerenciaProducto sugerencia : club.getSugerenciasProducto()) {
            System.out.println();
            System.out.println("Sugerencia #" + numero);
            System.out.println("Nombre: " + sugerencia.getNombre());
            System.out.println(
                    "Descripción: "
                            + sugerencia.getDescripcion());
            System.out.println(
                    "Proponente: "
                            + sugerencia.getProponente().getNombre());
            System.out.println(
                    "Estado: " + sugerencia.getEstado());

            if (sugerencia.getAdministrador() != null) {
                System.out.println(
                        "Administrador: "
                                + sugerencia.getAdministrador().getNombre());
            }

            if (sugerencia.getProductoCreado() != null) {
                System.out.println(
                        "Producto creado: "
                                + sugerencia.getProductoCreado().getNombre());
            }

            numero++;
        }
    }

    private void decidirSugerenciaProducto() {
        System.out.println();
        System.out.println("--- DECIDIR SUGERENCIA DE PRODUCTO ---");

        SugerenciaProducto sugerencia = seleccionarSugerenciaProducto();

        if (sugerencia == null) {
            return;
        }

        String idAdministrador = readString(
                "ID del administrador: ");

        Usuario usuario = club.buscarUsuario(idAdministrador);

        if (!(usuario instanceof Administrador)) {
            System.out.println(
                    "No existe un administrador con ese ID.");
            return;
        }

        boolean aprobar = readBoolean(
                "¿Desea aprobar la sugerencia? (s/n): ");

        try {
            if (aprobar) {
                double precio = readDouble(
                        "Precio del producto: ");
                String categoria = readString(
                        "Categoría del producto: ");

                Producto producto = new ArticuloTienda(
                        sugerencia.getNombre(),
                        precio,
                        categoria);

                club.aprobarSugerencia(
                        sugerencia,
                        (Administrador) usuario,
                        producto);

                System.out.println(
                        "Sugerencia aprobada y producto registrado.");
            } else {
                club.rechazarSugerencia(
                        sugerencia,
                        (Administrador) usuario);

                System.out.println(
                        "Sugerencia rechazada.");
            }
        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            System.out.println(
                    "No fue posible decidir la sugerencia: "
                            + e.getMessage());
        }
    }

    private SugerenciaProducto seleccionarSugerenciaProducto() {
        if (club.getSugerenciasProducto().isEmpty()) {
            System.out.println(
                    "No hay sugerencias registradas.");
            return null;
        }

        System.out.println("Sugerencias disponibles:");

        int numero = 1;

        for (SugerenciaProducto sugerencia : club.getSugerenciasProducto()) {
            System.out.println(
                    numero + ". " + sugerencia);
            numero++;
        }

        int seleccion = readInt(
                "Número de la sugerencia: ");

        if (seleccion < 1
                || seleccion > club.getSugerenciasProducto().size()) {

            System.out.println(
                    "La sugerencia seleccionada no es válida.");
            return null;
        }

        return club.getSugerenciasProducto().get(
                seleccion - 1);
    }
}

package utilidades.persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import clubdeportivo.ClubDeportivo;
import clubdeportivo.deportes.Modalidad;
import clubdeportivo.instalaciones.Instalacion;
import clubdeportivo.instalaciones.Reserva;
import clubdeportivo.instalaciones.TipoInstalacion;
import clubdeportivo.membresias.PagoMensualidad;
import tiendaclub.inventario.ArticuloTienda;
import tiendaclub.inventario.ExistenciaInventario;
import tiendaclub.inventario.MovimientoInventario;
import tiendaclub.inventario.Producto;
import tiendaclub.inventario.TipoMovimiento;
import tiendaclub.inventario.UbicacionInventario;
import usuarios.Usuario;
import usuarios.tipos.Socio;
import tiendaclub.ventas.Venta;
import tiendaclub.ventas.detalles.DetalleVenta;
import tiendaclub.ventas.descuentos.DescuentoCodigoCompartido;
import tiendaclub.ventas.tipos.VentaCafeteria;
import tiendaclub.ventas.tipos.VentaTienda;
import usuarios.Comprador;

public class PersistenciaClub {
    private static final Path CARPETA = Paths.get("datos");
    private static final Path ARCHIVO_SOCIOS = CARPETA.resolve("socios.txt");
    private static final Path ARCHIVO_PRODUCTOS = CARPETA.resolve("productos.txt");
    private static final Path ARCHIVO_INVENTARIO = CARPETA.resolve("inventario.txt");
    private static final Path ARCHIVO_MOVIMIENTOS = CARPETA.resolve("movimientos.txt");
    private static final Path ARCHIVO_PAGOS = CARPETA.resolve("pagos.txt");
    private static final Path ARCHIVO_INSTALACIONES = CARPETA.resolve("instalaciones.txt");
    private static final Path ARCHIVO_RESERVAS = CARPETA.resolve("reservas.txt");
    private static final Path ARCHIVO_VENTAS = CARPETA.resolve("ventas.txt");

    public void guardar(ClubDeportivo club) throws IOException {
        if (club == null) {
            throw new IllegalArgumentException("El club es obligatorio");
        }

        Files.createDirectories(CARPETA);

        guardarSocios(club);
        guardarProductos(club);
        guardarInventario(club);
        guardarMovimientos(club);
        guardarPagos(club);
        guardarInstalaciones(club);
        guardarReservas(club);
        guardarVentas(club);
    }

    public void cargar(ClubDeportivo club) throws IOException {
        if (club == null) {
            throw new IllegalArgumentException("El club es obligatorio");
        }

        Files.createDirectories(CARPETA);

        cargarSocios(club);
        cargarProductos(club);
        cargarInventario(club);
        cargarInstalaciones(club);
        cargarMovimientos(club);
        cargarPagos(club);
        cargarReservas(club);
        cargarVentas(club);
    }

    // Socios

    private void guardarSocios(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_SOCIOS)) {
            for (Usuario usuario : club.getUsuarios()) {
                if (usuario instanceof Socio) {
                    Socio socio = (Socio) usuario;

                    escritor.write(socio.getId() + ";"
                            + socio.getNombre() + ";"
                            + socio.getFechaNacimiento() + ";"
                            + socio.getLogin() + ";"
                            + socio.getContrasena() + ";"
                            + socio.getPuntosFidelidad());

                    escritor.newLine();
                }
            }
        }
    }

    private void cargarSocios(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_SOCIOS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_SOCIOS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 6) {
                    continue;
                }

                Socio socio = new Socio(datos[0], datos[1], LocalDate.parse(datos[2]),
                        datos[3], datos[4]);

                socio.agregarPuntos(Double.parseDouble(datos[5]));
                club.registrarUsuario(socio);
            }
        }
    }

    // Productos

    private void guardarProductos(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_PRODUCTOS)) {
            for (Producto producto : club.getProductos()) {
                if (producto instanceof ArticuloTienda) {
                    ArticuloTienda articulo = (ArticuloTienda) producto;

                    escritor.write(articulo.getNombre() + ";"
                            + articulo.getPrecio() + ";"
                            + articulo.getCategoria());

                    escritor.newLine();
                }
            }
        }
    }

    private void cargarProductos(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_PRODUCTOS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_PRODUCTOS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 3) {
                    continue;
                }

                ArticuloTienda producto = new ArticuloTienda(
                        datos[0], Double.parseDouble(datos[1]), datos[2]);

                club.registrarProducto(producto);
            }
        }
    }

    // Inventario

    private void guardarInventario(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_INVENTARIO)) {
            for (UbicacionInventario ubicacion : club.getUbicacionesInventario()) {
                for (ExistenciaInventario existencia : ubicacion.getExistencias()) {
                    escritor.write(existencia.getProducto().getNombre() + ";"
                            + ubicacion.getNombre() + ";"
                            + existencia.getCantidad());

                    escritor.newLine();
                }
            }
        }
    }

    private void cargarInventario(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_INVENTARIO)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_INVENTARIO)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 3) {
                    continue;
                }

                Producto producto = club.buscarProducto(datos[0]);

                if (producto == null) {
                    continue;
                }

                UbicacionInventario ubicacion = club.buscarUbicacionInventario(datos[1]);

                if (ubicacion == null) {
                    ubicacion = new UbicacionInventario(datos[1]);
                    club.registrarUbicacionInventario(ubicacion);
                }

                int cantidad = Integer.parseInt(datos[2]);
                ExistenciaInventario existencia = new ExistenciaInventario(
                        producto, ubicacion, cantidad);

                ubicacion.agregarExistencia(existencia);
            }
        }
    }

    // Movimientos

    private void guardarMovimientos(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_MOVIMIENTOS)) {
            for (MovimientoInventario movimiento : club.getMovimientosInventario()) {
                String origen = "";
                String destino = "";

                if (movimiento.getOrigen() != null) {
                    origen = movimiento.getOrigen().getNombre();
                }

                if (movimiento.getDestino() != null) {
                    destino = movimiento.getDestino().getNombre();
                }

                escritor.write(movimiento.getFecha() + ";"
                        + movimiento.getCantidad() + ";"
                        + movimiento.getTipo() + ";"
                        + movimiento.getProducto().getNombre() + ";"
                        + origen + ";"
                        + destino);

                escritor.newLine();
            }
        }
    }

    private void cargarMovimientos(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_MOVIMIENTOS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_MOVIMIENTOS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 6) {
                    continue;
                }

                Producto producto = club.buscarProducto(datos[3]);

                if (producto == null) {
                    continue;
                }

                UbicacionInventario origen = null;
                UbicacionInventario destino = null;

                if (!datos[4].isEmpty()) {
                    origen = club.buscarUbicacionInventario(datos[4]);
                }

                if (!datos[5].isEmpty()) {
                    destino = club.buscarUbicacionInventario(datos[5]);
                }

                TipoMovimiento tipo = TipoMovimiento.valueOf(datos[2]);

                MovimientoInventario movimiento = new MovimientoInventario(
                        LocalDate.parse(datos[0]), Integer.parseInt(datos[1]),
                        tipo, producto, origen, destino);

                club.registrarMovimiento(movimiento);
            }
        }
    }

    // Pagos

    private void guardarPagos(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_PAGOS)) {
            for (PagoMensualidad pago : club.getPagosMensualidad()) {
                escritor.write(pago.getFecha() + ";"
                        + pago.getPeriodo() + ";"
                        + pago.getValor() + ";"
                        + pago.getSocio().getId());

                escritor.newLine();
            }
        }
    }

    private void cargarPagos(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_PAGOS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_PAGOS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 4) {
                    continue;
                }

                Usuario usuario = club.buscarUsuario(datos[3]);

                if (!(usuario instanceof Socio)) {
                    continue;
                }

                PagoMensualidad pago = new PagoMensualidad(
                        LocalDate.parse(datos[0]), datos[1],
                        Double.parseDouble(datos[2]), (Socio) usuario);

                club.registrarPagoMensualidad(pago);
            }
        }
    }

    // Instalaciones

    private void guardarInstalaciones(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_INSTALACIONES)) {
            for (Instalacion instalacion : club.getInstalaciones()) {
                escritor.write(instalacion.getTipo() + ";"
                        + instalacion.getId() + ";"
                        + instalacion.getCapacidadMaxima() + ";"
                        + instalacion.isTechada());

                escritor.newLine();
            }
        }
    }

    private void cargarInstalaciones(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_INSTALACIONES)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_INSTALACIONES)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 4) {
                    continue;
                }

                TipoInstalacion tipo = TipoInstalacion.valueOf(datos[0]);
                String id = datos[1];
                int capacidad = Integer.parseInt(datos[2]);
                boolean techada = Boolean.parseBoolean(datos[3]);

                Instalacion instalacion = new Instalacion(id, tipo, capacidad, techada);
                club.registrarInstalacion(instalacion);
            }
        }
    }

    // Reservas

    private void guardarReservas(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_RESERVAS)) {
            for (Reserva reserva : club.getReservas()) {
                escritor.write(reserva.getSocio().getId() + ";"
                        + reserva.getInstalacion().getId() + ";"
                        + reserva.getFecha() + ";"
                        + reserva.getHoraInicio() + ";"
                        + reserva.getDuracionMinutos() + ";"
                        + reserva.getNumeroJugadores() + ";"
                        + reserva.getModalidad());

                escritor.newLine();
            }
        }
    }

    private void cargarReservas(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_RESERVAS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_RESERVAS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 7) {
                    continue;
                }

                Usuario usuario = club.buscarUsuario(datos[0]);
                Instalacion instalacion = club.buscarInstalacion(datos[1]);
                LocalDate fecha = LocalDate.parse(datos[2]);

                if (!(usuario instanceof Socio) || instalacion == null) {
                    continue;
                }

                /*
                 * Las reservas pasadas no se cargan porque el nuevo
                 * modelo no permite mantener reservas en fechas pasadas.
                 */
                if (fecha.isBefore(LocalDate.now())) {
                    continue;
                }

                Reserva reserva = new Reserva(
                        (Socio) usuario,
                        instalacion,
                        fecha,
                        LocalTime.parse(datos[3]),
                        Integer.parseInt(datos[4]),
                        Integer.parseInt(datos[5]),
                        Modalidad.valueOf(datos[6]));

                club.registrarReserva(reserva);
            }
        }
    }
    // Ventas

    private void guardarVentas(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_VENTAS)) {
            for (Venta venta : club.getVentas()) {
                if (!(venta.getComprador() instanceof Usuario)) {
                    continue;
                }

                String tipoVenta;

                if (venta instanceof VentaCafeteria) {
                    tipoVenta = "CAFETERIA";
                } else {
                    tipoVenta = "TIENDA";
                }

                boolean usaCodigoCompartido = venta.getPoliticaDescuento() instanceof DescuentoCodigoCompartido;
                boolean incluyePropina = venta instanceof VentaCafeteria
                        && ((VentaCafeteria) venta).getPropina() > 0;

                StringBuilder detalles = new StringBuilder();

                for (DetalleVenta detalle : venta.getDetalles()) {
                    if (detalles.length() > 0) {
                        detalles.append("|");
                    }

                    detalles.append(detalle.getProducto().getNombre())
                            .append(",")
                            .append(detalle.getCantidad());
                }

                Usuario comprador = (Usuario) venta.getComprador();

                escritor.write(venta.getFecha() + ";"
                        + comprador.getId() + ";"
                        + tipoVenta + ";"
                        + usaCodigoCompartido + ";"
                        + incluyePropina + ";"
                        + detalles);

                escritor.newLine();
            }
        }
    }

    private void cargarVentas(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_VENTAS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_VENTAS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 6) {
                    continue;
                }

                Usuario usuario = club.buscarUsuario(datos[1]);

                if (!(usuario instanceof Comprador)) {
                    continue;
                }

                LocalDate fecha = LocalDate.parse(datos[0]);
                Comprador comprador = (Comprador) usuario;
                boolean usaCodigoCompartido = Boolean.parseBoolean(datos[3]);
                boolean incluyePropina = Boolean.parseBoolean(datos[4]);
                Venta venta;

                if (datos[2].equals("CAFETERIA")) {
                    venta = new VentaCafeteria(fecha, comprador);
                } else {
                    venta = new VentaTienda(fecha, comprador);
                }

                String[] detallesGuardados = datos[5].split("\\|");

                for (String detalleGuardado : detallesGuardados) {
                    if (detalleGuardado.trim().isEmpty()) {
                        continue;
                    }

                    String[] datosDetalle = detalleGuardado.split(",", -1);

                    if (datosDetalle.length < 2) {
                        continue;
                    }

                    Producto producto = club.buscarProducto(datosDetalle[0]);

                    if (producto == null) {
                        continue;
                    }

                    int cantidad = Integer.parseInt(datosDetalle[1]);
                    venta.agregarDetalle(new DetalleVenta(producto, cantidad));
                }

                if (venta.getDetalles().isEmpty()) {
                    continue;
                }

                if (venta instanceof VentaCafeteria) {
                    ((VentaCafeteria) venta).finalizarVenta(usaCodigoCompartido, incluyePropina);
                } else {
                    ((VentaTienda) venta).finalizarVenta(usaCodigoCompartido);
                }

                club.registrarVenta(venta);
            }
        }
    }
}
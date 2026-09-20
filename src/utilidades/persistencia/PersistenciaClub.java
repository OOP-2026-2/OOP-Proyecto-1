package utilidades.persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalTime;
import clubdeportivo.ClubDeportivo;
import tiendaclub.TiendaClub;
import tiendaclub.inventario.ArticuloTienda;
import tiendaclub.inventario.ExistenciaInventario;
import tiendaclub.inventario.MovimientoInventario;
import tiendaclub.inventario.Producto;
import tiendaclub.inventario.TipoMovimiento;
import tiendaclub.inventario.UbicacionInventario;
import usuarios.tipos.Socio;
import usuarios.Usuario;
import clubdeportivo.membresias.PagoMensualidad;
import clubdeportivo.deportes.Modalidad;
import clubdeportivo.instalaciones.tipos.CanchaConjunto;
import clubdeportivo.instalaciones.tipos.CanchaPadel;
import clubdeportivo.instalaciones.tipos.CanchaTenis;
import clubdeportivo.instalaciones.Instalacion;
import clubdeportivo.instalaciones.tipos.MesaTenisMesa;
import clubdeportivo.instalaciones.Reserva;

public class PersistenciaClub {
    private static final Path CARPETA = Paths.get("datos");
    private static final Path ARCHIVO_SOCIOS = CARPETA.resolve("socios.txt");
    private static final Path ARCHIVO_PRODUCTOS = CARPETA.resolve("productos.txt");
    private static final Path ARCHIVO_INVENTARIO = CARPETA.resolve("TiendaClub.inventario.txt");
    private static final Path ARCHIVO_MOVIMIENTOS = CARPETA.resolve("movimientos.txt");
    private static final Path ARCHIVO_PAGOS = CARPETA.resolve("pagos.txt");
    private static final Path ARCHIVO_INSTALACIONES = CARPETA.resolve("ClubDeportivo.instalaciones.txt");
    private static final Path ARCHIVO_RESERVAS = CARPETA.resolve("reservas.txt");

    public void guardar(ClubDeportivo club, TiendaClub tienda)
            throws IOException {

        Files.createDirectories(CARPETA);

        guardarSocios(club);
        guardarProductos(tienda);
        guardarInventario(tienda);
        guardarMovimientos(tienda);
        guardarPagos(club);
        guardarInstalaciones(club);
        guardarReservas(club);
    }

    public void cargar(ClubDeportivo club, TiendaClub tienda)
            throws IOException {

        Files.createDirectories(CARPETA);

        cargarSocios(club);
        cargarProductos(tienda);
        cargarInventario(tienda);
        cargarInstalaciones(club);
        cargarMovimientos(tienda);
        cargarPagos(club);
        cargarReservas(club);
    }

    private void guardarSocios(ClubDeportivo club)
            throws IOException {

        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_SOCIOS)) {

            for (Usuario usuario : club.getUsuarios()) {
                if (usuario instanceof Socio) {
                    Socio socio = (Socio) usuario;

                    escritor.write(
                            socio.getId() + ";"
                                    + socio.getNombre() + ";"
                                    + socio.getLogin() + ";"
                                    + socio.getPassword() + ";"
                                    + socio.getFechaNacimiento() + ";"
                                    + socio.getPuntosFidelidad());

                    escritor.newLine();
                }
            }
        }
    }

    private void guardarProductos(TiendaClub tienda)
            throws IOException {

        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_PRODUCTOS)) {

            for (Producto producto : tienda.getProductos()) {

                if (producto instanceof ArticuloTienda) {
                    ArticuloTienda articulo = (ArticuloTienda) producto;

                    escritor.write(
                            articulo.getNombre() + ";"
                                    + articulo.getPrecio() + ";"
                                    + articulo.getCategoria());

                    escritor.newLine();
                }
            }
        }
    }

    private void guardarInventario(TiendaClub tienda)
            throws IOException {

        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_INVENTARIO)) {

            for (ExistenciaInventario existencia : tienda.getExistencias()) {

                escritor.write(
                        existencia.getProducto()
                                .getNombre()
                                + ";"
                                + existencia.getUbicacion()
                                        .getNombre()
                                + ";"
                                + existencia.getCantidad());

                escritor.newLine();
            }
        }
    }

    private void guardarMovimientos(
            TiendaClub tienda)
            throws IOException {

        try (BufferedWriter escritor = Files.newBufferedWriter(
                ARCHIVO_MOVIMIENTOS)) {

            for (MovimientoInventario movimiento : tienda.getMovimientosInventario()) {

                String origen = "";

                if (movimiento.getOrigen() != null) {
                    origen = movimiento.getOrigen()
                            .getNombre();
                }

                String destino = "";

                if (movimiento.getDestino() != null) {
                    destino = movimiento.getDestino()
                            .getNombre();
                }

                escritor.write(
                        movimiento.getFecha() + ";"
                                + movimiento.getCantidad() + ";"
                                + movimiento.getTipo() + ";"
                                + movimiento.getProducto()
                                        .getNombre()
                                + ";"
                                + origen + ";"
                                + destino);

                escritor.newLine();
            }
        }
    }

    private void guardarPagos(ClubDeportivo club)
            throws IOException {

        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_PAGOS)) {

            for (PagoMensualidad pago : club.getPagosMensualidad()) {

                escritor.write(
                        pago.getFecha() + ";"
                                + pago.getPeriodo() + ";"
                                + pago.getValor() + ";"
                                + pago.getSocio().getId());

                escritor.newLine();
            }
        }
    }

    private void cargarSocios(ClubDeportivo club)
            throws IOException {

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

                Socio socio;

                if (datos.length >= 6) {
                    socio = new Socio(datos[0], datos[1], Integer.parseInt(datos[2]), datos[3], datos[4]);

                    socio.setPuntosFidelidad(
                            Double.parseDouble(datos[5]));
                } else {
                    continue;
                }

                club.registrarUsuario(socio);
            }
        }
    }

    private void cargarProductos(
            TiendaClub tienda)
            throws IOException {

        if (!Files.exists(ARCHIVO_PRODUCTOS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(
                ARCHIVO_PRODUCTOS)) {

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
                        datos[0],
                        Double.parseDouble(
                                datos[1]),
                        datos[2]);

                tienda.registrarProducto(producto);
            }
        }
    }

    private void cargarInventario(
            TiendaClub tienda)
            throws IOException {

        if (!Files.exists(ARCHIVO_INVENTARIO)) {
            return;
        }

        Map<String, UbicacionInventario> ubicaciones = new HashMap<String, UbicacionInventario>();

        try (BufferedReader lector = Files.newBufferedReader(
                ARCHIVO_INVENTARIO)) {

            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 3) {
                    continue;
                }

                Producto producto = buscarProducto(
                        tienda,
                        datos[0]);

                if (producto == null) {
                    continue;
                }

                UbicacionInventario ubicacion = ubicaciones.get(datos[1]);

                if (ubicacion == null) {
                    ubicacion = new UbicacionInventario(
                            datos[1]);

                    ubicaciones.put(
                            datos[1],
                            ubicacion);
                }

                int cantidad = Integer.parseInt(datos[2]);

                ExistenciaInventario existencia = new ExistenciaInventario(
                        producto,
                        ubicacion,
                        cantidad);

                tienda.registrarExistencia(existencia);
            }
        }
    }

    private void cargarMovimientos(
            TiendaClub tienda)
            throws IOException {

        if (!Files.exists(ARCHIVO_MOVIMIENTOS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(
                ARCHIVO_MOVIMIENTOS)) {

            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 6) {
                    continue;
                }

                Producto producto = buscarProducto(
                        tienda,
                        datos[3]);

                if (producto == null) {
                    continue;
                }

                UbicacionInventario origen = null;
                UbicacionInventario destino = null;

                if (!datos[4].isEmpty()) {
                    origen = buscarUbicacion(
                            tienda,
                            datos[4]);
                }

                if (!datos[5].isEmpty()) {
                    destino = buscarUbicacion(
                            tienda,
                            datos[5]);
                }

                TipoMovimiento tipo = TipoMovimiento.valueOf(
                        datos[2]);

                MovimientoInventario movimiento = new MovimientoInventario(
                        LocalDate.parse(datos[0]),
                        Integer.parseInt(datos[1]),
                        tipo,
                        producto,
                        origen,
                        destino);

                tienda.registrarMovimientoInventario(
                        movimiento);
            }
        }
    }

    private void cargarPagos(
            ClubDeportivo club)
            throws IOException {

        if (!Files.exists(ARCHIVO_PAGOS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(
                ARCHIVO_PAGOS)) {

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
                        LocalDate.parse(datos[0]),
                        datos[1],
                        Double.parseDouble(datos[2]),
                        (Socio) usuario);

                club.registrarPagoMensualidad(pago);
            }
        }
    }

    private Producto buscarProducto(
            TiendaClub tienda,
            String nombre) {

        for (Producto producto : tienda.getProductos()) {

            if (producto.getNombre()
                    .equalsIgnoreCase(nombre)) {
                return producto;
            }
        }

        return null;
    }

    private UbicacionInventario buscarUbicacion(
            TiendaClub tienda,
            String nombre) {

        for (ExistenciaInventario existencia : tienda.getExistencias()) {

            if (existencia.getUbicacion()
                    .getNombre()
                    .equalsIgnoreCase(nombre)) {
                return existencia.getUbicacion();
            }
        }

        return null;
    }

    private void guardarInstalaciones(ClubDeportivo club)
            throws IOException {

        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_INSTALACIONES)) {

            for (Instalacion instalacion : club.getInstalaciones()) {

                String tipo;

                if (instalacion instanceof CanchaConjunto) {
                    tipo = "CANCHA_CONJUNTO";
                } else if (instalacion instanceof CanchaPadel) {
                    tipo = "CANCHA_PADEL";
                } else if (instalacion instanceof CanchaTenis) {
                    tipo = "CANCHA_TENIS";
                } else if (instalacion instanceof MesaTenisMesa) {
                    tipo = "MESA_TENIS_MESA";
                } else {
                    continue;
                }

                escritor.write(
                        tipo + ";"
                                + instalacion.getId() + ";"
                                + instalacion.getCapacidadMaxima() + ";"
                                + instalacion.isTechada());

                escritor.newLine();
            }
        }
    }

    private void cargarInstalaciones(ClubDeportivo club)
            throws IOException {

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

                String tipo = datos[0];
                String id = datos[1];
                int capacidad = Integer.parseInt(datos[2]);
                boolean techada = Boolean.parseBoolean(datos[3]);

                Instalacion instalacion;

                if (tipo.equals("CANCHA_CONJUNTO")) {
                    instalacion = new CanchaConjunto(
                            id,
                            capacidad,
                            techada);
                } else if (tipo.equals("CANCHA_PADEL")) {
                    instalacion = new CanchaPadel(
                            id,
                            capacidad,
                            techada);
                } else if (tipo.equals("CANCHA_TENIS")) {
                    instalacion = new CanchaTenis(
                            id,
                            capacidad,
                            techada);
                } else if (tipo.equals("MESA_TENIS_MESA")) {
                    instalacion = new MesaTenisMesa(
                            id,
                            capacidad,
                            techada);
                } else {
                    continue;
                }

                club.registrarInstalacion(instalacion);
            }
        }
    }

    private void guardarReservas(ClubDeportivo club)
            throws IOException {

        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_RESERVAS)) {

            for (Reserva reserva : club.getReservas()) {
                escritor.write(
                        reserva.getSocio().getId() + ";"
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

    private void cargarReservas(ClubDeportivo club)
            throws IOException {

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

                if (!(usuario instanceof Socio)
                        || instalacion == null) {
                    continue;
                }

                Reserva reserva = new Reserva(
                        (Socio) usuario,
                        instalacion,
                        LocalDate.parse(datos[2]),
                        LocalTime.parse(datos[3]),
                        Integer.parseInt(datos[4]),
                        Integer.parseInt(datos[5]),
                        Modalidad.valueOf(datos[6]));

                club.registrarReserva(reserva);
            }
        }
    }
}
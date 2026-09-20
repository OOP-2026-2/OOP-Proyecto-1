import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import clubdeportivo.ClubDeportivo;
import clubdeportivo.deportes.Modalidad;
import utilidades.excepciones.InventarioInsuficienteException;
import utilidades.excepciones.ReservaNoDisponibleException;
import clubdeportivo.instalaciones.tipos.CanchaPadel;
import clubdeportivo.instalaciones.Instalacion;
import clubdeportivo.instalaciones.Reserva;
import tiendaclub.inventario.ArticuloTienda;
import tiendaclub.inventario.ExistenciaInventario;
import tiendaclub.inventario.MovimientoInventario;
import tiendaclub.inventario.Producto;
import tiendaclub.inventario.UbicacionInventario;
import utilidades.persistencia.PersistenciaClub;
import usuarios.tipos.Socio;
import usuarios.Usuario;
import tiendaclub.ventas.detalles.DetalleVenta;
import clubdeportivo.membresias.PagoMensualidad;
import tiendaclub.ventas.Venta;
import tiendaclub.ventas.tipos.VentaTienda;
import tiendaclub.TiendaClub;

public class ConsolaClubDeportivo {
    private ClubDeportivo club;
    private Scanner scanner;
    private boolean ejecutando;
    private PersistenciaClub persistencia;
    private TiendaClub tienda;

    public ConsolaClubDeportivo() {
        club = new ClubDeportivo();
        tienda = new TiendaClub();
        scanner = new Scanner(System.in);
        persistencia = new PersistenciaClub();
        ejecutando = true;

        try {
            persistencia.cargar(club, tienda);
            System.out.println("Datos cargados correctamente.");
        } catch (IOException | RuntimeException e) {
            System.out.println(
                    "No fue posible cargar los datos: "
                            + e.getMessage());
        }
    }

    public void ejecutar() {
        System.out.println("================================");
        System.out.println("   CLUB DEPORTIVO SÉNECA");
        System.out.println("================================");

        while (ejecutando) {
            mostrarMenu();
            int opcion = leerEntero(
                    "Seleccione una opción: ");

            if (ejecutando) {
                ejecutarOpcion(opcion);
            }
        }

        guardarDatos();
        scanner.close();
        System.out.println("Programa finalizado.");
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("1. Registrar socio");
        System.out.println("2. Consultar usuario");
        System.out.println("3. Ver TiendaClub.inventario");
        System.out.println("4. Reabastecer producto");
        System.out.println("5. Registrar venta");
        System.out.println("6. Registrar reserva");
        System.out.println("7. Ver TiendaClub.ventas");
        System.out.println("8. Ver reservas");
        System.out.println("9. Registrar pago de mensualidad");
        System.out.println("10. Ver pagos de mensualidad");
        System.out.println("11. Ver movimientos de TiendaClub.inventario");
        System.out.println("0. Salir");
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                registrarSocio();
                break;
            case 2:
                consultarUsuario();
                break;
            case 3:
                verInventario();
                break;
            case 4:
                reabastecerProducto();
                break;
            case 5:
                registrarVenta();
                break;
            case 6:
                registrarReserva();
                break;
            case 7:
                verVentas();
                break;
            case 8:
                verReservas();
                break;
            case 9:
                registrarPagoMensualidad();
                break;
            case 10:
                verPagosMensualidad();
                break;
            case 11:
                verMovimientosInventario();
                break;
            case 0:
                ejecutando = false;
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }

    private void guardarDatos() {
        try {
            persistencia.guardar(club, tienda);
            System.out.println(
                    "Datos guardados correctamente.");
        } catch (IOException e) {
            System.out.println(
                    "No fue posible guardar los datos: "
                            + e.getMessage());
        }
    }

    private void registrarSocio() {
        System.out.println();
        System.out.println("--- REGISTRAR SOCIO ---");

        String id = leerTexto("ID: ");
        String nombre = leerTexto("Nombre: ");
        String login = leerTexto("Login: ");
        String contrasena = leerTexto("Contraseña: ");
        int fechaNacimiento = leerEntero(
                "Año de nacimiento: ");

        if (!ejecutando) {
            return;
        }

        try {

            Socio socio = new Socio(
                    id,
                    nombre,
                    fechaNacimiento,
                    login,
                    contrasena);

            club.registrarUsuario(socio);

            System.out.println(
                    "Socio registrado correctamente.");
        } catch (DateTimeParseException e) {
            System.out.println(
                    "Fecha inválida. Use el formato AAAA-MM-DD.");
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "No fue posible registrar el socio: "
                            + e.getMessage());
        }
    }

    private void consultarUsuario() {
        System.out.println();
        System.out.println("--- CONSULTAR USUARIO ---");

        String id = leerTexto("ID del usuario: ");
        Usuario usuario = club.buscarUsuario(id);

        if (usuario == null) {
            System.out.println(
                    "No existe un usuario con ese ID.");
            return;
        }

        System.out.println("ID: " + usuario.getId());
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("Login: " + usuario.getLogin());

        if (usuario instanceof Socio) {
            Socio socio = (Socio) usuario;

            System.out.println(
                    "Fecha de nacimiento: "
                            + socio.getFechaNacimiento());

            System.out.println(
                    "Puntos de fidelidad: "
                            + socio.getPuntosFidelidad());
        }
    }

    private void verInventario() {
        System.out.println();
        System.out.println("--- INVENTARIO ---");

        if (tienda.getExistencias().isEmpty()) {
            System.out.println(
                    "No hay productos registrados en el TiendaClub.inventario.");
            return;
        }

        for (ExistenciaInventario existencia : tienda.getExistencias()) {

            System.out.println(
                    "Producto: "
                            + existencia.getProducto().getNombre()
                            + " | Ubicación: "
                            + existencia.getUbicacion().getNombre()
                            + " | Cantidad: "
                            + existencia.getCantidad());
        }
    }

    private void reabastecerProducto() {
        System.out.println();
        System.out.println(
                "--- REABASTECER PRODUCTO ---");

        String nombreProducto = leerTexto(
                "Nombre del producto: ");

        Producto producto = buscarProducto(nombreProducto);

        if (producto == null) {
            System.out.println(
                    "El producto no existe. Se creará uno nuevo.");

            double precio = leerDouble("Precio: ");
            String categoria = leerTexto("Categoría: ");

            producto = new ArticuloTienda(
                    nombreProducto,
                    precio,
                    categoria);

            tienda.registrarProducto(producto);
        }

        String nombreUbicacion = leerTexto(
                "Nombre de la ubicación: ");

        UbicacionInventario ubicacion = buscarUbicacion(nombreUbicacion);

        if (ubicacion == null) {
            ubicacion = new UbicacionInventario(
                    nombreUbicacion);
        }

        int cantidad = leerEntero(
                "Cantidad que desea agregar: ");

        try {
            tienda.reabastecerProducto(
                    producto,
                    ubicacion,
                    cantidad);

            System.out.println(
                    "Inventario actualizado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "No fue posible actualizar el TiendaClub.inventario: "
                            + e.getMessage());
        }
    }

    private void registrarVenta() {
        System.out.println();
        System.out.println("--- REGISTRAR VENTA ---");

        String id = leerTexto("ID del comprador: ");
        Usuario comprador = club.buscarUsuario(id);

        if (comprador == null) {
            System.out.println(
                    "No existe un usuario con ese ID.");
            return;
        }

        String nombreProducto = leerTexto(
                "Nombre del producto: ");

        Producto producto = buscarProducto(nombreProducto);

        if (producto == null) {
            System.out.println(
                    "No existe ese producto.");
            return;
        }

        String nombreUbicacion = leerTexto(
                "Nombre de la ubicación: ");

        UbicacionInventario ubicacion = buscarUbicacion(nombreUbicacion);

        if (ubicacion == null) {
            System.out.println(
                    "No existe esa ubicación.");
            return;
        }

        int cantidad = leerEntero("Cantidad: ");

        boolean usaCodigoCompartido = leerSiNo(
                "¿Usa código compartido? (s/n): ");

        try {
            VentaTienda venta = new VentaTienda(
                    LocalDate.now(),
                    comprador);

            DetalleVenta detalle = new DetalleVenta(
                    producto,
                    cantidad);

            venta.agregarDetalle(detalle);
            venta.finalizarVenta(
                    usaCodigoCompartido);

            tienda.procesarVenta(
                    venta,
                    ubicacion);

            System.out.println(
                    "Venta registrada correctamente.");

            System.out.println(
                    "Subtotal: " + venta.getSubtotal());

            System.out.println(
                    "Descuento: " + venta.getDescuento());

            System.out.println(
                    "Tipo de descuento: "
                            + venta.getTipoDescuento());

            System.out.println(
                    "Impuesto: " + venta.getImpuesto());

            System.out.println(
                    "Total: " + venta.getTotal());

            System.out.println(
                    "Puntos generados: "
                            + venta.getPuntosGenerados());
        } catch (InventarioInsuficienteException e) {
            System.out.println(
                    "No fue posible realizar la venta: "
                            + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Datos inválidos: "
                            + e.getMessage());
        }
    }

    private void registrarReserva() {
        System.out.println();
        System.out.println("--- REGISTRAR RESERVA ---");

        String idSocio = leerTexto("ID del socio: ");
        Usuario usuario = club.buscarUsuario(idSocio);

        if (!(usuario instanceof Socio)) {
            System.out.println(
                    "No existe un socio con ese ID.");
            return;
        }

        Socio socio = (Socio) usuario;

        String idCancha = leerTexto(
                "ID de la cancha: ");

        Instalacion instalacion = club.buscarInstalacion(idCancha);

        if (instalacion == null) {
            System.out.println(
                    "La cancha no existe. Se creará una cancha de pádel.");

            int capacidad = leerEntero(
                    "Capacidad máxima: ");

            boolean techada = leerSiNo(
                    "¿Es techada? (s/n): ");

            instalacion = new CanchaPadel(
                    idCancha,
                    capacidad,
                    techada);

            club.registrarInstalacion(instalacion);
        }

        String fechaTexto = leerTexto(
                "Fecha de la reserva (AAAA-MM-DD): ");

        String horaTexto = leerTexto(
                "Hora de inicio (HH:MM): ");

        int duracion = leerEntero(
                "Duración en minutos: ");

        int numeroJugadores = leerEntero(
                "Número de jugadores: ");

        String modalidadTexto = leerTexto(
                "Modalidad (SENCILLOS/DOBLES): ");

        try {
            LocalDate fecha = LocalDate.parse(fechaTexto);

            LocalTime hora = LocalTime.parse(horaTexto);

            Modalidad modalidad = Modalidad.valueOf(
                    modalidadTexto.toUpperCase());

            if (fecha.isBefore(LocalDate.now())) {
                System.out.println(
                        "No se pueden registrar reservas en fechas pasadas.");
                return;
            }

            if (duracion <= 0) {
                System.out.println(
                        "La duración debe ser mayor que cero.");
                return;
            }

            if (numeroJugadores <= 0) {
                System.out.println(
                        "El número de jugadores debe ser mayor que cero.");
                return;
            }

            if (modalidad == Modalidad.SENCILLOS
                    && numeroJugadores != 2) {
                System.out.println(
                        "La modalidad SENCILLOS requiere 2 jugadores.");
                return;
            }

            if (modalidad == Modalidad.DOBLES
                    && numeroJugadores != 4) {
                System.out.println(
                        "La modalidad DOBLES requiere 4 jugadores.");
                return;
            }

            Reserva reserva = new Reserva(
                    socio,
                    instalacion,
                    fecha,
                    hora,
                    duracion,
                    numeroJugadores,
                    modalidad);

            club.registrarReserva(reserva);

            System.out.println(
                    "Reserva registrada correctamente.");

            System.out.println(
                    "Horario: "
                            + hora
                            + " - "
                            + reserva.calcularHoraFin());
        } catch (ReservaNoDisponibleException e) {
            System.out.println(
                    "No fue posible reservar: "
                            + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println(
                    "Fecha u hora inválida. Use AAAA-MM-DD y HH:MM.");
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Datos inválidos: "
                            + e.getMessage());
        }
    }

    private void registrarPagoMensualidad() {
        System.out.println();
        System.out.println(
                "--- REGISTRAR PAGO DE MENSUALIDAD ---");

        String id = leerTexto("ID del socio: ");
        Usuario usuario = club.buscarUsuario(id);

        if (!(usuario instanceof Socio)) {
            System.out.println(
                    "No existe un socio con ese ID.");
            return;
        }

        String periodo = leerTexto(
                "Periodo del pago: ");

        double valor = leerDouble(
                "Valor de la mensualidad: ");

        try {
            PagoMensualidad pago = new PagoMensualidad(
                    LocalDate.now(),
                    periodo,
                    valor,
                    (Socio) usuario);

            club.registrarPagoMensualidad(pago);

            System.out.println(
                    "Pago registrado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "No fue posible registrar el pago: "
                            + e.getMessage());
        }
    }

    private void verPagosMensualidad() {
        System.out.println();
        System.out.println(
                "--- PAGOS DE MENSUALIDAD ---");

        if (club.getPagosMensualidad().isEmpty()) {
            System.out.println(
                    "No hay pagos registrados.");
            return;
        }

        int numero = 1;

        for (PagoMensualidad pago : club.getPagosMensualidad()) {

            System.out.println();
            System.out.println("Pago #" + numero);
            System.out.println(
                    "Socio: "
                            + pago.getSocio().getNombre());
            System.out.println(
                    "Fecha: " + pago.getFecha());
            System.out.println(
                    "Periodo: " + pago.getPeriodo());
            System.out.println(
                    "Valor: " + pago.getValor());

            numero++;
        }
    }

    private void verMovimientosInventario() {
        System.out.println();
        System.out.println(
                "--- MOVIMIENTOS DE INVENTARIO ---");

        if (tienda.getMovimientosInventario().isEmpty()) {
            System.out.println(
                    "No hay movimientos registrados.");
            return;
        }

        int numero = 1;

        for (MovimientoInventario movimiento : tienda.getMovimientosInventario()) {

            System.out.println();
            System.out.println(
                    "Movimiento #" + numero);

            System.out.println(
                    "Fecha: "
                            + movimiento.getFecha());

            System.out.println(
                    "Tipo: "
                            + movimiento.getTipo());

            System.out.println(
                    "Producto: "
                            + movimiento.getProducto()
                                    .getNombre());

            System.out.println(
                    "Cantidad: "
                            + movimiento.getCantidad());

            if (movimiento.getOrigen() != null) {
                System.out.println(
                        "Origen: "
                                + movimiento.getOrigen()
                                        .getNombre());
            }

            if (movimiento.getDestino() != null) {
                System.out.println(
                        "Destino: "
                                + movimiento.getDestino()
                                        .getNombre());
            }

            numero++;
        }
    }

    private void verVentas() {
        System.out.println();
        System.out.println(
                "--- VENTAS REGISTRADAS ---");

        if (tienda.getVentas().isEmpty()) {
            System.out.println(
                    "No hay TiendaClub.ventas registradas.");
            return;
        }

        int numeroVenta = 1;

        for (Venta venta : tienda.getVentas()) {
            System.out.println();
            System.out.println(
                    "Venta #" + numeroVenta);

            System.out.println(
                    "Fecha: " + venta.getFecha());

            System.out.println(
                    "Comprador: "
                            + venta.getComprador()
                                    .getNombre());

            System.out.println(
                    "Subtotal: "
                            + venta.getSubtotal());

            System.out.println(
                    "Descuento: "
                            + venta.getDescuento());

            System.out.println(
                    "Tipo de descuento: "
                            + venta.getTipoDescuento());

            System.out.println(
                    "Impuesto: "
                            + venta.getImpuesto());

            System.out.println(
                    "Total: "
                            + venta.getTotal());

            System.out.println(
                    "Puntos generados: "
                            + venta.getPuntosGenerados());

            numeroVenta++;
        }
    }

    private void verReservas() {
        System.out.println();
        System.out.println(
                "--- RESERVAS REGISTRADAS ---");

        if (club.getReservas().isEmpty()) {
            System.out.println(
                    "No hay reservas registradas.");
            return;
        }

        int numeroReserva = 1;

        for (Reserva reserva : club.getReservas()) {
            System.out.println();
            System.out.println(
                    "Reserva #" + numeroReserva);

            System.out.println(
                    "Socio: "
                            + reserva.getSocio()
                                    .getNombre());

            System.out.println(
                    "Instalación: "
                            + reserva.getInstalacion()
                                    .getId());

            System.out.println(
                    "Fecha: "
                            + reserva.getFecha());

            System.out.println(
                    "Horario: "
                            + reserva.getHoraInicio()
                            + " - "
                            + reserva.calcularHoraFin());

            System.out.println(
                    "Jugadores: "
                            + reserva.getNumeroJugadores());

            System.out.println(
                    "Modalidad: "
                            + reserva.getModalidad());

            numeroReserva++;
        }
    }

    private Producto buscarProducto(String nombre) {
        for (Producto producto : tienda.getProductos()) {
            if (producto.getNombre()
                    .equalsIgnoreCase(nombre)) {
                return producto;
            }
        }

        return null;
    }

    private UbicacionInventario buscarUbicacion(
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

    private String leerTexto(String mensaje) {
        while (ejecutando) {
            System.out.print(mensaje);

            if (!scanner.hasNextLine()) {
                ejecutando = false;
                return "";
            }

            String entrada = scanner.nextLine().trim();

            if (!entrada.isEmpty()) {
                return entrada;
            }

            System.out.println(
                    "El valor no puede estar vacío.");
        }

        return "";
    }

    private int leerEntero(String mensaje) {
        while (ejecutando) {
            String entrada = leerTexto(mensaje);

            if (!ejecutando) {
                return 0;
            }

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Debe escribir un número entero.");
            }
        }

        return 0;
    }

    private double leerDouble(String mensaje) {
        while (ejecutando) {
            String entrada = leerTexto(mensaje);

            if (!ejecutando) {
                return 0;
            }

            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Debe escribir un número válido.");
            }
        }

        return 0;
    }

    private boolean leerSiNo(String mensaje) {
        while (ejecutando) {
            String respuesta = leerTexto(mensaje).toLowerCase();

            if (respuesta.equals("s")
                    || respuesta.equals("si")
                    || respuesta.equals("sí")) {
                return true;
            }

            if (respuesta.equals("n")
                    || respuesta.equals("no")) {
                return false;
            }

            System.out.println(
                    "Responda s o n.");
        }

        return false;
    }
}
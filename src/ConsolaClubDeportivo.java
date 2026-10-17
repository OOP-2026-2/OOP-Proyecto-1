import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
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
import tiendaclub.inventario.UbicacionInventario;
import tiendaclub.ventas.Venta;
import tiendaclub.ventas.tipos.VentaTienda;
import usuarios.Comprador;
import usuarios.Usuario;
import usuarios.tipos.Socio;
import utilidades.excepciones.InventarioInsuficienteException;
import utilidades.excepciones.ReservaNoDisponibleException;
import utilidades.persistencia.PersistenciaClub;

public class ConsolaClubDeportivo {
        private ClubDeportivo club;
        private Scanner scanner;
        private boolean ejecutando;
        private PersistenciaClub persistencia;

        public ConsolaClubDeportivo() {
                club = new ClubDeportivo();
                scanner = new Scanner(System.in);
                persistencia = new PersistenciaClub();
                ejecutando = true;

                try {
                        persistencia.cargar(club);
                        System.out.println("Datos cargados correctamente.");
                } catch (IOException | RuntimeException e) {
                        System.out.println("No fue posible cargar los datos: " + e.getMessage());
                }
        }

        public void ejecutar() {
                System.out.println("================================");
                System.out.println("   CLUB DEPORTIVO SÉNECA");
                System.out.println("================================");

                while (ejecutando) {
                        mostrarMenu();
                        int opcion = leerEntero("Seleccione una opción: ");

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
                System.out.println("3. Ver inventario");
                System.out.println("4. Reabastecer producto");
                System.out.println("5. Registrar venta");
                System.out.println("6. Registrar reserva");
                System.out.println("7. Ver ventas");
                System.out.println("8. Ver reservas");
                System.out.println("9. Registrar pago de mensualidad");
                System.out.println("10. Ver pagos de mensualidad");
                System.out.println("11. Ver movimientos de inventario");
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
                        persistencia.guardar(club);
                        System.out.println("Datos guardados correctamente.");
                } catch (IOException e) {
                        System.out.println("No fue posible guardar los datos: " + e.getMessage());
                }
        }

        private void registrarSocio() {
                System.out.println();
                System.out.println("--- REGISTRAR SOCIO ---");

                String id = leerTexto("ID: ");
                String nombre = leerTexto("Nombre: ");
                String fechaTexto = leerTexto("Fecha de nacimiento (AAAA-MM-DD): ");
                String login = leerTexto("Login: ");
                String contrasena = leerTexto("Contraseña: ");

                if (!ejecutando) {
                        return;
                }

                try {
                        LocalDate fechaNacimiento = LocalDate.parse(fechaTexto);
                        Socio socio = new Socio(id, nombre, fechaNacimiento, login, contrasena);

                        club.registrarUsuario(socio);
                        System.out.println("Socio registrado correctamente.");
                } catch (DateTimeParseException e) {
                        System.out.println("Fecha inválida. Use el formato AAAA-MM-DD.");
                } catch (IllegalArgumentException e) {
                        System.out.println("No fue posible registrar el socio: " + e.getMessage());
                }
        }

        private void consultarUsuario() {
                System.out.println();
                System.out.println("--- CONSULTAR USUARIO ---");

                String id = leerTexto("ID del usuario: ");
                Usuario usuario = club.buscarUsuario(id);

                if (usuario == null) {
                        System.out.println("No existe un usuario con ese ID.");
                        return;
                }

                System.out.println("ID: " + usuario.getId());
                System.out.println("Nombre: " + usuario.getNombre());
                System.out.println("Fecha de nacimiento: " + usuario.getFechaNacimiento());
                System.out.println("Login: " + usuario.getLogin());

                if (usuario instanceof Socio) {
                        Socio socio = (Socio) usuario;
                        System.out.println("Puntos de fidelidad: " + socio.getPuntosFidelidad());
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

                String nombreProducto = leerTexto("Nombre del producto: ");
                Producto producto = club.buscarProducto(nombreProducto);

                try {
                        if (producto == null) {
                                System.out.println("El producto no existe. Se creará uno nuevo.");

                                double precio = leerDouble("Precio: ");
                                String categoria = leerTexto("Categoría: ");

                                producto = new ArticuloTienda(nombreProducto, precio, categoria);
                                club.registrarProducto(producto);
                        }

                        String nombreUbicacion = leerTexto("Nombre de la ubicación: ");
                        UbicacionInventario ubicacion = club.buscarUbicacionInventario(nombreUbicacion);

                        if (ubicacion == null) {
                                ubicacion = new UbicacionInventario(nombreUbicacion);
                                club.registrarUbicacionInventario(ubicacion);
                        }

                        int cantidad = leerEntero("Cantidad que desea agregar: ");
                        club.reabastecerProducto(producto, ubicacion, cantidad);

                        System.out.println("Inventario actualizado correctamente.");
                } catch (IllegalArgumentException e) {
                        System.out.println("No fue posible actualizar el inventario: " + e.getMessage());
                }
        }

        private void registrarVenta() {
                System.out.println();
                System.out.println("--- REGISTRAR VENTA ---");

                String id = leerTexto("ID del comprador: ");
                Usuario usuario = club.buscarUsuario(id);

                if (!(usuario instanceof Comprador)) {
                        System.out.println("No existe un comprador con ese ID.");
                        return;
                }

                Comprador comprador = (Comprador) usuario;
                String nombreProducto = leerTexto("Nombre del producto: ");
                Producto producto = club.buscarProducto(nombreProducto);

                if (producto == null) {
                        System.out.println("No existe ese producto.");
                        return;
                }

                String nombreUbicacion = leerTexto("Nombre de la ubicación: ");
                UbicacionInventario ubicacion = club.buscarUbicacionInventario(nombreUbicacion);

                if (ubicacion == null) {
                        System.out.println("No existe esa ubicación.");
                        return;
                }

                int cantidad = leerEntero("Cantidad: ");
                boolean usaCodigoCompartido = leerSiNo("¿Usa código compartido? (s/n): ");

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

        private void registrarReserva() {
                System.out.println();
                System.out.println("--- REGISTRAR RESERVA ---");

                String idSocio = leerTexto("ID del socio: ");
                Usuario usuario = club.buscarUsuario(idSocio);

                if (!(usuario instanceof Socio)) {
                        System.out.println("No existe un socio con ese ID.");
                        return;
                }

                Socio socio = (Socio) usuario;
                String idInstalacion = leerTexto("ID de la instalación: ");
                Instalacion instalacion = club.buscarInstalacion(idInstalacion);

                try {
                        if (instalacion == null) {
                                System.out.println("La instalación no existe. Se creará una nueva.");
                                mostrarTiposInstalacion();

                                String tipoTexto = leerTexto("Tipo de instalación: ").toUpperCase();
                                TipoInstalacion tipo = TipoInstalacion.valueOf(tipoTexto);
                                int capacidad = leerEntero("Capacidad máxima: ");
                                boolean techada = leerSiNo("¿Es techada? (s/n): ");

                                instalacion = new Instalacion(idInstalacion, tipo, capacidad, techada);
                                club.registrarInstalacion(instalacion);
                        }

                        String fechaTexto = leerTexto("Fecha de la reserva (AAAA-MM-DD): ");
                        String horaTexto = leerTexto("Hora de inicio (HH:MM): ");
                        int duracion = leerEntero("Duración en minutos: ");
                        int numeroJugadores = leerEntero("Número de jugadores: ");
                        String modalidadTexto = leerTexto("Modalidad (SENCILLOS/DOBLES): ");

                        LocalDate fecha = LocalDate.parse(fechaTexto);
                        LocalTime hora = LocalTime.parse(horaTexto);
                        Modalidad modalidad = Modalidad.valueOf(modalidadTexto.toUpperCase());

                        Reserva reserva = new Reserva(socio, instalacion, fecha, hora,
                                        duracion, numeroJugadores, modalidad);

                        club.registrarReserva(reserva);

                        System.out.println("Reserva registrada correctamente.");
                        System.out.println("Horario: " + hora + " - " + reserva.calcularHoraFin());
                } catch (ReservaNoDisponibleException e) {
                        System.out.println("No fue posible reservar: " + e.getMessage());
                } catch (DateTimeParseException e) {
                        System.out.println("Fecha u hora inválida. Use AAAA-MM-DD y HH:MM.");
                } catch (IllegalArgumentException e) {
                        System.out.println("Datos inválidos: " + e.getMessage());
                }
        }

        private void mostrarTiposInstalacion() {
                System.out.println("Tipos disponibles:");

                for (TipoInstalacion tipo : TipoInstalacion.values()) {
                        System.out.println("- " + tipo);
                }
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

        private void verReservas() {
                System.out.println();
                System.out.println("--- RESERVAS REGISTRADAS ---");

                if (club.getReservas().isEmpty()) {
                        System.out.println("No hay reservas registradas.");
                        return;
                }

                int numero = 1;

                for (Reserva reserva : club.getReservas()) {
                        System.out.println();
                        System.out.println("Reserva #" + numero);
                        System.out.println("Socio: " + reserva.getSocio().getNombre());
                        System.out.println("Instalación: " + reserva.getInstalacion().getId());
                        System.out.println("Tipo: " + reserva.getInstalacion().getTipo());
                        System.out.println("Fecha: " + reserva.getFecha());
                        System.out.println("Horario: " + reserva.getHoraInicio()
                                        + " - " + reserva.calcularHoraFin());
                        System.out.println("Jugadores: " + reserva.getNumeroJugadores());
                        System.out.println("Modalidad: " + reserva.getModalidad());
                        numero++;
                }
        }

        private void registrarPagoMensualidad() {
                System.out.println();
                System.out.println("--- REGISTRAR PAGO DE MENSUALIDAD ---");

                String id = leerTexto("ID del socio: ");
                Usuario usuario = club.buscarUsuario(id);

                if (!(usuario instanceof Socio)) {
                        System.out.println("No existe un socio con ese ID.");
                        return;
                }

                String periodo = leerTexto("Periodo del pago: ");
                double valor = leerDouble("Valor de la mensualidad: ");

                try {
                        PagoMensualidad pago = new PagoMensualidad(
                                        LocalDate.now(), periodo, valor, (Socio) usuario);

                        club.registrarPagoMensualidad(pago);
                        System.out.println("Pago registrado correctamente.");
                } catch (IllegalArgumentException e) {
                        System.out.println("No fue posible registrar el pago: " + e.getMessage());
                }
        }

        private void verPagosMensualidad() {
                System.out.println();
                System.out.println("--- PAGOS DE MENSUALIDAD ---");

                if (club.getPagosMensualidad().isEmpty()) {
                        System.out.println("No hay pagos registrados.");
                        return;
                }

                int numero = 1;

                for (PagoMensualidad pago : club.getPagosMensualidad()) {
                        System.out.println();
                        System.out.println("Pago #" + numero);
                        System.out.println("Socio: " + pago.getSocio().getNombre());
                        System.out.println("Fecha: " + pago.getFecha());
                        System.out.println("Periodo: " + pago.getPeriodo());
                        System.out.println("Valor: " + pago.getValor());
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

                        System.out.println("El valor no puede estar vacío.");
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
                                System.out.println("Debe escribir un número entero.");
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
                                System.out.println("Debe escribir un número válido.");
                        }
                }

                return 0;
        }

        private boolean leerSiNo(String mensaje) {
                while (ejecutando) {
                        String respuesta = leerTexto(mensaje).toLowerCase();

                        if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                                return true;
                        }

                        if (respuesta.equals("n") || respuesta.equals("no")) {
                                return false;
                        }

                        System.out.println("Responda s o n.");
                }

                return false;
        }
}
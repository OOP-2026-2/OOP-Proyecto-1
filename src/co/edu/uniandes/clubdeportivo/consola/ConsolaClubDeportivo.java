package co.edu.uniandes.clubdeportivo.consola;

import java.time.LocalDate;
import java.util.Scanner;
import java.time.LocalTime;
import java.util.ArrayList;
import co.edu.uniandes.clubdeportivo.ClubDeportivo;
import co.edu.uniandes.clubdeportivo.usuarios.Socio;
import java.time.format.DateTimeParseException;
import co.edu.uniandes.clubdeportivo.usuarios.Usuario;
import co.edu.uniandes.clubdeportivo.inventario.ExistenciaInventario;
import co.edu.uniandes.clubdeportivo.inventario.ArticuloTienda;
import co.edu.uniandes.clubdeportivo.inventario.Producto;
import co.edu.uniandes.clubdeportivo.inventario.UbicacionInventario;
import co.edu.uniandes.clubdeportivo.excepciones.InventarioInsuficienteException;
import co.edu.uniandes.clubdeportivo.ventas.DetalleVenta;
import co.edu.uniandes.clubdeportivo.ventas.VentaTienda;
import co.edu.uniandes.clubdeportivo.deportes.Modalidad;
import co.edu.uniandes.clubdeportivo.excepciones.ReservaNoDisponibleException;
import co.edu.uniandes.clubdeportivo.instalaciones.CanchaPadel;
import co.edu.uniandes.clubdeportivo.instalaciones.Instalacion;
import co.edu.uniandes.clubdeportivo.instalaciones.Reserva;
import co.edu.uniandes.clubdeportivo.ventas.Venta;

public class ConsolaClubDeportivo {
    private ClubDeportivo club;
    private Scanner scanner;
    private boolean ejecutando;
    private ArrayList<Instalacion> instalaciones;

    public ConsolaClubDeportivo() {
        club = new ClubDeportivo();
        scanner = new Scanner(System.in);
        instalaciones = new ArrayList<Instalacion>();
        ejecutando = true;
    }

    public void ejecutar() {
        System.out.println("================================");
        System.out.println("   CLUB DEPORTIVO SÉNECA");
        System.out.println("================================");

        while (ejecutando) {
            mostrarMenu();
            int opcion = leerEntero("Seleccione una opción: ");
            ejecutarOpcion(opcion);
        }

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
            case 0:
                ejecutando = false;
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }

    private void registrarSocio() {
        System.out.println();
        System.out.println("--- REGISTRAR SOCIO ---");
        System.out.print("ID: ");
        String id = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Fecha de nacimiento (AAAA-MM-DD): ");
        String fechaTexto = scanner.nextLine();
        try {
            LocalDate fechaNacimiento = LocalDate.parse(fechaTexto);
            Socio socio = new Socio(id, nombre, fechaNacimiento);
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
        System.out.print("ID del usuario: ");
        String id = scanner.nextLine();
        Usuario usuario = club.buscarUsuario(id);

        if (usuario == null) {
            System.out.println("No existe un usuario con ese ID.");
            return;
        }

        System.out.println("ID: " + usuario.getId());
        System.out.println("Nombre: " + usuario.getNombre());

        if (usuario instanceof Socio) {
            Socio socio = (Socio) usuario;
            System.out.println("Fecha de nacimiento: " + socio.getFechaNacimiento());
            System.out.println("Puntos de fidelidad: " + socio.getPuntosFidelidad());
        }
    }

    private void verInventario() {
        System.out.println();
        System.out.println("--- INVENTARIO ---");
        if (club.getExistencias().isEmpty()) {
            System.out.println("No hay productos registrados en el inventario.");
            return;
        }
        for (ExistenciaInventario existencia : club.getExistencias()) {
            System.out.println("Producto: " + existencia.getProducto().getNombre() + " | Ubicación: "
                    + existencia.getUbicacion().getNombre() + " | Cantidad: " + existencia.getCantidad());
        }
    }

    private void reabastecerProducto() {
        System.out.println();
        System.out.println("--- REABASTECER PRODUCTO ---");

        System.out.print("Nombre del producto: ");
        String nombreProducto = scanner.nextLine();

        Producto producto = buscarProducto(nombreProducto);

        if (producto == null) {
            System.out.println("El producto no existe. Se creará uno nuevo.");

            double precio = leerDouble("Precio: ");

            System.out.print("Categoría: ");
            String categoria = scanner.nextLine();

            producto = new ArticuloTienda(nombreProducto, precio, categoria);
            club.registrarProducto(producto);
        }

        System.out.print("Nombre de la ubicación: ");
        String nombreUbicacion = scanner.nextLine();

        UbicacionInventario ubicacion = buscarUbicacion(nombreUbicacion);

        if (ubicacion == null) {
            ubicacion = new UbicacionInventario(nombreUbicacion);
        }

        int cantidad = leerEntero("Cantidad que desea agregar: ");

        try {
            club.reabastecerProducto(producto, ubicacion, cantidad);
            System.out.println("Inventario actualizado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("No fue posible actualizar el inventario: "
                    + e.getMessage());
        }
    }

    private Producto buscarProducto(String nombre) {
        for (Producto producto : club.getProductos()) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }

        return null;
    }

    private UbicacionInventario buscarUbicacion(String nombre) {
        for (ExistenciaInventario existencia : club.getExistencias()) {
            if (existencia.getUbicacion().getNombre().equalsIgnoreCase(nombre)) {
                return existencia.getUbicacion();
            }
        }

        return null;
    }

    private double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();

            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número válido.");
            }
        }
    }

    private void registrarVenta() {
        System.out.println();
        System.out.println("--- REGISTRAR VENTA ---");
        System.out.print("ID del comprador: ");
        String id = scanner.nextLine();
        Usuario comprador = club.buscarUsuario(id);

        if (comprador == null) {
            System.out.println("No existe un usuario con ese ID.");
            return;
        }

        System.out.print("Nombre del producto: ");
        String nombreProducto = scanner.nextLine();
        Producto producto = buscarProducto(nombreProducto);

        if (producto == null) {
            System.out.println("No existe ese producto.");
            return;
        }

        System.out.print("Nombre de la ubicación: ");
        String nombreUbicacion = scanner.nextLine();
        UbicacionInventario ubicacion = buscarUbicacion(nombreUbicacion);

        if (ubicacion == null) {
            System.out.println("No existe esa ubicación.");
            return;
        }

        int cantidad = leerEntero("Cantidad: ");
        System.out.print("¿Usa código compartido? (s/n): ");
        boolean usaCodigoCompartido = scanner.nextLine().equalsIgnoreCase("s");

        try {
            VentaTienda venta = new VentaTienda(LocalDate.now(), comprador);
            DetalleVenta detalle = new DetalleVenta(producto, cantidad);
            venta.agregarDetalle(detalle);
            venta.finalizarVenta(usaCodigoCompartido);
            club.procesarVenta(venta, ubicacion);
            System.out.println("Venta registrada correctamente.");
            System.out.println("Subtotal: " + venta.getSubtotal());
            System.out.println("Descuento: " + venta.getDescuento());
            System.out.println("Impuesto: " + venta.getImpuesto());
            System.out.println("Total: " + venta.getTotal());
            System.out.println("Puntos generados: " + venta.getPuntosGenerados());
        } catch (InventarioInsuficienteException e) {
            System.out.println("No fue posible realizar la venta: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Datos inválidos: " + e.getMessage());
        }
    }

    private void registrarReserva() {
        System.out.println();
        System.out.println("--- REGISTRAR RESERVA ---");
        System.out.print("ID del socio: ");
        String idSocio = scanner.nextLine();
        Usuario usuario = club.buscarUsuario(idSocio);

        if (!(usuario instanceof Socio)) {
            System.out.println("No existe un socio con ese ID.");
            return;
        }

        Socio socio = (Socio) usuario;

        System.out.print("ID de la cancha: ");
        String idCancha = scanner.nextLine();
        Instalacion instalacion = buscarInstalacion(idCancha);

        if (instalacion == null) {
            System.out.println("La cancha no existe. Se creará una cancha de pádel.");
            int capacidad = leerEntero("Capacidad máxima: ");

            System.out.print("¿Es techada? (s/n): ");
            boolean techada = scanner.nextLine().equalsIgnoreCase("s");

            instalacion = new CanchaPadel(idCancha, capacidad, techada);
            instalaciones.add(instalacion);
        }

        System.out.print("Fecha de la reserva (AAAA-MM-DD): ");
        String fechaTexto = scanner.nextLine();
        System.out.print("Hora de inicio (HH:MM): ");
        String horaTexto = scanner.nextLine();
        int duracion = leerEntero("Duración en minutos: ");
        int numeroJugadores = leerEntero("Número de jugadores: ");
        System.out.print("Modalidad (SENCILLOS/DOBLES): ");
        String modalidadTexto = scanner.nextLine();

        try {
            LocalDate fecha = LocalDate.parse(fechaTexto);
            LocalTime hora = LocalTime.parse(horaTexto);
            Modalidad modalidad = Modalidad.valueOf(modalidadTexto.toUpperCase());

            if (fecha.isBefore(LocalDate.now())) {
                System.out.println("No se pueden registrar reservas en fechas pasadas.");
                return;
            }

            if (numeroJugadores <= 0) {
                System.out.println("El número de jugadores debe ser mayor que cero.");
                return;
            }

            if (modalidad == Modalidad.SENCILLOS && numeroJugadores > 2) {
                System.out.println("La modalidad SENCILLOS permite máximo 2 jugadores.");
                return;
            }

            if (modalidad == Modalidad.DOBLES && numeroJugadores > 4) {
                System.out.println("La modalidad DOBLES permite máximo 4 jugadores.");
                return;
            }

            Reserva reserva = new Reserva(socio, instalacion, fecha, hora, duracion, numeroJugadores, modalidad);
            club.registrarReserva(reserva);
            System.out.println("Reserva registrada correctamente.");
            System.out.println("Horario: " + hora + " - " + reserva.calcularHoraFin());
        } catch (ReservaNoDisponibleException e) {
            System.out.println("No fue posible reservar: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Fecha u hora inválida. Use AAAA-MM-DD y HH:MM.");
        } catch (IllegalArgumentException e) {
            System.out.println("Modalidad inválida. Escriba SENCILLOS o DOBLES.");
        }
    }

    private Instalacion buscarInstalacion(String id) {
        for (Instalacion instalacion : instalaciones) {
            if (instalacion.getId().equalsIgnoreCase(id)) {
                return instalacion;
            }
        }

        return null;
    }

    private void verVentas() {
        System.out.println();
        System.out.println("--- VENTAS REGISTRADAS ---");
        if (club.getVentas().isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }

        int numeroVenta = 1;
        for (Venta venta : club.getVentas()) {
            System.out.println();
            System.out.println("Venta #" + numeroVenta);
            System.out.println("Fecha: " + venta.getFecha());
            System.out.println("Comprador: " + venta.getComprador().getNombre());
            System.out.println("Subtotal: " + venta.getSubtotal());
            System.out.println("Descuento: " + venta.getDescuento());
            System.out.println("Impuesto: " + venta.getImpuesto());
            System.out.println("Total: " + venta.getTotal());
            System.out.println("Puntos generados: " + venta.getPuntosGenerados());
            numeroVenta++;
        }
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número entero.");
            }
        }
    }
}
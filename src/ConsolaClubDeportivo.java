import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import clubdeportivo.ClubDeportivo;
import clubdeportivo.competencias.Competencia;
import clubdeportivo.competencias.InscripcionCompetencia;
import clubdeportivo.competencias.PosicionTabla;
import clubdeportivo.competencias.FormatoCompetencia;
import clubdeportivo.competencias.TipoCompetencia;
import clubdeportivo.competencias.Estadisticas.EstadisticaBaloncesto;
import clubdeportivo.competencias.Estadisticas.EstadisticaFutbol;
import clubdeportivo.competencias.Estadisticas.EstadisticaJugador;
import clubdeportivo.competencias.Estadisticas.EstadisticaTenisMesa;
import clubdeportivo.competencias.Estadisticas.EstadisticaTenisPadel;
import clubdeportivo.competencias.Estadisticas.EstadisticaVoleibol;
import clubdeportivo.competencias.Partidos.Partido;
import clubdeportivo.competencias.Partidos.PartidoInterno;
import clubdeportivo.competencias.Partidos.PartidoOficial;
import clubdeportivo.competencias.Partidos.PartidoPractica;
import clubdeportivo.competencias.Partidos.PropositoPartido;
import clubdeportivo.deportes.Categoria;
import clubdeportivo.deportes.DeporteConjunto;
import clubdeportivo.deportes.DeporteIndividual;
import clubdeportivo.deportes.Equipo;
import clubdeportivo.deportes.InscripcionAcademia;
import clubdeportivo.deportes.InscripcionEquipo;
import clubdeportivo.deportes.Modalidad;
import clubdeportivo.deportes.Nivel;
import clubdeportivo.deportes.Posicion;
import clubdeportivo.entrenamientos.EstadoAsistencia;
import clubdeportivo.entrenamientos.RegistroAsistencia;
import clubdeportivo.entrenamientos.SesionEntrenamiento;
import clubdeportivo.entrenamientos.TipoSesion;
import clubdeportivo.instalaciones.Instalacion;
import clubdeportivo.instalaciones.Reserva;
import clubdeportivo.instalaciones.TipoInstalacion;
import clubdeportivo.membresias.PagoMensualidad;
import clubdeportivo.turnos.DiaSemana;
import clubdeportivo.turnos.SolicitudCambioTurno;
import clubdeportivo.turnos.Turno;
import tiendaclub.inventario.ArticuloTienda;
import tiendaclub.inventario.ExistenciaInventario;
import tiendaclub.inventario.MovimientoInventario;
import tiendaclub.inventario.Producto;
import tiendaclub.inventario.SugerenciaProducto;
import tiendaclub.inventario.UbicacionInventario;
import tiendaclub.ventas.Venta;
import tiendaclub.ventas.tipos.VentaTienda;
import usuarios.Comprador;
import usuarios.Usuario;
import usuarios.tipos.Empleado;
import usuarios.tipos.Socio;
import usuarios.tipos.empleados.Administrador;
import usuarios.tipos.empleados.Entrenador;
import usuarios.tipos.empleados.Fisioterapeuta;
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
                System.out.println("12. Registrar deporte de conjunto");
                System.out.println("13. Registrar deporte individual");
                System.out.println("14. Registrar equipo");
                System.out.println("15. Inscribir socio en equipo");
                System.out.println("16. Inscribir socio en academia");
                System.out.println("17. Ver deportes y equipos");
                System.out.println("18. Ver inscripciones deportivas");
                System.out.println("19. Registrar competencia");
                System.out.println("20. Inscribir equipo en competencia");
                System.out.println("21. Programar partido oficial");
                System.out.println("22. Programar partido interno");
                System.out.println("23. Programar partido de práctica");
                System.out.println("24. Registrar resultado de partido");
                System.out.println("25. Ver competencias y partidos");
                System.out.println("26. Programar sesión de entrenamiento");
                System.out.println("27. Registrar socio en una sesión");
                System.out.println("28. Registrar asistencia");
                System.out.println("29. Registrar evaluación");
                System.out.println("30. Ver sesiones y asistencias");
                System.out.println("31. Registrar entrenador");
                System.out.println("32. Registrar administrador");
                System.out.println("33. Registrar fisioterapeuta");
                System.out.println("34. Asignar turno a empleado");
                System.out.println("35. Solicitar cambio de turno");
                System.out.println("36. Decidir solicitud de cambio");
                System.out.println("37. Ver turnos y solicitudes");
                System.out.println("38. Registrar sugerencia de producto");
                System.out.println("39. Ver sugerencias de productos");
                System.out.println("40. Decidir sugerencia de producto");
                System.out.println("41. Registrar estadística de jugador");
                System.out.println("42. Ver estadísticas de partidos");
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
                        case 12:
                                registrarDeporteConjunto();
                                break;
                        case 13:
                                registrarDeporteIndividual();
                                break;
                        case 14:
                                registrarEquipo();
                                break;
                        case 15:
                                inscribirSocioEnEquipo();
                                break;
                        case 16:
                                inscribirSocioEnAcademia();
                                break;
                        case 17:
                                verDeportesYEquipos();
                                break;
                        case 18:
                                verInscripcionesDeportivas();
                                break;
                        case 19:
                                registrarCompetencia();
                                break;
                        case 20:
                                inscribirEquipoEnCompetencia();
                                break;
                        case 21:
                                programarPartidoOficial();
                                break;
                        case 22:
                                programarPartidoInterno();
                                break;
                        case 23:
                                programarPartidoPractica();
                                break;
                        case 24:
                                registrarResultadoPartido();
                                break;
                        case 25:
                                verCompetenciasYPartidos();
                                break;
                        case 26:
                                programarSesionEntrenamiento();
                                break;
                        case 27:
                                registrarSocioEnSesion();
                                break;
                        case 28:
                                registrarAsistencia();
                                break;
                        case 29:
                                registrarEvaluacion();
                                break;
                        case 30:
                                verSesionesYAsistencias();
                                break;
                        case 31:
                                registrarEntrenador();
                                break;
                        case 32:
                                registrarAdministrador();
                                break;
                        case 33:
                                registrarFisioterapeuta();
                                break;
                        case 34:
                                asignarTurnoEmpleado();
                                break;
                        case 35:
                                solicitarCambioTurno();
                                break;
                        case 36:
                                decidirSolicitudCambio();
                                break;
                        case 37:
                                verTurnosYSolicitudes();
                                break;
                        case 38:
                                registrarSugerenciaProducto();
                                break;
                        case 39:
                                verSugerenciasProducto();
                                break;
                        case 40:
                                decidirSugerenciaProducto();
                                break;
                        case 41:
                                registrarEstadisticaJugador();
                                break;
                        case 42:
                                verEstadisticasPartidos();
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

                                DeporteIndividual disciplina = null;

                                if (tipo != TipoInstalacion.CANCHA_CONJUNTO) {
                                        String nombreDisciplina = leerTexto(
                                                        "Deporte individual de la instalación: ");

                                        disciplina = club.buscarDeporteIndividual(nombreDisciplina);
                                }

                                instalacion = new Instalacion(idInstalacion, tipo, capacidad, techada,
                                                disciplina);
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

        private void registrarDeporteConjunto() {
                System.out.println();
                System.out.println("--- REGISTRAR DEPORTE DE CONJUNTO ---");

                String nombre = leerTexto("Nombre del deporte: ");
                int minimoJugadores = leerEntero("Mínimo de jugadores: ");
                int puntosVictoria = leerEntero("Puntos por victoria: ");
                int puntosEmpate = leerEntero("Puntos por empate: ");
                boolean permiteEmpate = leerSiNo("¿Permite empates? (s/n): ");
                int cantidadPosiciones = leerEntero("Cantidad de posiciones: ");

                try {
                        DeporteConjunto deporte = new DeporteConjunto(
                                        nombre, minimoJugadores, puntosVictoria,
                                        puntosEmpate, permiteEmpate);

                        for (int i = 1; i <= cantidadPosiciones; i++) {
                                String nombrePosicion = leerTexto(
                                                "Posición #" + i + ": ");

                                deporte.agregarPosicion(
                                                new Posicion(nombrePosicion));
                        }

                        club.registrarDeporteConjunto(deporte);

                        System.out.println(
                                        "Deporte de conjunto registrado correctamente.");
                } catch (IllegalArgumentException e) {
                        System.out.println(
                                        "No fue posible registrar el deporte: "
                                                        + e.getMessage());
                }
        }

        private void registrarDeporteIndividual() {
                System.out.println();
                System.out.println("--- REGISTRAR DEPORTE INDIVIDUAL ---");

                String nombre = leerTexto("Nombre del deporte: ");
                int maximoInstalaciones = leerEntero(
                                "Máximo de instalaciones reservadas al mismo tiempo: ");

                try {
                        DeporteIndividual deporte = new DeporteIndividual(
                                        nombre,
                                        maximoInstalaciones);

                        club.registrarDeporteIndividual(deporte);

                        System.out.println(
                                        "Deporte individual registrado correctamente.");
                } catch (IllegalArgumentException e) {
                        System.out.println(
                                        "No fue posible registrar el deporte: "
                                                        + e.getMessage());
                }
        }

        private void registrarEquipo() {
                System.out.println();
                System.out.println("--- REGISTRAR EQUIPO ---");

                String nombreDeporte = leerTexto(
                                "Nombre del deporte de conjunto: ");

                DeporteConjunto deporte = club.buscarDeporteConjunto(nombreDeporte);

                if (deporte == null) {
                        System.out.println(
                                        "No existe ese deporte de conjunto.");
                        return;
                }

                String nombreEquipo = leerTexto(
                                "Nombre del equipo: ");

                System.out.println("Categorías disponibles:");

                for (Categoria categoria : Categoria.values()) {
                        System.out.println("- " + categoria);
                }

                String categoriaTexto = leerTexto(
                                "Categoría: ").toUpperCase();

                int cupoMaximo = leerEntero(
                                "Cupo máximo: ");

                try {
                        Categoria categoria = Categoria.valueOf(categoriaTexto);

                        Equipo equipo = new Equipo(
                                        nombreEquipo,
                                        categoria,
                                        cupoMaximo,
                                        deporte);

                        club.registrarEquipo(equipo);

                        System.out.println(
                                        "Equipo registrado correctamente.");
                } catch (IllegalArgumentException e) {
                        System.out.println(
                                        "No fue posible registrar el equipo: "
                                                        + e.getMessage());
                }
        }

        private void inscribirSocioEnEquipo() {
                System.out.println();
                System.out.println("--- INSCRIBIR SOCIO EN EQUIPO ---");

                String idSocio = leerTexto("ID del socio: ");
                Usuario usuario = club.buscarUsuario(idSocio);

                if (!(usuario instanceof Socio)) {
                        System.out.println(
                                        "No existe un socio con ese ID.");
                        return;
                }

                String nombreEquipo = leerTexto(
                                "Nombre del equipo: ");

                Equipo equipo = club.buscarEquipo(nombreEquipo);

                if (equipo == null) {
                        System.out.println(
                                        "No existe un equipo con ese nombre.");
                        return;
                }

                System.out.println("Posiciones disponibles:");

                for (Posicion posicion : equipo.getDeporte().getPosiciones()) {

                        System.out.println("- " + posicion.getNombre());
                }

                String nombrePosicion = leerTexto("Posición: ");
                Posicion posicionSeleccionada = null;

                for (Posicion posicion : equipo.getDeporte().getPosiciones()) {

                        if (posicion.getNombre().equalsIgnoreCase(
                                        nombrePosicion)) {

                                posicionSeleccionada = posicion;
                                break;
                        }
                }

                if (posicionSeleccionada == null) {
                        System.out.println(
                                        "La posición no pertenece a este deporte.");
                        return;
                }

                int numeroCamiseta = leerEntero(
                                "Número de camiseta: ");

                try {
                        InscripcionEquipo inscripcion = club.inscribirSocioEnEquipo(
                                        (Socio) usuario,
                                        equipo,
                                        posicionSeleccionada,
                                        numeroCamiseta);

                        System.out.println(
                                        "Inscripción registrada correctamente.");

                        System.out.println(inscripcion);
                } catch (IllegalArgumentException
                                | IllegalStateException e) {

                        System.out.println(
                                        "No fue posible realizar la inscripción: "
                                                        + e.getMessage());
                }
        }

        private void inscribirSocioEnAcademia() {
                System.out.println();
                System.out.println("--- INSCRIBIR SOCIO EN ACADEMIA ---");

                String idSocio = leerTexto("ID del socio: ");
                Usuario usuario = club.buscarUsuario(idSocio);

                if (!(usuario instanceof Socio)) {
                        System.out.println(
                                        "No existe un socio con ese ID.");
                        return;
                }

                String nombreDeporte = leerTexto(
                                "Nombre del deporte individual: ");

                DeporteIndividual deporte = club.buscarDeporteIndividual(nombreDeporte);

                if (deporte == null) {
                        System.out.println(
                                        "No existe ese deporte individual.");
                        return;
                }

                System.out.println("Niveles disponibles:");

                for (Nivel nivel : Nivel.values()) {
                        System.out.println("- " + nivel);
                }

                String nivelTexto = leerTexto(
                                "Nivel: ").toUpperCase();

                try {
                        Nivel nivel = Nivel.valueOf(nivelTexto);

                        InscripcionAcademia inscripcion = club.inscribirSocioEnAcademia(
                                        (Socio) usuario,
                                        deporte,
                                        nivel);

                        System.out.println(
                                        "Inscripción registrada correctamente.");

                        System.out.println(inscripcion);
                } catch (IllegalArgumentException
                                | IllegalStateException e) {

                        System.out.println(
                                        "No fue posible realizar la inscripción: "
                                                        + e.getMessage());
                }
        }

        private void verDeportesYEquipos() {
                System.out.println();
                System.out.println("--- DEPORTES Y EQUIPOS ---");

                if (club.getDeportesConjunto().isEmpty()
                                && club.getDeportesIndividuales().isEmpty()) {

                        System.out.println(
                                        "No hay deportes registrados.");
                        return;
                }

                System.out.println("Deportes de conjunto:");

                for (DeporteConjunto deporte : club.getDeportesConjunto()) {

                        System.out.println();
                        System.out.println(
                                        deporte.getNombre()
                                                        + " | mínimo de jugadores: "
                                                        + deporte.getMinimoJugadores());

                        System.out.println(
                                        "Posiciones: "
                                                        + deporte.getPosiciones());

                        if (deporte.getEquipos().isEmpty()) {
                                System.out.println(
                                                "No tiene equipos registrados.");
                        } else {
                                for (Equipo equipo : deporte.getEquipos()) {

                                        System.out.println(
                                                        "- " + equipo
                                                                        + " | cupo: "
                                                                        + equipo.getCupoMaximo()
                                                                        + " | activo: "
                                                                        + equipo.isActivo());
                                }
                        }
                }

                System.out.println();
                System.out.println("Deportes individuales:");

                for (DeporteIndividual deporte : club.getDeportesIndividuales()) {

                        System.out.println(
                                        "- " + deporte.getNombre()
                                                        + " | máximo de instalaciones: "
                                                        + deporte.getMaximoInstalaciones());
                }
        }

        private void verInscripcionesDeportivas() {
                System.out.println();
                System.out.println("--- INSCRIPCIONES DEPORTIVAS ---");

                boolean hayInscripciones = false;

                for (DeporteConjunto deporte : club.getDeportesConjunto()) {

                        for (Equipo equipo : deporte.getEquipos()) {
                                for (InscripcionEquipo inscripcion : equipo.getInscripciones()) {

                                        hayInscripciones = true;
                                        System.out.println(
                                                        "Equipo: " + inscripcion);
                                }
                        }
                }

                for (DeporteIndividual deporte : club.getDeportesIndividuales()) {

                        for (InscripcionAcademia inscripcion : deporte.getInscripciones()) {

                                hayInscripciones = true;
                                System.out.println(
                                                "Academia: " + inscripcion);
                        }
                }

                if (!hayInscripciones) {
                        System.out.println(
                                        "No hay inscripciones deportivas registradas.");
                }
        }

        private void registrarCompetencia() {
                System.out.println();
                System.out.println("--- REGISTRAR COMPETENCIA ---");

                String nombreDeporte = leerTexto(
                                "Nombre del deporte de conjunto: ");

                DeporteConjunto deporte = club.buscarDeporteConjunto(
                                nombreDeporte);

                if (deporte == null) {
                        System.out.println(
                                        "No existe ese deporte de conjunto.");
                        return;
                }

                String nombre = leerTexto("Nombre de la competencia: ");
                String organizador = leerTexto("Organizador: ");
                int anio = leerEntero("Año: ");

                System.out.println("Categorías disponibles:");
                for (Categoria categoria : Categoria.values()) {
                        System.out.println("- " + categoria);
                }

                String categoriaTexto = leerTexto(
                                "Categoría: ").toUpperCase();

                System.out.println("Tipos disponibles:");
                for (TipoCompetencia tipo : TipoCompetencia.values()) {
                        System.out.println("- " + tipo);
                }

                String tipoTexto = leerTexto(
                                "Tipo: ").toUpperCase();

                System.out.println("Formatos disponibles:");
                for (FormatoCompetencia formato : FormatoCompetencia.values()) {
                        System.out.println("- " + formato);
                }

                String formatoTexto = leerTexto(
                                "Formato: ").toUpperCase();

                try {
                        Categoria categoria = Categoria.valueOf(
                                        categoriaTexto);

                        TipoCompetencia tipo = TipoCompetencia.valueOf(
                                        tipoTexto);

                        FormatoCompetencia formato = FormatoCompetencia.valueOf(
                                        formatoTexto);

                        Competencia competencia = new Competencia(
                                        nombre, organizador, anio,
                                        categoria, tipo, formato, deporte);

                        club.registrarCompetencia(competencia);

                        System.out.println(
                                        "Competencia registrada correctamente.");
                } catch (IllegalArgumentException e) {
                        System.out.println(
                                        "No fue posible registrar la competencia: "
                                                        + e.getMessage());
                }
        }

        private void inscribirEquipoEnCompetencia() {
                System.out.println();
                System.out.println(
                                "--- INSCRIBIR EQUIPO EN COMPETENCIA ---");

                Competencia competencia = seleccionarCompetencia();

                if (competencia == null) {
                        return;
                }

                String nombreEquipo = leerTexto(
                                "Nombre del equipo: ");

                Equipo equipo = club.buscarEquipo(nombreEquipo);

                if (equipo == null) {
                        System.out.println(
                                        "No existe un equipo con ese nombre.");
                        return;
                }

                try {
                        InscripcionCompetencia inscripcion = competencia.inscribirEquipo(equipo);

                        System.out.println(
                                        "Equipo inscrito correctamente.");

                        String idJugador = leerTexto(
                                        "ID de un jugador para la nómina (0 para terminar): ");

                        while (!idJugador.equals("0")) {
                                Usuario jugador = club.buscarUsuario(idJugador);

                                if (jugador instanceof Socio) {
                                        inscripcion.agregarJugadorNomina((Socio) jugador);
                                        System.out.println("Jugador agregado a la nómina.");
                                } else {
                                        System.out.println("No existe un socio con ese ID.");
                                }

                                idJugador = leerTexto(
                                                "ID de otro jugador para la nómina (0 para terminar): ");
                        }
                } catch (IllegalArgumentException
                                | IllegalStateException e) {

                        System.out.println(
                                        "No fue posible inscribir el equipo: "
                                                        + e.getMessage());
                }
        }

        private void programarPartidoOficial() {
                System.out.println();
                System.out.println("--- PROGRAMAR PARTIDO OFICIAL ---");

                Competencia competencia = seleccionarCompetencia();

                if (competencia == null) {
                        return;
                }

                String nombreEquipo = leerTexto(
                                "Nombre del equipo: ");

                Equipo equipo = club.buscarEquipo(nombreEquipo);

                if (equipo == null) {
                        System.out.println(
                                        "No existe un equipo con ese nombre.");
                        return;
                }

                String fechaTexto = leerTexto(
                                "Fecha del partido (AAAA-MM-DD): ");

                String institucionRival = leerTexto(
                                "Institución rival: ");

                String lugar = leerTexto("Lugar: ");

                try {
                        PartidoOficial partido = new PartidoOficial(
                                        LocalDate.parse(fechaTexto),
                                        equipo,
                                        competencia,
                                        institucionRival,
                                        lugar);

                        club.registrarPartido(partido);

                        System.out.println(
                                        "Partido oficial programado correctamente.");
                } catch (DateTimeParseException e) {
                        System.out.println(
                                        "Fecha inválida. Use AAAA-MM-DD.");
                } catch (IllegalArgumentException
                                | IllegalStateException e) {

                        System.out.println(
                                        "No fue posible programar el partido: "
                                                        + e.getMessage());
                }
        }

        private void programarPartidoInterno() {
                System.out.println();
                System.out.println("--- PROGRAMAR PARTIDO INTERNO ---");

                String nombreLocal = leerTexto(
                                "Nombre del equipo local: ");

                String nombreVisitante = leerTexto(
                                "Nombre del equipo visitante: ");

                Equipo equipoLocal = club.buscarEquipo(nombreLocal);
                Equipo equipoVisitante = club.buscarEquipo(nombreVisitante);

                if (equipoLocal == null
                                || equipoVisitante == null) {

                        System.out.println(
                                        "Uno de los equipos no está registrado.");
                        return;
                }

                String fechaTexto = leerTexto(
                                "Fecha del partido (AAAA-MM-DD): ");

                System.out.println("Propósitos disponibles:");
                for (PropositoPartido proposito : PropositoPartido.values()) {
                        System.out.println("- " + proposito);
                }

                String propositoTexto = leerTexto(
                                "Propósito: ").toUpperCase();

                try {
                        PartidoInterno partido = new PartidoInterno(
                                        LocalDate.parse(fechaTexto),
                                        equipoLocal,
                                        equipoVisitante,
                                        PropositoPartido.valueOf(
                                                        propositoTexto));

                        club.registrarPartido(partido);

                        System.out.println(
                                        "Partido interno programado correctamente.");
                } catch (DateTimeParseException e) {
                        System.out.println(
                                        "Fecha inválida. Use AAAA-MM-DD.");
                } catch (IllegalArgumentException
                                | IllegalStateException e) {

                        System.out.println(
                                        "No fue posible programar el partido: "
                                                        + e.getMessage());
                }
        }

        private void programarPartidoPractica() {
                System.out.println();
                System.out.println("--- PROGRAMAR PARTIDO DE PRÁCTICA ---");

                String nombreDeporte = leerTexto(
                                "Nombre del deporte individual: ");

                DeporteIndividual deporte = club.buscarDeporteIndividual(
                                nombreDeporte);

                if (deporte == null) {
                        System.out.println(
                                        "No existe ese deporte individual.");
                        return;
                }

                String fechaTexto = leerTexto(
                                "Fecha del partido (AAAA-MM-DD): ");

                String modalidadTexto = leerTexto(
                                "Modalidad (SENCILLOS/DOBLES): ")
                                .toUpperCase();

                try {
                        Modalidad modalidad = Modalidad.valueOf(
                                        modalidadTexto);

                        PartidoPractica partido = new PartidoPractica(
                                        LocalDate.parse(fechaTexto),
                                        deporte,
                                        modalidad);

                        int cantidadParticipantes = modalidad == Modalidad.SENCILLOS
                                        ? 2
                                        : 4;

                        for (int i = 1; i <= cantidadParticipantes; i++) {
                                String idSocio = leerTexto(
                                                "ID del participante #" + i + ": ");

                                Usuario usuario = club.buscarUsuario(idSocio);

                                if (!(usuario instanceof Socio)) {
                                        throw new IllegalArgumentException(
                                                        "No existe el socio " + idSocio);
                                }

                                partido.agregarParticipante(
                                                (Socio) usuario);
                        }

                        club.registrarPartido(partido);

                        System.out.println(
                                        "Partido de práctica programado correctamente.");
                } catch (DateTimeParseException e) {
                        System.out.println(
                                        "Fecha inválida. Use AAAA-MM-DD.");
                } catch (IllegalArgumentException
                                | IllegalStateException e) {

                        System.out.println(
                                        "No fue posible programar el partido: "
                                                        + e.getMessage());
                }
        }

        private void registrarResultadoPartido() {
                System.out.println();
                System.out.println("--- REGISTRAR RESULTADO ---");

                Partido partido = seleccionarPartido();

                if (partido == null) {
                        return;
                }

                String marcador = leerTexto("Marcador final: ");

                try {
                        partido.registrarResultado(marcador);
                        partido.finalizar();

                        System.out.println(
                                        "Resultado registrado y partido finalizado.");
                } catch (IllegalArgumentException
                                | IllegalStateException e) {

                        System.out.println(
                                        "No fue posible registrar el resultado: "
                                                        + e.getMessage());
                }
        }

        private void verCompetenciasYPartidos() {
                System.out.println();
                System.out.println("--- COMPETENCIAS Y PARTIDOS ---");

                if (club.getCompetencias().isEmpty()) {
                        System.out.println(
                                        "No hay competencias registradas.");
                } else {
                        System.out.println("Competencias:");

                        for (Competencia competencia : club.getCompetencias()) {
                                System.out.println(
                                                "- " + competencia
                                                                + " | deporte: "
                                                                + competencia.getDeporte().getNombre()
                                                                + " | categoría: "
                                                                + competencia.getCategoria()
                                                                + " | equipos inscritos: "
                                                                + competencia.getInscripciones().size());

                                for (PosicionTabla posicion : competencia.obtenerTablaPosiciones()) {
                                        System.out.println("    " + posicion);
                                }
                        }
                }

                if (club.getPartidos().isEmpty()) {
                        System.out.println(
                                        "No hay partidos registrados.");
                        return;
                }

                System.out.println();
                System.out.println("Partidos:");

                int numero = 1;

                for (Partido partido : club.getPartidos()) {
                        System.out.println(
                                        numero + ". "
                                                        + describirPartido(partido)
                                                        + " | fecha: "
                                                        + partido.getFecha()
                                                        + " | estado: "
                                                        + partido.getEstado()
                                                        + " | marcador: "
                                                        + (partido.getMarcadorFinal() == null
                                                                        ? "Sin resultado"
                                                                        : partido.getMarcadorFinal()));

                        numero++;
                }
        }

        private Competencia seleccionarCompetencia() {
                if (club.getCompetencias().isEmpty()) {
                        System.out.println(
                                        "No hay competencias registradas.");
                        return null;
                }

                System.out.println("Competencias disponibles:");

                int numero = 1;
                for (Competencia competencia : club.getCompetencias()) {
                        System.out.println(numero + ". " + competencia);
                        numero++;
                }

                int seleccion = leerEntero(
                                "Número de la competencia: ");

                if (seleccion < 1
                                || seleccion > club.getCompetencias().size()) {

                        System.out.println(
                                        "La competencia seleccionada no es válida.");
                        return null;
                }

                return club.getCompetencias().get(
                                seleccion - 1);
        }

        private Partido seleccionarPartido() {
                if (club.getPartidos().isEmpty()) {
                        System.out.println(
                                        "No hay partidos registrados.");
                        return null;
                }

                System.out.println("Partidos disponibles:");

                int numero = 1;
                for (Partido partido : club.getPartidos()) {
                        System.out.println(
                                        numero + ". "
                                                        + describirPartido(partido)
                                                        + " | "
                                                        + partido.getFecha()
                                                        + " | "
                                                        + partido.getEstado());

                        numero++;
                }

                int seleccion = leerEntero(
                                "Número del partido: ");

                if (seleccion < 1
                                || seleccion > club.getPartidos().size()) {

                        System.out.println(
                                        "El partido seleccionado no es válido.");
                        return null;
                }

                return club.getPartidos().get(
                                seleccion - 1);
        }

        private String describirPartido(Partido partido) {
                if (partido instanceof PartidoOficial) {
                        PartidoOficial oficial = (PartidoOficial) partido;

                        return "Oficial: "
                                        + oficial.getEquipo().getNombre()
                                        + " contra "
                                        + oficial.getInstitucionRival();
                }

                if (partido instanceof PartidoInterno) {
                        PartidoInterno interno = (PartidoInterno) partido;

                        return "Interno: "
                                        + interno.getEquipoLocal().getNombre()
                                        + " contra "
                                        + interno.getEquipoVisitante().getNombre();
                }

                if (partido instanceof PartidoPractica) {
                        PartidoPractica practica = (PartidoPractica) partido;

                        return "Práctica: "
                                        + practica.getDeporte().getNombre()
                                        + " - "
                                        + practica.getModalidad();
                }

                return partido.getClass().getSimpleName();
        }

        private void programarSesionEntrenamiento() {
                System.out.println();
                System.out.println(
                                "--- PROGRAMAR SESIÓN DE ENTRENAMIENTO ---");

                String idEntrenador = leerTexto(
                                "ID del entrenador: ");

                Usuario usuario = club.buscarUsuario(idEntrenador);

                if (!(usuario instanceof Entrenador)) {
                        System.out.println(
                                        "No existe un entrenador con ese ID.");
                        return;
                }

                String idInstalacion = leerTexto(
                                "ID de la instalación: ");

                Instalacion instalacion = club.buscarInstalacion(
                                idInstalacion);

                if (instalacion == null) {
                        System.out.println(
                                        "No existe una instalación con ese ID.");
                        return;
                }

                String fechaTexto = leerTexto(
                                "Fecha de la sesión (AAAA-MM-DD): ");

                String horaTexto = leerTexto(
                                "Hora de inicio (HH:MM): ");

                int duracion = leerEntero(
                                "Duración en minutos: ");

                System.out.println("Tipos de sesión disponibles:");
                for (TipoSesion tipo : TipoSesion.values()) {
                        System.out.println("- " + tipo);
                }

                String tipoTexto = leerTexto(
                                "Tipo de sesión: ").toUpperCase();

                try {
                        TipoSesion tipo = TipoSesion.valueOf(
                                        tipoTexto);

                        SesionEntrenamiento sesion = new SesionEntrenamiento(
                                        LocalDate.parse(fechaTexto),
                                        LocalTime.parse(horaTexto),
                                        duracion,
                                        (Entrenador) usuario,
                                        instalacion,
                                        tipo);

                        if (tipo == TipoSesion.GRUPAL) {
                                String nombreEquipo = leerTexto(
                                                "Nombre del equipo: ");

                                Equipo equipo = club.buscarEquipo(
                                                nombreEquipo);

                                if (equipo == null) {
                                        System.out.println(
                                                        "No existe ese equipo.");
                                        return;
                                }

                                sesion.asignarEquipo(equipo);
                        } else {
                                String nombreDeporte = leerTexto(
                                                "Nombre del deporte individual: ");

                                DeporteIndividual deporte = club
                                                .buscarDeporteIndividual(
                                                                nombreDeporte);

                                if (deporte == null) {
                                        System.out.println(
                                                        "No existe ese deporte individual.");
                                        return;
                                }

                                sesion.asignarDeporteIndividual(
                                                deporte);
                        }

                        club.programarSesion(sesion);

                        System.out.println(
                                        "Sesión programada correctamente.");
                } catch (DateTimeParseException e) {
                        System.out.println(
                                        "Fecha u hora inválida. Use AAAA-MM-DD y HH:MM.");
                } catch (IllegalArgumentException
                                | IllegalStateException e) {

                        System.out.println(
                                        "No fue posible programar la sesión: "
                                                        + e.getMessage());
                }
        }

        private void registrarSocioEnSesion() {
                System.out.println();
                System.out.println(
                                "--- REGISTRAR SOCIO EN UNA SESIÓN ---");

                SesionEntrenamiento sesion = seleccionarSesion();

                if (sesion == null) {
                        return;
                }

                String idSocio = leerTexto("ID del socio: ");
                Usuario usuario = club.buscarUsuario(idSocio);

                if (!(usuario instanceof Socio)) {
                        System.out.println(
                                        "No existe un socio con ese ID.");
                        return;
                }

                try {
                        RegistroAsistencia registro = club.registrarAsistencia(
                                        sesion,
                                        (Socio) usuario);

                        System.out.println(
                                        "Socio registrado en la sesión.");
                        System.out.println(registro);
                } catch (IllegalArgumentException
                                | IllegalStateException e) {

                        System.out.println(
                                        "No fue posible registrar al socio: "
                                                        + e.getMessage());
                }
        }

        private void registrarAsistencia() {
                System.out.println();
                System.out.println("--- REGISTRAR ASISTENCIA ---");

                RegistroAsistencia registro = seleccionarRegistro();

                if (registro == null) {
                        return;
                }

                System.out.println("Estados disponibles:");
                System.out.println("- ASISTIO");
                System.out.println("- AUSENTE");
                System.out.println("- TARDE");

                String estadoTexto = leerTexto(
                                "Estado: ").toUpperCase();

                try {
                        EstadoAsistencia estado = EstadoAsistencia.valueOf(
                                        estadoTexto);

                        if (estado == EstadoAsistencia.PENDIENTE) {
                                throw new IllegalArgumentException(
                                                "Debe registrar un estado definitivo");
                        }

                        registro.registrarAsistencia(estado);

                        System.out.println(
                                        "Asistencia actualizada correctamente.");
                } catch (IllegalArgumentException e) {
                        System.out.println(
                                        "No fue posible registrar la asistencia: "
                                                        + e.getMessage());
                }
        }

        private void registrarEvaluacion() {
                System.out.println();
                System.out.println("--- REGISTRAR EVALUACIÓN ---");

                RegistroAsistencia registro = seleccionarRegistro();

                if (registro == null) {
                        return;
                }

                double nota = leerDouble(
                                "Nota de desempeño (1 a 5): ");

                String comentario = leerTexto("Comentario: ");

                try {
                        registro.registrarEvaluacion(
                                        nota,
                                        comentario);

                        System.out.println(
                                        "Evaluación registrada correctamente.");
                } catch (IllegalArgumentException
                                | IllegalStateException e) {

                        System.out.println(
                                        "No fue posible registrar la evaluación: "
                                                        + e.getMessage());
                }
        }

        private void verSesionesYAsistencias() {
                System.out.println();
                System.out.println("--- SESIONES Y ASISTENCIAS ---");

                if (club.getSesionesEntrenamiento().isEmpty()) {
                        System.out.println(
                                        "No hay sesiones registradas.");
                        return;
                }

                int numero = 1;

                for (SesionEntrenamiento sesion : club.getSesionesEntrenamiento()) {

                        System.out.println();
                        System.out.println(
                                        "Sesión #" + numero + ": " + sesion);

                        System.out.println(
                                        "Entrenador: "
                                                        + sesion.getEntrenador().getNombre());

                        if (sesion.getEquipo() != null) {
                                System.out.println(
                                                "Equipo: "
                                                                + sesion.getEquipo().getNombre());
                        } else {
                                System.out.println(
                                                "Deporte: "
                                                                + sesion.getDeporteIndividual()
                                                                                .getNombre());
                        }

                        if (sesion.getRegistros().isEmpty()) {
                                System.out.println(
                                                "No tiene asistencias registradas.");
                        } else {
                                for (RegistroAsistencia registro : sesion.getRegistros()) {

                                        System.out.println(
                                                        "- " + registro);
                                }
                        }

                        numero++;
                }
        }

        private SesionEntrenamiento seleccionarSesion() {
                if (club.getSesionesEntrenamiento().isEmpty()) {
                        System.out.println(
                                        "No hay sesiones registradas.");
                        return null;
                }

                System.out.println("Sesiones disponibles:");

                int numero = 1;
                for (SesionEntrenamiento sesion : club.getSesionesEntrenamiento()) {

                        System.out.println(numero + ". " + sesion);
                        numero++;
                }

                int seleccion = leerEntero(
                                "Número de la sesión: ");

                if (seleccion < 1
                                || seleccion > club.getSesionesEntrenamiento().size()) {

                        System.out.println(
                                        "La sesión seleccionada no es válida.");
                        return null;
                }

                return club.getSesionesEntrenamiento().get(
                                seleccion - 1);
        }

        private RegistroAsistencia seleccionarRegistro() {
                SesionEntrenamiento sesion = seleccionarSesion();

                if (sesion == null) {
                        return null;
                }

                String idSocio = leerTexto("ID del socio: ");

                for (RegistroAsistencia registro : sesion.getRegistros()) {

                        if (registro.getSocio().getId()
                                        .equalsIgnoreCase(idSocio)) {

                                return registro;
                        }
                }

                System.out.println(
                                "El socio no está registrado en esa sesión.");
                return null;
        }

        private void registrarEntrenador() {
                registrarEmpleado("ENTRENADOR");
        }

        private void registrarAdministrador() {
                registrarEmpleado("ADMINISTRADOR");
        }

        private void registrarFisioterapeuta() {
                registrarEmpleado("FISIOTERAPEUTA");
        }

        private void registrarEmpleado(String tipoEmpleado) {
                System.out.println();
                System.out.println("--- REGISTRAR " + tipoEmpleado + " ---");

                String id = leerTexto("ID: ");
                String nombre = leerTexto("Nombre: ");
                String fechaTexto = leerTexto(
                                "Fecha de nacimiento (AAAA-MM-DD): ");
                String login = leerTexto("Login: ");
                String contrasena = leerTexto("Contraseña: ");
                String codigoDescuento = "";

                if (!tipoEmpleado.equals("ADMINISTRADOR")) {
                        codigoDescuento = leerTexto("Código de descuento: ");
                }

                try {
                        LocalDate fechaNacimiento = LocalDate.parse(
                                        fechaTexto);

                        Usuario empleado;

                        if (tipoEmpleado.equals("ENTRENADOR")) {
                                empleado = new Entrenador(
                                                id, nombre, fechaNacimiento,
                                                login, contrasena, codigoDescuento);
                        } else if (tipoEmpleado.equals("ADMINISTRADOR")) {
                                empleado = new Administrador(
                                                id, nombre, fechaNacimiento,
                                                login, contrasena);
                        } else {
                                empleado = new Fisioterapeuta(
                                                id, nombre, fechaNacimiento,
                                                login, contrasena, codigoDescuento);
                        }

                        club.registrarUsuario(empleado);

                        System.out.println(
                                        "Empleado registrado correctamente.");
                } catch (DateTimeParseException e) {
                        System.out.println(
                                        "Fecha inválida. Use AAAA-MM-DD.");
                } catch (IllegalArgumentException e) {
                        System.out.println(
                                        "No fue posible registrar el empleado: "
                                                        + e.getMessage());
                }
        }

        private void asignarTurnoEmpleado() {
                System.out.println();
                System.out.println("--- ASIGNAR TURNO ---");

                Empleado empleado = seleccionarEmpleado();

                if (empleado == null) {
                        return;
                }

                mostrarDiasSemana();
                String diaTexto = leerTexto("Día: ").toUpperCase();
                String inicioTexto = leerTexto("Hora de inicio (HH:MM): ");
                String finTexto = leerTexto("Hora final (HH:MM): ");

                try {
                        Turno turno = new Turno(
                                        DiaSemana.valueOf(diaTexto),
                                        LocalTime.parse(inicioTexto),
                                        LocalTime.parse(finTexto));

                        club.asignarTurno(empleado, turno);

                        System.out.println(
                                        "Turno asignado correctamente.");
                } catch (DateTimeParseException e) {
                        System.out.println(
                                        "Hora inválida. Use HH:MM.");
                } catch (IllegalArgumentException
                                | IllegalStateException e) {

                        System.out.println(
                                        "No fue posible asignar el turno: "
                                                        + e.getMessage());
                }
        }

        private void solicitarCambioTurno() {
                System.out.println();
                System.out.println("--- SOLICITAR CAMBIO DE TURNO ---");

                Empleado empleado = seleccionarEmpleado();

                if (empleado == null) {
                        return;
                }

                Turno turno = seleccionarTurno(empleado);

                if (turno == null) {
                        return;
                }

                mostrarDiasSemana();
                String diaTexto = leerTexto("Nuevo día: ").toUpperCase();
                String inicioTexto = leerTexto(
                                "Nueva hora de inicio (HH:MM): ");
                String finTexto = leerTexto(
                                "Nueva hora final (HH:MM): ");

                try {
                        SolicitudCambioTurno solicitud = club.solicitarCambioTurno(
                                        empleado,
                                        turno,
                                        DiaSemana.valueOf(diaTexto),
                                        LocalTime.parse(inicioTexto),
                                        LocalTime.parse(finTexto));

                        System.out.println(
                                        "Solicitud registrada correctamente.");
                        System.out.println(solicitud);
                } catch (DateTimeParseException e) {
                        System.out.println(
                                        "Hora inválida. Use HH:MM.");
                } catch (IllegalArgumentException
                                | IllegalStateException e) {

                        System.out.println(
                                        "No fue posible registrar la solicitud: "
                                                        + e.getMessage());
                }
        }

        private void decidirSolicitudCambio() {
                System.out.println();
                System.out.println("--- DECIDIR SOLICITUD DE CAMBIO ---");

                SolicitudCambioTurno solicitud = seleccionarSolicitud();

                if (solicitud == null) {
                        return;
                }

                String idAdministrador = leerTexto(
                                "ID del administrador: ");

                Usuario usuario = club.buscarUsuario(idAdministrador);

                if (!(usuario instanceof Administrador)) {
                        System.out.println(
                                        "No existe un administrador con ese ID.");
                        return;
                }

                boolean aprobar = leerSiNo(
                                "¿Desea aprobar la solicitud? (s/n): ");

                try {
                        if (aprobar) {
                                club.aprobarCambioTurno(
                                                solicitud,
                                                (Administrador) usuario);

                                System.out.println(
                                                "Solicitud aprobada y turno actualizado.");
                        } else {
                                club.rechazarCambioTurno(
                                                solicitud,
                                                (Administrador) usuario);

                                System.out.println(
                                                "Solicitud rechazada.");
                        }
                } catch (IllegalArgumentException
                                | IllegalStateException e) {

                        System.out.println(
                                        "No fue posible decidir la solicitud: "
                                                        + e.getMessage());
                }
        }

        private void verTurnosYSolicitudes() {
                System.out.println();
                System.out.println("--- TURNOS Y SOLICITUDES ---");

                boolean hayEmpleados = false;

                for (Usuario usuario : club.getUsuarios()) {
                        if (!(usuario instanceof Empleado)) {
                                continue;
                        }

                        hayEmpleados = true;
                        Empleado empleado = (Empleado) usuario;

                        System.out.println();
                        System.out.println(
                                        empleado.getNombre()
                                                        + " | "
                                                        + empleado.getClass().getSimpleName());

                        if (empleado.getTurnos().isEmpty()) {
                                System.out.println("Sin turnos asignados.");
                        } else {
                                for (Turno turno : empleado.getTurnos()) {
                                        System.out.println("- " + turno);
                                }
                        }
                }

                if (!hayEmpleados) {
                        System.out.println("No hay empleados registrados.");
                }

                System.out.println();
                System.out.println("Solicitudes:");

                if (club.getSolicitudesCambioTurno().isEmpty()) {
                        System.out.println("No hay solicitudes registradas.");
                } else {
                        for (SolicitudCambioTurno solicitud : club.getSolicitudesCambioTurno()) {

                                System.out.println("- " + solicitud);
                        }
                }
        }

        private Empleado seleccionarEmpleado() {
                String idEmpleado = leerTexto("ID del empleado: ");
                Usuario usuario = club.buscarUsuario(idEmpleado);

                if (!(usuario instanceof Empleado)) {
                        System.out.println(
                                        "No existe un empleado con ese ID.");
                        return null;
                }

                return (Empleado) usuario;
        }

        private Turno seleccionarTurno(Empleado empleado) {
                if (empleado.getTurnos().isEmpty()) {
                        System.out.println(
                                        "El empleado no tiene turnos asignados.");
                        return null;
                }

                int numero = 1;
                for (Turno turno : empleado.getTurnos()) {
                        System.out.println(numero + ". " + turno);
                        numero++;
                }

                int seleccion = leerEntero("Número del turno: ");

                if (seleccion < 1
                                || seleccion > empleado.getTurnos().size()) {

                        System.out.println("El turno seleccionado no es válido.");
                        return null;
                }

                return empleado.getTurnos().get(seleccion - 1);
        }

        private SolicitudCambioTurno seleccionarSolicitud() {
                if (club.getSolicitudesCambioTurno().isEmpty()) {
                        System.out.println(
                                        "No hay solicitudes registradas.");
                        return null;
                }

                int numero = 1;
                for (SolicitudCambioTurno solicitud : club.getSolicitudesCambioTurno()) {

                        System.out.println(numero + ". " + solicitud);
                        numero++;
                }

                int seleccion = leerEntero(
                                "Número de la solicitud: ");

                if (seleccion < 1
                                || seleccion > club.getSolicitudesCambioTurno().size()) {

                        System.out.println(
                                        "La solicitud seleccionada no es válida.");
                        return null;
                }

                return club.getSolicitudesCambioTurno().get(
                                seleccion - 1);
        }

        private void mostrarDiasSemana() {
                System.out.println("Días disponibles:");

                for (DiaSemana dia : DiaSemana.values()) {
                        System.out.println("- " + dia);
                }
        }

        private void registrarSugerenciaProducto() {
                System.out.println();
                System.out.println("--- REGISTRAR SUGERENCIA DE PRODUCTO ---");

                Empleado empleado = seleccionarEmpleado();

                if (empleado == null) {
                        return;
                }

                String nombre = leerTexto("Nombre del producto sugerido: ");
                String descripcion = leerTexto("Descripción: ");

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

                String idAdministrador = leerTexto(
                                "ID del administrador: ");

                Usuario usuario = club.buscarUsuario(idAdministrador);

                if (!(usuario instanceof Administrador)) {
                        System.out.println(
                                        "No existe un administrador con ese ID.");
                        return;
                }

                boolean aprobar = leerSiNo(
                                "¿Desea aprobar la sugerencia? (s/n): ");

                try {
                        if (aprobar) {
                                double precio = leerDouble(
                                                "Precio del producto: ");
                                String categoria = leerTexto(
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

                int seleccion = leerEntero(
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

        private void registrarEstadisticaJugador() {
                System.out.println();
                System.out.println("--- REGISTRAR ESTADÍSTICA DE JUGADOR ---");

                Partido partido = seleccionarPartido();

                if (partido == null) {
                        return;
                }

                String idSocio = leerTexto("ID del jugador: ");
                Usuario usuario = club.buscarUsuario(idSocio);

                if (!(usuario instanceof Socio)) {
                        System.out.println(
                                        "No existe un socio con ese ID.");
                        return;
                }

                System.out.println("Tipos de estadística disponibles:");
                System.out.println("- FUTBOL");
                System.out.println("- BALONCESTO");
                System.out.println("- VOLEIBOL");
                System.out.println("- TENIS_MESA");
                System.out.println("- TENIS_PADEL");

                String tipo = leerTexto(
                                "Tipo de estadística: ").toUpperCase();

                try {
                        EstadisticaJugador estadistica = crearEstadistica(
                                        tipo,
                                        (Socio) usuario,
                                        partido);

                        partido.agregarEstadistica(estadistica);

                        System.out.println(
                                        "Estadística registrada correctamente.");
                } catch (IllegalArgumentException
                                | IllegalStateException e) {

                        System.out.println(
                                        "No fue posible registrar la estadística: "
                                                        + e.getMessage());
                }
        }

        private EstadisticaJugador crearEstadistica(
                        String tipo,
                        Socio socio,
                        Partido partido) {

                if (tipo.equals("FUTBOL")) {
                        EstadisticaFutbol estadistica = new EstadisticaFutbol(
                                        socio, partido);

                        int goles = leerCantidadNoNegativa("Goles: ");
                        int asistencias = leerCantidadNoNegativa("Asistencias: ");
                        int amarillas = leerCantidadNoNegativa(
                                        "Tarjetas amarillas: ");
                        int rojas = leerCantidadNoNegativa(
                                        "Tarjetas rojas: ");

                        for (int i = 0; i < goles; i++) {
                                estadistica.registrarGol();
                        }
                        for (int i = 0; i < asistencias; i++) {
                                estadistica.registrarAsistencia();
                        }
                        for (int i = 0; i < amarillas; i++) {
                                estadistica.registrarTarjetaAmarilla();
                        }
                        for (int i = 0; i < rojas; i++) {
                                estadistica.registrarTarjetaRoja();
                        }

                        return estadistica;
                }

                if (tipo.equals("BALONCESTO")) {
                        EstadisticaBaloncesto estadistica = new EstadisticaBaloncesto(
                                        socio, partido);

                        int puntos = leerCantidadNoNegativa("Puntos: ");
                        int rebotes = leerCantidadNoNegativa("Rebotes: ");

                        estadistica.agregarPuntos(puntos);

                        for (int i = 0; i < rebotes; i++) {
                                estadistica.registrarRebote();
                        }

                        return estadistica;
                }

                if (tipo.equals("VOLEIBOL")) {
                        EstadisticaVoleibol estadistica = new EstadisticaVoleibol(
                                        socio, partido);

                        int aces = leerCantidadNoNegativa("Aces: ");
                        int bloqueos = leerCantidadNoNegativa("Bloqueos: ");

                        for (int i = 0; i < aces; i++) {
                                estadistica.registrarAce();
                        }
                        for (int i = 0; i < bloqueos; i++) {
                                estadistica.registrarBloqueo();
                        }

                        return estadistica;
                }

                if (tipo.equals("TENIS_MESA")) {
                        EstadisticaTenisMesa estadistica = new EstadisticaTenisMesa(
                                        socio, partido);

                        int sets = leerCantidadNoNegativa(
                                        "Sets ganados: ");
                        int puntos = leerCantidadNoNegativa(
                                        "Puntos totales: ");

                        for (int i = 0; i < sets; i++) {
                                estadistica.registrarSetGanado();
                        }

                        estadistica.agregarPuntos(puntos);
                        return estadistica;
                }

                if (tipo.equals("TENIS_PADEL")) {
                        EstadisticaTenisPadel estadistica = new EstadisticaTenisPadel(
                                        socio, partido);

                        int sets = leerCantidadNoNegativa(
                                        "Sets ganados: ");
                        int games = leerCantidadNoNegativa(
                                        "Games ganados: ");
                        int aces = leerCantidadNoNegativa("Aces: ");
                        int errores = leerCantidadNoNegativa(
                                        "Errores no forzados: ");

                        for (int i = 0; i < sets; i++) {
                                estadistica.registrarSetGanado();
                        }
                        for (int i = 0; i < games; i++) {
                                estadistica.registrarGameGanado();
                        }
                        for (int i = 0; i < aces; i++) {
                                estadistica.registrarAce();
                        }
                        for (int i = 0; i < errores; i++) {
                                estadistica.registrarErrorNoForzado();
                        }

                        return estadistica;
                }

                throw new IllegalArgumentException(
                                "El tipo de estadística no es válido");
        }

        private void verEstadisticasPartidos() {
                System.out.println();
                System.out.println("--- ESTADÍSTICAS DE PARTIDOS ---");

                if (club.getPartidos().isEmpty()) {
                        System.out.println("No hay partidos registrados.");
                        return;
                }

                int numero = 1;

                for (Partido partido : club.getPartidos()) {
                        System.out.println();
                        System.out.println(
                                        "Partido #" + numero
                                                        + ": "
                                                        + describirPartido(partido));

                        if (partido.getEstadisticas().isEmpty()) {
                                System.out.println(
                                                "No tiene estadísticas registradas.");
                        } else {
                                for (EstadisticaJugador estadistica : partido.getEstadisticas()) {
                                        System.out.println(
                                                        "- "
                                                                        + estadistica.getJugador().getNombre()
                                                                        + " | "
                                                                        + describirEstadistica(estadistica));
                                }
                        }

                        numero++;
                }
        }

        private String describirEstadistica(
                        EstadisticaJugador estadistica) {

                if (estadistica instanceof EstadisticaFutbol) {
                        EstadisticaFutbol dato = (EstadisticaFutbol) estadistica;

                        return "Fútbol | goles: " + dato.getGoles()
                                        + " | asistencias: " + dato.getAsistencias()
                                        + " | amarillas: " + dato.getTarjetasAmarillas()
                                        + " | rojas: " + dato.getTarjetasRojas();
                }

                if (estadistica instanceof EstadisticaBaloncesto) {
                        EstadisticaBaloncesto dato = (EstadisticaBaloncesto) estadistica;

                        return "Baloncesto | puntos: " + dato.getPuntos()
                                        + " | rebotes: " + dato.getRebotes();
                }

                if (estadistica instanceof EstadisticaVoleibol) {
                        EstadisticaVoleibol dato = (EstadisticaVoleibol) estadistica;

                        return "Voleibol | aces: " + dato.getAces()
                                        + " | bloqueos: " + dato.getBloqueos();
                }

                if (estadistica instanceof EstadisticaTenisMesa) {
                        EstadisticaTenisMesa dato = (EstadisticaTenisMesa) estadistica;

                        return "Tenis de mesa | sets: " + dato.getSetsGanados()
                                        + " | puntos: " + dato.getPuntosTotales();
                }

                if (estadistica instanceof EstadisticaTenisPadel) {
                        EstadisticaTenisPadel dato = (EstadisticaTenisPadel) estadistica;

                        return "Tenis/Pádel | sets: " + dato.getSetsGanados()
                                        + " | games: " + dato.getGamesGanados()
                                        + " | aces: " + dato.getAces()
                                        + " | errores no forzados: "
                                        + dato.getErroresNoForzados();
                }

                return estadistica.getClass().getSimpleName();
        }

        private int leerCantidadNoNegativa(String mensaje) {
                while (ejecutando) {
                        int cantidad = leerEntero(mensaje);

                        if (cantidad >= 0) {
                                return cantidad;
                        }

                        System.out.println(
                                        "La cantidad no puede ser negativa.");
                }

                return 0;
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

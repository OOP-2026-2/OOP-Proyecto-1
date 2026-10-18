package utilidades.persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import clubdeportivo.ClubDeportivo;
import clubdeportivo.competencias.Competencia;
import clubdeportivo.competencias.FormatoCompetencia;
import clubdeportivo.competencias.InscripcionCompetencia;
import clubdeportivo.competencias.TipoCompetencia;
import clubdeportivo.competencias.Estadisticas.EstadisticaBaloncesto;
import clubdeportivo.competencias.Estadisticas.EstadisticaFutbol;
import clubdeportivo.competencias.Estadisticas.EstadisticaJugador;
import clubdeportivo.competencias.Estadisticas.EstadisticaTenisMesa;
import clubdeportivo.competencias.Estadisticas.EstadisticaTenisPadel;
import clubdeportivo.competencias.Estadisticas.EstadisticaVoleibol;
import clubdeportivo.competencias.Partidos.EstadoPartido;
import clubdeportivo.competencias.Partidos.Partido;
import clubdeportivo.competencias.Partidos.PartidoInterno;
import clubdeportivo.competencias.Partidos.PartidoOficial;
import clubdeportivo.competencias.Partidos.PartidoPractica;
import clubdeportivo.competencias.Partidos.PropositoPartido;
import clubdeportivo.deportes.Categoria;
import clubdeportivo.deportes.DeporteConjunto;
import clubdeportivo.deportes.DeporteIndividual;
import clubdeportivo.deportes.Equipo;
import clubdeportivo.deportes.EstadoInscripcion;
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
import clubdeportivo.turnos.EstadoSolicitud;
import clubdeportivo.turnos.SolicitudCambioTurno;
import clubdeportivo.turnos.Turno;
import tiendaclub.inventario.ArticuloTienda;
import tiendaclub.inventario.Bebida;
import tiendaclub.inventario.ExistenciaInventario;
import tiendaclub.inventario.MovimientoInventario;
import tiendaclub.inventario.Producto;
import tiendaclub.inventario.Snack;
import tiendaclub.inventario.SugerenciaProducto;
import tiendaclub.inventario.TipoMovimiento;
import tiendaclub.inventario.UbicacionInventario;
import tiendaclub.ventas.Venta;
import tiendaclub.ventas.descuentos.DescuentoCodigoCompartido;
import tiendaclub.ventas.detalles.DetalleVenta;
import tiendaclub.ventas.tipos.VentaCafeteria;
import tiendaclub.ventas.tipos.VentaTienda;
import usuarios.Comprador;
import usuarios.Usuario;
import usuarios.tipos.Empleado;
import usuarios.tipos.Socio;
import usuarios.tipos.empleados.Administrador;
import usuarios.tipos.empleados.Entrenador;
import usuarios.tipos.empleados.Fisioterapeuta;

public class PersistenciaClub {
    private static final Path CARPETA = Paths.get("datos");
    private static final Path ARCHIVO_SOCIOS = CARPETA.resolve("socios.txt");
    private static final Path ARCHIVO_DEPORTES_CONJUNTO = CARPETA.resolve("deportes_conjunto.txt");
    private static final Path ARCHIVO_DEPORTES_INDIVIDUALES = CARPETA.resolve("deportes_individuales.txt");
    private static final Path ARCHIVO_EQUIPOS = CARPETA.resolve("equipos.txt");
    private static final Path ARCHIVO_INSCRIPCIONES_EQUIPO = CARPETA.resolve("inscripciones_equipo.txt");
    private static final Path ARCHIVO_INSCRIPCIONES_ACADEMIA = CARPETA.resolve("inscripciones_academia.txt");
    private static final Path ARCHIVO_PRODUCTOS = CARPETA.resolve("productos.txt");
    private static final Path ARCHIVO_INVENTARIO = CARPETA.resolve("inventario.txt");
    private static final Path ARCHIVO_MOVIMIENTOS = CARPETA.resolve("movimientos.txt");
    private static final Path ARCHIVO_PAGOS = CARPETA.resolve("pagos.txt");
    private static final Path ARCHIVO_INSTALACIONES = CARPETA.resolve("instalaciones.txt");
    private static final Path ARCHIVO_RESERVAS = CARPETA.resolve("reservas.txt");
    private static final Path ARCHIVO_VENTAS = CARPETA.resolve("ventas.txt");
    private static final Path ARCHIVO_EMPLEADOS = CARPETA.resolve("empleados.txt");
    private static final Path ARCHIVO_SOLICITUDES_TURNO = CARPETA.resolve("solicitudes_turno.txt");
    private static final Path ARCHIVO_TURNOS = CARPETA.resolve("turnos.txt");
    private static final Path ARCHIVO_COMPETENCIAS = CARPETA.resolve("competencias.txt");
    private static final Path ARCHIVO_INSCRIPCIONES_COMPETENCIA = CARPETA.resolve("inscripciones_competencia.txt");
    private static final Path ARCHIVO_PARTIDOS = CARPETA.resolve("partidos.txt");
    private static final Path ARCHIVO_ESTADISTICAS = CARPETA.resolve("estadisticas.txt");
    private static final Path ARCHIVO_SESIONES = CARPETA.resolve("sesiones.txt");
    private static final Path ARCHIVO_ASISTENCIAS = CARPETA.resolve("asistencias.txt");
    private static final Path ARCHIVO_SUGERENCIAS = CARPETA.resolve("sugerencias.txt");

    public void guardar(ClubDeportivo club) throws IOException {
        if (club == null) {
            throw new IllegalArgumentException("El club es obligatorio");
        }

        Files.createDirectories(CARPETA);
        guardarSocios(club);
        guardarEmpleados(club);
        guardarTurnos(club);
        guardarSolicitudesTurno(club);
        guardarDeportesConjunto(club);
        guardarDeportesIndividuales(club);
        guardarEquipos(club);
        guardarInscripcionesEquipo(club);
        guardarInscripcionesAcademia(club);
        guardarCompetencias(club);
        guardarInscripcionesCompetencia(club);
        guardarPartidos(club);
        guardarEstadisticas(club);
        guardarProductos(club);
        guardarInventario(club);
        guardarMovimientos(club);
        guardarPagos(club);
        guardarInstalaciones(club);
        guardarReservas(club);
        guardarSesiones(club);
        guardarAsistencias(club);
        guardarSugerencias(club);
        guardarVentas(club);
    }

    public void cargar(ClubDeportivo club) throws IOException {
        if (club == null) {
            throw new IllegalArgumentException("El club es obligatorio");
        }

        Files.createDirectories(CARPETA);
        cargarSocios(club);
        cargarEmpleados(club);
        cargarSolicitudesTurno(club);
        cargarTurnos(club);
        cargarDeportesConjunto(club);
        cargarDeportesIndividuales(club);
        cargarEquipos(club);
        cargarInscripcionesEquipo(club);
        cargarInscripcionesAcademia(club);
        cargarEstadosEquipos(club);
        cargarCompetencias(club);
        cargarInscripcionesCompetencia(club);
        cargarPartidos(club);
        cargarEstadisticas(club);
        cargarProductos(club);
        cargarInventario(club);
        cargarInstalaciones(club);
        cargarMovimientos(club);
        cargarPagos(club);
        cargarReservas(club);
        cargarSesiones(club);
        cargarAsistencias(club);
        cargarSugerencias(club);
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

                Socio socio = new Socio(
                        datos[0],
                        datos[1],
                        LocalDate.parse(datos[2]),
                        datos[3],
                        datos[4]);

                socio.agregarPuntos(Double.parseDouble(datos[5]));
                club.registrarUsuario(socio);
            }
        }
    }

    // Deportes de conjunto

    private void guardarDeportesConjunto(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_DEPORTES_CONJUNTO)) {
            for (DeporteConjunto deporte : club.getDeportesConjunto()) {
                StringBuilder posiciones = new StringBuilder();

                for (Posicion posicion : deporte.getPosiciones()) {
                    if (posiciones.length() > 0) {
                        posiciones.append(",");
                    }

                    posiciones.append(posicion.getNombre());
                }

                escritor.write(deporte.getNombre() + ";"
                        + deporte.getMinimoJugadores() + ";"
                        + posiciones);

                escritor.newLine();
            }
        }
    }

    private void cargarDeportesConjunto(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_DEPORTES_CONJUNTO)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_DEPORTES_CONJUNTO)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 3) {
                    continue;
                }

                DeporteConjunto deporte = new DeporteConjunto(
                        datos[0],
                        Integer.parseInt(datos[1]));

                if (!datos[2].trim().isEmpty()) {
                    String[] posiciones = datos[2].split(",");

                    for (String nombrePosicion : posiciones) {
                        deporte.agregarPosicion(
                                new Posicion(nombrePosicion));
                    }
                }

                club.registrarDeporteConjunto(deporte);
            }
        }
    }

    // Deportes individuales

    private void guardarDeportesIndividuales(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_DEPORTES_INDIVIDUALES)) {
            for (DeporteIndividual deporte : club.getDeportesIndividuales()) {
                escritor.write(deporte.getNombre() + ";"
                        + deporte.getMaximoInscripciones());

                escritor.newLine();
            }
        }
    }

    private void cargarDeportesIndividuales(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_DEPORTES_INDIVIDUALES)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_DEPORTES_INDIVIDUALES)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 2) {
                    continue;
                }

                DeporteIndividual deporte = new DeporteIndividual(
                        datos[0],
                        Integer.parseInt(datos[1]));

                club.registrarDeporteIndividual(deporte);
            }
        }
    }

    // Equipos

    private void guardarEquipos(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_EQUIPOS)) {
            for (DeporteConjunto deporte : club.getDeportesConjunto()) {
                for (Equipo equipo : deporte.getEquipos()) {
                    escritor.write(deporte.getNombre() + ";"
                            + equipo.getNombre() + ";"
                            + equipo.getCategoria() + ";"
                            + equipo.getCupoMaximo() + ";"
                            + equipo.isActivo());

                    escritor.newLine();
                }
            }
        }
    }

    private void cargarEquipos(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_EQUIPOS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_EQUIPOS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 5) {
                    continue;
                }

                DeporteConjunto deporte = club.buscarDeporteConjunto(datos[0]);

                if (deporte == null) {
                    continue;
                }

                Equipo equipo = new Equipo(
                        datos[1],
                        Categoria.valueOf(datos[2]),
                        Integer.parseInt(datos[3]),
                        deporte);

                club.registrarEquipo(equipo);
            }
        }
    }

    private void cargarEstadosEquipos(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_EQUIPOS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_EQUIPOS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 5) {
                    continue;
                }

                DeporteConjunto deporte = club.buscarDeporteConjunto(datos[0]);

                Equipo equipo = club.buscarEquipo(datos[1]);

                if (deporte == null || equipo == null
                        || equipo.getDeporte() != deporte) {
                    continue;
                }

                if (!Boolean.parseBoolean(datos[4])) {
                    equipo.desactivar();
                }
            }
        }
    }

    // Inscripciones en equipos

    private void guardarInscripcionesEquipo(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_INSCRIPCIONES_EQUIPO)) {
            for (DeporteConjunto deporte : club.getDeportesConjunto()) {
                for (Equipo equipo : deporte.getEquipos()) {
                    for (InscripcionEquipo inscripcion : equipo.getInscripciones()) {
                        escritor.write(inscripcion.getSocio().getId() + ";"
                                + deporte.getNombre() + ";"
                                + equipo.getNombre() + ";"
                                + inscripcion.getPosicion().getNombre() + ";"
                                + inscripcion.getNumeroCamiseta() + ";"
                                + inscripcion.getEstado());

                        escritor.newLine();
                    }
                }
            }
        }
    }

    private void cargarInscripcionesEquipo(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_INSCRIPCIONES_EQUIPO)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_INSCRIPCIONES_EQUIPO)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 6) {
                    continue;
                }

                Usuario usuario = club.buscarUsuario(datos[0]);

                DeporteConjunto deporte = club.buscarDeporteConjunto(datos[1]);

                Equipo equipo = club.buscarEquipo(datos[2]);

                if (!(usuario instanceof Socio)
                        || deporte == null
                        || equipo == null
                        || equipo.getDeporte() != deporte) {
                    continue;
                }

                Posicion posicion = null;

                for (Posicion existente : deporte.getPosiciones()) {
                    if (existente.getNombre()
                            .equalsIgnoreCase(datos[3])) {
                        posicion = existente;
                        break;
                    }
                }

                if (posicion == null) {
                    continue;
                }

                InscripcionEquipo inscripcion = club.inscribirSocioEnEquipo(
                        (Socio) usuario,
                        equipo,
                        posicion,
                        Integer.parseInt(datos[4]));

                EstadoInscripcion estado = EstadoInscripcion.valueOf(datos[5]);

                if (estado == EstadoInscripcion.SUSPENDIDA) {
                    inscripcion.suspender();
                } else if (estado == EstadoInscripcion.RETIRADA) {
                    inscripcion.retirar();
                }
            }
        }
    }

    // Inscripciones en academias

    private void guardarInscripcionesAcademia(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_INSCRIPCIONES_ACADEMIA)) {
            for (DeporteIndividual deporte : club.getDeportesIndividuales()) {
                for (InscripcionAcademia inscripcion : deporte.getInscripciones()) {
                    escritor.write(inscripcion.getSocio().getId() + ";"
                            + deporte.getNombre() + ";"
                            + inscripcion.getNivel() + ";"
                            + inscripcion.getPuntajeRanking() + ";"
                            + inscripcion.getEstado());

                    escritor.newLine();
                }
            }
        }
    }

    private void cargarInscripcionesAcademia(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_INSCRIPCIONES_ACADEMIA)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_INSCRIPCIONES_ACADEMIA)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 5) {
                    continue;
                }

                Usuario usuario = club.buscarUsuario(datos[0]);

                DeporteIndividual deporte = club.buscarDeporteIndividual(datos[1]);

                if (!(usuario instanceof Socio)
                        || deporte == null) {
                    continue;
                }

                InscripcionAcademia inscripcion = club.inscribirSocioEnAcademia(
                        (Socio) usuario,
                        deporte,
                        Nivel.valueOf(datos[2]));

                inscripcion.actualizarPuntaje(
                        Double.parseDouble(datos[3]));

                EstadoInscripcion estado = EstadoInscripcion.valueOf(datos[4]);

                if (estado == EstadoInscripcion.SUSPENDIDA) {
                    inscripcion.suspender();
                } else if (estado == EstadoInscripcion.RETIRADA) {
                    inscripcion.retirar();
                }
            }
        }
    }

    // Productos

    private void guardarProductos(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_PRODUCTOS)) {
            for (Producto producto : club.getProductos()) {
                if (producto instanceof ArticuloTienda) {
                    ArticuloTienda articulo = (ArticuloTienda) producto;

                    escritor.write("ARTICULO;"
                            + articulo.getNombre() + ";"
                            + articulo.getPrecio() + ";"
                            + articulo.getCategoria());
                } else if (producto instanceof Bebida) {
                    Bebida bebida = (Bebida) producto;

                    escritor.write("BEBIDA;"
                            + bebida.getNombre() + ";"
                            + bebida.getPrecio() + ";"
                            + bebida.isCaliente());
                } else if (producto instanceof Snack) {
                    Snack snack = (Snack) producto;
                    StringBuilder alergenos = new StringBuilder();

                    for (String alergeno : snack.getAlergenos()) {
                        if (alergenos.length() > 0) {
                            alergenos.append(",");
                        }
                        alergenos.append(alergeno);
                    }

                    escritor.write("SNACK;"
                            + snack.getNombre() + ";"
                            + snack.getPrecio() + ";"
                            + alergenos);
                } else {
                    continue;
                }

                escritor.newLine();
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

                Producto producto;

                if (datos.length >= 4 && datos[0].equals("ARTICULO")) {
                    producto = new ArticuloTienda(
                            datos[1], Double.parseDouble(datos[2]), datos[3]);
                } else if (datos.length >= 4 && datos[0].equals("BEBIDA")) {
                    producto = new Bebida(
                            datos[1], Double.parseDouble(datos[2]),
                            Boolean.parseBoolean(datos[3]));
                } else if (datos.length >= 4 && datos[0].equals("SNACK")) {
                    ArrayList<String> alergenos = new ArrayList<String>();

                    if (!datos[3].trim().isEmpty()) {
                        String[] listaAlergenos = datos[3].split(",");

                        for (String alergeno : listaAlergenos) {
                            alergenos.add(alergeno);
                        }
                    }

                    producto = new Snack(
                            datos[1], Double.parseDouble(datos[2]), alergenos);
                } else {
                    producto = new ArticuloTienda(
                            datos[0], Double.parseDouble(datos[1]), datos[2]);
                }

                club.registrarProducto(producto);
            }
        }
    }

    // Inventario

    private void guardarInventario(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_INVENTARIO)) {
            for (UbicacionInventario ubicacion : club.getUbicacionesInventario()) {

                for (ExistenciaInventario existencia : ubicacion.getExistencias()) {

                    escritor.write(
                            existencia.getProducto().getNombre() + ";"
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

                ExistenciaInventario existencia = new ExistenciaInventario(
                        producto,
                        ubicacion,
                        Integer.parseInt(datos[2]));

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

                MovimientoInventario movimiento = new MovimientoInventario(
                        LocalDate.parse(datos[0]),
                        Integer.parseInt(datos[1]),
                        TipoMovimiento.valueOf(datos[2]),
                        producto,
                        origen,
                        destino);

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
                        LocalDate.parse(datos[0]),
                        datos[1],
                        Double.parseDouble(datos[2]),
                        (Socio) usuario);

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

                Instalacion instalacion = new Instalacion(
                        datos[1],
                        TipoInstalacion.valueOf(datos[0]),
                        Integer.parseInt(datos[2]),
                        Boolean.parseBoolean(datos[3]));

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

                if (!(usuario instanceof Socio)
                        || instalacion == null) {
                    continue;
                }

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
                        && ((VentaCafeteria) venta)
                        .getPropina() > 0;

                StringBuilder detalles = new StringBuilder();

                for (DetalleVenta detalle : venta.getDetalles()) {

                    if (detalles.length() > 0) {
                        detalles.append("|");
                    }

                    detalles.append(
                                    detalle.getProducto().getNombre())
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
                    venta = new VentaCafeteria(
                            fecha,
                            comprador);
                } else {
                    venta = new VentaTienda(
                            fecha,
                            comprador);
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

                    Producto producto = club.buscarProducto(
                            datosDetalle[0]);

                    if (producto == null) {
                        continue;
                    }

                    DetalleVenta detalle = new DetalleVenta(
                            producto,
                            Integer.parseInt(
                                    datosDetalle[1]));

                    venta.agregarDetalle(detalle);
                }

                if (venta.getDetalles().isEmpty()) {
                    continue;
                }

                if (venta instanceof VentaCafeteria) {
                    ((VentaCafeteria) venta)
                            .finalizarVenta(
                                    usaCodigoCompartido,
                                    incluyePropina);
                } else {
                    ((VentaTienda) venta)
                            .finalizarVenta(
                                    usaCodigoCompartido);
                }

                club.registrarVenta(venta);
            }
        }
    }

    // Estadísticas de jugadores

    private void guardarEstadisticas(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_ESTADISTICAS)) {
            int numeroPartido = 1;

            for (Partido partido : club.getPartidos()) {
                for (EstadisticaJugador estadistica : partido.getEstadisticas()) {
                    String tipo;
                    String valores;

                    if (estadistica instanceof EstadisticaBaloncesto) {
                        EstadisticaBaloncesto dato = (EstadisticaBaloncesto) estadistica;
                        tipo = "BALONCESTO";
                        valores = dato.getPuntos() + "," + dato.getRebotes();
                    } else if (estadistica instanceof EstadisticaFutbol) {
                        EstadisticaFutbol dato = (EstadisticaFutbol) estadistica;
                        tipo = "FUTBOL";
                        valores = dato.getGoles() + ","
                                + dato.getAsistencias() + ","
                                + dato.getTarjetasAmarillas() + ","
                                + dato.getTarjetasRojas();
                    } else if (estadistica instanceof EstadisticaTenisMesa) {
                        EstadisticaTenisMesa dato = (EstadisticaTenisMesa) estadistica;
                        tipo = "TENIS_MESA";
                        valores = dato.getSetsGanados() + ","
                                + dato.getPuntosTotales();
                    } else if (estadistica instanceof EstadisticaTenisPadel) {
                        EstadisticaTenisPadel dato = (EstadisticaTenisPadel) estadistica;
                        tipo = "TENIS_PADEL";
                        valores = dato.getSetsGanados() + ","
                                + dato.getGamesGanados() + ","
                                + dato.getAces() + ","
                                + dato.getErroresNoForzados();
                    } else if (estadistica instanceof EstadisticaVoleibol) {
                        EstadisticaVoleibol dato = (EstadisticaVoleibol) estadistica;
                        tipo = "VOLEIBOL";
                        valores = dato.getAces() + "," + dato.getBloqueos();
                    } else {
                        continue;
                    }

                    escritor.write(numeroPartido + ";"
                            + estadistica.getJugador().getId() + ";"
                            + tipo + ";"
                            + valores);
                    escritor.newLine();
                }

                numeroPartido++;
            }
        }
    }

    private void cargarEstadisticas(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_ESTADISTICAS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_ESTADISTICAS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 4) {
                    continue;
                }

                int indicePartido = Integer.parseInt(datos[0]) - 1;

                if (indicePartido < 0 || indicePartido >= club.getPartidos().size()) {
                    continue;
                }

                Usuario usuario = club.buscarUsuario(datos[1]);

                if (!(usuario instanceof Socio)) {
                    continue;
                }

                Partido partido = club.getPartidos().get(indicePartido);
                Socio socio = (Socio) usuario;
                String[] valores = datos[3].split(",", -1);
                EstadisticaJugador estadistica;

                if (datos[2].equals("BALONCESTO") && valores.length >= 2) {
                    EstadisticaBaloncesto dato = new EstadisticaBaloncesto(socio, partido);
                    dato.agregarPuntos(Integer.parseInt(valores[0]));

                    for (int i = 0; i < Integer.parseInt(valores[1]); i++) {
                        dato.registrarRebote();
                    }

                    estadistica = dato;
                } else if (datos[2].equals("FUTBOL") && valores.length >= 4) {
                    EstadisticaFutbol dato = new EstadisticaFutbol(socio, partido);

                    for (int i = 0; i < Integer.parseInt(valores[0]); i++) {
                        dato.registrarGol();
                    }
                    for (int i = 0; i < Integer.parseInt(valores[1]); i++) {
                        dato.registrarAsistencia();
                    }
                    for (int i = 0; i < Integer.parseInt(valores[2]); i++) {
                        dato.registrarTarjetaAmarilla();
                    }
                    for (int i = 0; i < Integer.parseInt(valores[3]); i++) {
                        dato.registrarTarjetaRoja();
                    }

                    estadistica = dato;
                } else if (datos[2].equals("TENIS_MESA") && valores.length >= 2) {
                    EstadisticaTenisMesa dato = new EstadisticaTenisMesa(socio, partido);

                    for (int i = 0; i < Integer.parseInt(valores[0]); i++) {
                        dato.registrarSetGanado();
                    }
                    dato.agregarPuntos(Integer.parseInt(valores[1]));
                    estadistica = dato;
                } else if (datos[2].equals("TENIS_PADEL") && valores.length >= 4) {
                    EstadisticaTenisPadel dato = new EstadisticaTenisPadel(socio, partido);

                    for (int i = 0; i < Integer.parseInt(valores[0]); i++) {
                        dato.registrarSetGanado();
                    }
                    for (int i = 0; i < Integer.parseInt(valores[1]); i++) {
                        dato.registrarGameGanado();
                    }
                    for (int i = 0; i < Integer.parseInt(valores[2]); i++) {
                        dato.registrarAce();
                    }
                    for (int i = 0; i < Integer.parseInt(valores[3]); i++) {
                        dato.registrarErrorNoForzado();
                    }

                    estadistica = dato;
                } else if (datos[2].equals("VOLEIBOL") && valores.length >= 2) {
                    EstadisticaVoleibol dato = new EstadisticaVoleibol(socio, partido);

                    for (int i = 0; i < Integer.parseInt(valores[0]); i++) {
                        dato.registrarAce();
                    }
                    for (int i = 0; i < Integer.parseInt(valores[1]); i++) {
                        dato.registrarBloqueo();
                    }

                    estadistica = dato;
                } else {
                    continue;
                }

                partido.agregarEstadistica(estadistica);
            }
        }
    }

    // Sesiones de entrenamiento

    private void guardarSesiones(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_SESIONES)) {
            int numero = 1;

            for (SesionEntrenamiento sesion : club.getSesionesEntrenamiento()) {
                String equipo = sesion.getEquipo() == null
                        ? ""
                        : sesion.getEquipo().getNombre();
                String deporte = sesion.getDeporteIndividual() == null
                        ? ""
                        : sesion.getDeporteIndividual().getNombre();

                escritor.write(numero + ";"
                        + sesion.getFecha() + ";"
                        + sesion.getHoraInicio() + ";"
                        + sesion.getDuracionMinutos() + ";"
                        + sesion.getEntrenador().getId() + ";"
                        + sesion.getInstalacion().getId() + ";"
                        + sesion.getTipo() + ";"
                        + equipo + ";"
                        + deporte);
                escritor.newLine();
                numero++;
            }
        }
    }

    private void cargarSesiones(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_SESIONES)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_SESIONES)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 9) {
                    continue;
                }

                Usuario usuario = club.buscarUsuario(datos[4]);
                Instalacion instalacion = club.buscarInstalacion(datos[5]);

                if (!(usuario instanceof Entrenador) || instalacion == null) {
                    continue;
                }

                SesionEntrenamiento sesion = SesionEntrenamiento.crearDesdePersistencia(
                        LocalDate.parse(datos[1]),
                        LocalTime.parse(datos[2]),
                        Integer.parseInt(datos[3]),
                        (Entrenador) usuario,
                        instalacion,
                        TipoSesion.valueOf(datos[6]));

                if (!datos[7].trim().isEmpty()) {
                    Equipo equipo = club.buscarEquipo(datos[7]);

                    if (equipo == null) {
                        continue;
                    }

                    sesion.asignarEquipoDesdePersistencia(equipo);
                } else {
                    DeporteIndividual deporte = club.buscarDeporteIndividual(datos[8]);

                    if (deporte == null) {
                        continue;
                    }

                    sesion.asignarDeporteIndividual(deporte);
                }

                club.programarSesion(sesion);
            }
        }
    }

    private void guardarAsistencias(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_ASISTENCIAS)) {
            int numeroSesion = 1;

            for (SesionEntrenamiento sesion : club.getSesionesEntrenamiento()) {
                for (RegistroAsistencia registro : sesion.getRegistros()) {
                    String nota = registro.getNotaDesempeno() == null
                            ? ""
                            : registro.getNotaDesempeno().toString();
                    String comentario = registro.getComentario() == null
                            ? ""
                            : registro.getComentario().replace(";", ",");

                    escritor.write(numeroSesion + ";"
                            + registro.getSocio().getId() + ";"
                            + registro.getEstado() + ";"
                            + nota + ";"
                            + comentario);
                    escritor.newLine();
                }

                numeroSesion++;
            }
        }
    }

    private void cargarAsistencias(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_ASISTENCIAS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_ASISTENCIAS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 5) {
                    continue;
                }

                int indiceSesion = Integer.parseInt(datos[0]) - 1;

                if (indiceSesion < 0
                        || indiceSesion >= club.getSesionesEntrenamiento().size()) {
                    continue;
                }

                Usuario usuario = club.buscarUsuario(datos[1]);

                if (!(usuario instanceof Socio)) {
                    continue;
                }

                SesionEntrenamiento sesion = club.getSesionesEntrenamiento()
                        .get(indiceSesion);
                RegistroAsistencia registro = new RegistroAsistencia(
                        sesion,
                        (Socio) usuario,
                        EstadoAsistencia.PENDIENTE);

                sesion.agregarRegistroDesdePersistencia(
                        registro);
                EstadoAsistencia estado = EstadoAsistencia.valueOf(datos[2]);

                if (estado != EstadoAsistencia.PENDIENTE) {
                    registro.registrarAsistencia(estado);
                }

                if (!datos[3].trim().isEmpty()) {
                    registro.registrarEvaluacion(
                            Double.parseDouble(datos[3]), datos[4]);
                }
            }
        }
    }

    // Sugerencias de productos

    private void guardarSugerencias(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_SUGERENCIAS)) {
            for (SugerenciaProducto sugerencia : club.getSugerenciasProducto()) {
                String idAdministrador = sugerencia.getAdministrador() == null
                        ? ""
                        : sugerencia.getAdministrador().getId();
                String producto = sugerencia.getProductoCreado() == null
                        ? ""
                        : sugerencia.getProductoCreado().getNombre();

                escritor.write(sugerencia.getNombre() + ";"
                        + sugerencia.getDescripcion().replace(";", ",") + ";"
                        + sugerencia.getProponente().getId() + ";"
                        + sugerencia.getEstado() + ";"
                        + idAdministrador + ";"
                        + producto);
                escritor.newLine();
            }
        }
    }

    private void cargarSugerencias(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_SUGERENCIAS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_SUGERENCIAS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 6) {
                    continue;
                }

                Usuario usuario = club.buscarUsuario(datos[2]);

                if (!(usuario instanceof Empleado)) {
                    continue;
                }

                SugerenciaProducto sugerencia = new SugerenciaProducto(
                        datos[0], datos[1], (Empleado) usuario);
                club.registrarSugerencia(sugerencia);
                EstadoSolicitud estado = EstadoSolicitud.valueOf(datos[3]);

                if (estado != EstadoSolicitud.PENDIENTE) {
                    Usuario usuarioAdministrador = club.buscarUsuario(datos[4]);

                    if (!(usuarioAdministrador instanceof Administrador)) {
                        continue;
                    }

                    Administrador administrador = (Administrador) usuarioAdministrador;

                    if (estado == EstadoSolicitud.APROBADA) {
                        Producto producto = club.buscarProducto(datos[5]);

                        if (producto != null) {
                            sugerencia.aprobar(administrador, producto);
                        }
                    } else {
                        sugerencia.rechazar(administrador);
                    }
                }
            }
        }
    }

    // Competencias

    private void guardarCompetencias(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_COMPETENCIAS)) {
            for (Competencia competencia : club.getCompetencias()) {
                escritor.write(competencia.getNombre() + ";"
                        + competencia.getOrganizador() + ";"
                        + competencia.getAnio() + ";"
                        + competencia.getCategoria() + ";"
                        + competencia.getTipo() + ";"
                        + competencia.getFormato() + ";"
                        + competencia.getDeporte().getNombre());
                escritor.newLine();
            }
        }
    }

    private void cargarCompetencias(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_COMPETENCIAS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_COMPETENCIAS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 7) {
                    continue;
                }

                DeporteConjunto deporte = club.buscarDeporteConjunto(datos[6]);

                if (deporte == null) {
                    continue;
                }

                Competencia competencia = new Competencia(
                        datos[0],
                        datos[1],
                        Integer.parseInt(datos[2]),
                        Categoria.valueOf(datos[3]),
                        TipoCompetencia.valueOf(datos[4]),
                        FormatoCompetencia.valueOf(datos[5]),
                        deporte);

                club.registrarCompetencia(competencia);
            }
        }
    }

    private void guardarInscripcionesCompetencia(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_INSCRIPCIONES_COMPETENCIA)) {
            for (Competencia competencia : club.getCompetencias()) {
                for (InscripcionCompetencia inscripcion : competencia.getInscripciones()) {
                    escritor.write(competencia.getNombre() + ";"
                            + competencia.getAnio() + ";"
                            + inscripcion.getEquipo().getNombre() + ";"
                            + inscripcion.getEstado());
                    escritor.newLine();
                }
            }
        }
    }

    private void cargarInscripcionesCompetencia(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_INSCRIPCIONES_COMPETENCIA)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_INSCRIPCIONES_COMPETENCIA)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 4) {
                    continue;
                }

                Competencia competencia = club.buscarCompetencia(
                        datos[0], Integer.parseInt(datos[1]));
                Equipo equipo = club.buscarEquipo(datos[2]);

                if (competencia == null || equipo == null) {
                    continue;
                }

                competencia.inscribirEquipo(equipo);
                InscripcionCompetencia inscripcion = competencia.buscarInscripcion(equipo);
                EstadoInscripcion estado = EstadoInscripcion.valueOf(datos[3]);

                if (estado == EstadoInscripcion.SUSPENDIDA) {
                    inscripcion.suspender();
                } else if (estado == EstadoInscripcion.RETIRADA) {
                    inscripcion.retirar();
                }
            }
        }
    }

    // Partidos

    private void guardarPartidos(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_PARTIDOS)) {
            for (Partido partido : club.getPartidos()) {
                String tipo;
                String datosEspecificos;

                if (partido instanceof PartidoOficial) {
                    PartidoOficial oficial = (PartidoOficial) partido;
                    tipo = "OFICIAL";
                    datosEspecificos = oficial.getEquipo().getNombre() + ","
                            + oficial.getCompetencia().getNombre() + ","
                            + oficial.getCompetencia().getAnio() + ","
                            + oficial.getInstitucionRival() + ","
                            + oficial.getLugar();
                } else if (partido instanceof PartidoInterno) {
                    PartidoInterno interno = (PartidoInterno) partido;
                    tipo = "INTERNO";
                    datosEspecificos = interno.getEquipoLocal().getNombre() + ","
                            + interno.getEquipoVisitante().getNombre() + ","
                            + interno.getProposito();
                } else if (partido instanceof PartidoPractica) {
                    PartidoPractica practica = (PartidoPractica) partido;
                    tipo = "PRACTICA";
                    StringBuilder participantes = new StringBuilder();

                    for (Socio socio : practica.getParticipantes()) {
                        if (participantes.length() > 0) {
                            participantes.append(",");
                        }
                        participantes.append(socio.getId());
                    }

                    datosEspecificos = practica.getDeporte().getNombre() + ","
                            + practica.getModalidad() + ","
                            + participantes;
                } else {
                    continue;
                }

                String marcador = partido.getMarcadorFinal() == null
                        ? ""
                        : partido.getMarcadorFinal();

                escritor.write(tipo + ";"
                        + partido.getFecha() + ";"
                        + partido.getEstado() + ";"
                        + marcador + ";"
                        + datosEspecificos);
                escritor.newLine();
            }
        }
    }

    private void cargarPartidos(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_PARTIDOS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_PARTIDOS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 5) {
                    continue;
                }

                String[] especificos = datos[4].split(",", -1);
                LocalDate fecha = LocalDate.parse(datos[1]);
                Partido partido;

                if (datos[0].equals("OFICIAL") && especificos.length >= 5) {
                    Equipo equipo = club.buscarEquipo(especificos[0]);
                    Competencia competencia = club.buscarCompetencia(
                            especificos[1], Integer.parseInt(especificos[2]));

                    if (equipo == null || competencia == null) {
                        continue;
                    }

                    partido = new PartidoOficial(fecha, equipo, competencia,
                            especificos[3], especificos[4]);
                } else if (datos[0].equals("INTERNO") && especificos.length >= 3) {
                    Equipo local = club.buscarEquipo(especificos[0]);
                    Equipo visitante = club.buscarEquipo(especificos[1]);

                    if (local == null || visitante == null) {
                        continue;
                    }

                    partido = new PartidoInterno(fecha, local, visitante,
                            PropositoPartido.valueOf(especificos[2]));
                } else if (datos[0].equals("PRACTICA") && especificos.length >= 2) {
                    DeporteIndividual deporte = club.buscarDeporteIndividual(especificos[0]);

                    if (deporte == null) {
                        continue;
                    }

                    PartidoPractica practica = new PartidoPractica(
                            fecha, deporte, Modalidad.valueOf(especificos[1]));

                    for (int i = 2; i < especificos.length; i++) {
                        Usuario usuario = club.buscarUsuario(especificos[i]);

                        if (usuario instanceof Socio) {
                            practica.agregarParticipante((Socio) usuario);
                        }
                    }

                    partido = practica;
                } else {
                    continue;
                }

                club.registrarPartido(partido);

                if (!datos[3].trim().isEmpty()) {
                    partido.registrarResultado(datos[3]);
                }

                EstadoPartido estado = EstadoPartido.valueOf(datos[2]);

                if (estado == EstadoPartido.FINALIZADO) {
                    partido.finalizar();
                } else if (estado == EstadoPartido.CANCELADO) {
                    partido.cancelar();
                }
            }
        }
    }

    // Empleados

    private void guardarEmpleados(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_EMPLEADOS)) {
            for (Usuario usuario : club.getUsuarios()) {
                if (!(usuario instanceof Empleado)) {
                    continue;
                }

                Empleado empleado = (Empleado) usuario;
                String tipo;

                if (empleado instanceof Administrador) {
                    tipo = "ADMINISTRADOR";
                } else if (empleado instanceof Entrenador) {
                    tipo = "ENTRENADOR";
                } else if (empleado instanceof Fisioterapeuta) {
                    tipo = "FISIOTERAPEUTA";
                } else {
                    continue;
                }

                escritor.write(tipo + ";"
                        + empleado.getId() + ";"
                        + empleado.getNombre() + ";"
                        + empleado.getFechaNacimiento() + ";"
                        + empleado.getLogin() + ";"
                        + empleado.getContrasena() + ";"
                        + empleado.getCodigoDescuento());
                escritor.newLine();
            }
        }
    }

    private void cargarEmpleados(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_EMPLEADOS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_EMPLEADOS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 7 || club.buscarUsuario(datos[1]) != null) {
                    continue;
                }

                LocalDate fechaNacimiento = LocalDate.parse(datos[3]);
                Empleado empleado;

                if (datos[0].equals("ADMINISTRADOR")) {
                    empleado = new Administrador(datos[1], datos[2], fechaNacimiento,
                            datos[4], datos[5], datos[6]);
                } else if (datos[0].equals("ENTRENADOR")) {
                    empleado = new Entrenador(datos[1], datos[2], fechaNacimiento,
                            datos[4], datos[5], datos[6]);
                } else if (datos[0].equals("FISIOTERAPEUTA")) {
                    empleado = new Fisioterapeuta(datos[1], datos[2], fechaNacimiento,
                            datos[4], datos[5], datos[6]);
                } else {
                    continue;
                }

                club.registrarUsuario(empleado);
            }
        }
    }

    // Turnos

    private void guardarTurnos(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_TURNOS)) {
            for (Usuario usuario : club.getUsuarios()) {
                if (!(usuario instanceof Empleado)) {
                    continue;
                }

                Empleado empleado = (Empleado) usuario;

                for (Turno turno : empleado.getTurnos()) {
                    escritor.write(empleado.getId() + ";"
                            + turno.getDiaSemana() + ";"
                            + turno.getHoraInicio() + ";"
                            + turno.getHoraFin());
                    escritor.newLine();
                }
            }
        }
    }

    private void cargarTurnos(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_TURNOS)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_TURNOS)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 4) {
                    continue;
                }

                Usuario usuario = club.buscarUsuario(datos[0]);

                if (!(usuario instanceof Empleado)) {
                    continue;
                }

                Empleado empleado = (Empleado) usuario;
                DiaSemana dia = DiaSemana.valueOf(datos[1]);
                LocalTime inicio = LocalTime.parse(datos[2]);
                LocalTime fin = LocalTime.parse(datos[3]);

                if (buscarTurno(empleado, dia, inicio, fin) == null) {
                    club.asignarTurno(empleado, new Turno(dia, inicio, fin));
                }
            }
        }
    }

    private Turno buscarTurno(Empleado empleado, DiaSemana dia,
                              LocalTime inicio, LocalTime fin) {

        for (Turno turno : empleado.getTurnos()) {
            if (turno.getDiaSemana() == dia
                    && turno.getHoraInicio().equals(inicio)
                    && turno.getHoraFin().equals(fin)) {
                return turno;
            }
        }

        return null;
    }

    // Solicitudes de cambio de turno

    private void guardarSolicitudesTurno(ClubDeportivo club) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_SOLICITUDES_TURNO)) {
            for (SolicitudCambioTurno solicitud : club.getSolicitudesCambioTurno()) {
                String idAdministrador = solicitud.getAdministradorDecision() == null
                        ? ""
                        : solicitud.getAdministradorDecision().getId();

                escritor.write(solicitud.getEmpleadoSolicitante().getId() + ";"
                        + solicitud.getTurnoOriginal().getDiaSemana() + ";"
                        + solicitud.getTurnoOriginal().getHoraInicio() + ";"
                        + solicitud.getTurnoOriginal().getHoraFin() + ";"
                        + solicitud.getNuevoDia() + ";"
                        + solicitud.getNuevaHoraInicio() + ";"
                        + solicitud.getNuevaHoraFin() + ";"
                        + solicitud.getEstado() + ";"
                        + idAdministrador);
                escritor.newLine();
            }
        }
    }

    private void cargarSolicitudesTurno(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_SOLICITUDES_TURNO)) {
            return;
        }

        try (BufferedReader lector = Files.newBufferedReader(ARCHIVO_SOLICITUDES_TURNO)) {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";", -1);

                if (datos.length < 9) {
                    continue;
                }

                Usuario usuario = club.buscarUsuario(datos[0]);

                if (!(usuario instanceof Empleado)) {
                    continue;
                }

                Empleado empleado = (Empleado) usuario;
                DiaSemana diaOriginal = DiaSemana.valueOf(datos[1]);
                LocalTime inicioOriginal = LocalTime.parse(datos[2]);
                LocalTime finOriginal = LocalTime.parse(datos[3]);
                Turno turnoOriginal = buscarTurno(empleado, diaOriginal,
                        inicioOriginal, finOriginal);

                if (turnoOriginal == null) {
                    turnoOriginal = new Turno(diaOriginal, inicioOriginal, finOriginal);
                    empleado.agregarTurno(turnoOriginal);
                }

                SolicitudCambioTurno solicitud = club.solicitarCambioTurno(
                        empleado,
                        turnoOriginal,
                        DiaSemana.valueOf(datos[4]),
                        LocalTime.parse(datos[5]),
                        LocalTime.parse(datos[6]));

                EstadoSolicitud estado = EstadoSolicitud.valueOf(datos[7]);

                if (estado != EstadoSolicitud.PENDIENTE) {
                    Usuario usuarioAdministrador = club.buscarUsuario(datos[8]);

                    if (!(usuarioAdministrador instanceof Administrador)) {
                        continue;
                    }

                    Administrador administrador = (Administrador) usuarioAdministrador;

                    if (estado == EstadoSolicitud.APROBADA) {
                        administrador.aprobarSolicitud(solicitud);
                        empleado.aplicarCambioTurno(solicitud);
                    } else {
                        administrador.rechazarSolicitud(solicitud);
                    }
                }
            }
        }
    }
}
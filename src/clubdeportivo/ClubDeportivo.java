package clubdeportivo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import clubdeportivo.competencias.Competencia;
import clubdeportivo.competencias.Partidos.Partido;
import clubdeportivo.competencias.Partidos.PartidoInterno;
import clubdeportivo.competencias.Partidos.PartidoOficial;
import clubdeportivo.competencias.Partidos.PartidoPractica;
import clubdeportivo.deportes.DeporteConjunto;
import clubdeportivo.deportes.DeporteIndividual;
import clubdeportivo.deportes.Equipo;
import clubdeportivo.deportes.InscripcionAcademia;
import clubdeportivo.deportes.InscripcionEquipo;
import clubdeportivo.deportes.Nivel;
import clubdeportivo.deportes.Posicion;
import clubdeportivo.entrenamientos.EstadoAsistencia;
import clubdeportivo.entrenamientos.RegistroAsistencia;
import clubdeportivo.entrenamientos.SesionEntrenamiento;
import clubdeportivo.instalaciones.Instalacion;
import clubdeportivo.instalaciones.Reserva;
import clubdeportivo.instalaciones.TipoInstalacion;
import clubdeportivo.membresias.PagoMensualidad;
import clubdeportivo.turnos.DiaSemana;
import clubdeportivo.turnos.SolicitudCambioTurno;
import clubdeportivo.turnos.Turno;
import tiendaclub.inventario.Bebida;
import tiendaclub.inventario.ExistenciaInventario;
import tiendaclub.inventario.MovimientoInventario;
import tiendaclub.inventario.Producto;
import tiendaclub.inventario.SugerenciaProducto;
import tiendaclub.inventario.TipoMovimiento;
import tiendaclub.inventario.UbicacionInventario;
import tiendaclub.ventas.Venta;
import tiendaclub.ventas.detalles.DetalleVenta;
import usuarios.Usuario;
import usuarios.tipos.Empleado;
import usuarios.tipos.Socio;
import usuarios.tipos.empleados.Administrador;
import usuarios.tipos.empleados.Entrenador;
import usuarios.tipos.empleados.Fisioterapeuta;
import utilidades.excepciones.InventarioInsuficienteException;
import utilidades.excepciones.ReservaNoDisponibleException;

public class ClubDeportivo {
    private final String nombre;
    private final String institucion;
    private final LocalTime horaApertura;
    private final LocalTime horaCierre;
    private final int minEntrenadoresEnTurno;
    private final int minFisioterapeutasEnTurno;
    private final ArrayList<Usuario> usuarios;
    private final ArrayList<DeporteConjunto> deportesConjunto;
    private final ArrayList<DeporteIndividual> deportesIndividuales;
    private final ArrayList<Competencia> competencias;
    private final ArrayList<Partido> partidos;
    private final ArrayList<Instalacion> instalaciones;
    private final ArrayList<Reserva> reservas;
    private final ArrayList<SesionEntrenamiento> sesionesEntrenamiento;
    private final ArrayList<SolicitudCambioTurno> solicitudesCambioTurno;
    private final ArrayList<PagoMensualidad> pagosMensualidad;
    private final ArrayList<Producto> productos;
    private final ArrayList<UbicacionInventario> ubicacionesInventario;
    private final ArrayList<MovimientoInventario> movimientosInventario;
    private final ArrayList<SugerenciaProducto> sugerenciasProducto;
    private final ArrayList<Venta> ventas;

    public ClubDeportivo() {
        this("Club Deportivo Séneca", "Universidad de los Andes",
                LocalTime.of(6, 0), LocalTime.of(22, 0), 2, 1);
    }

    public ClubDeportivo(String nombre, String institucion, LocalTime horaApertura,
            LocalTime horaCierre, int minEntrenadoresEnTurno, int minFisioterapeutasEnTurno) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del club es obligatorio");
        }

        if (institucion == null || institucion.trim().isEmpty()) {
            throw new IllegalArgumentException("La institución es obligatoria");
        }

        if (horaApertura == null || horaCierre == null) {
            throw new IllegalArgumentException("El horario del club es obligatorio");
        }

        if (!horaCierre.isAfter(horaApertura)) {
            throw new IllegalArgumentException("La hora de cierre debe ser posterior a la hora de apertura");
        }

        if (minEntrenadoresEnTurno < 0 || minFisioterapeutasEnTurno < 0) {
            throw new IllegalArgumentException("Los mínimos no pueden ser negativos");
        }

        this.nombre = nombre;
        this.institucion = institucion;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
        this.minEntrenadoresEnTurno = minEntrenadoresEnTurno;
        this.minFisioterapeutasEnTurno = minFisioterapeutasEnTurno;

        usuarios = new ArrayList<Usuario>();
        deportesConjunto = new ArrayList<DeporteConjunto>();
        deportesIndividuales = new ArrayList<DeporteIndividual>();
        competencias = new ArrayList<Competencia>();
        partidos = new ArrayList<Partido>();
        instalaciones = new ArrayList<Instalacion>();
        reservas = new ArrayList<Reserva>();
        sesionesEntrenamiento = new ArrayList<SesionEntrenamiento>();
        solicitudesCambioTurno = new ArrayList<SolicitudCambioTurno>();
        pagosMensualidad = new ArrayList<PagoMensualidad>();
        productos = new ArrayList<Producto>();
        ubicacionesInventario = new ArrayList<UbicacionInventario>();
        movimientosInventario = new ArrayList<MovimientoInventario>();
        sugerenciasProducto = new ArrayList<SugerenciaProducto>();
        ventas = new ArrayList<Venta>();
    }

    // Usuarios

    public void registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }

        if (buscarUsuario(usuario.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un usuario con ese ID");
        }

        usuarios.add(usuario);
    }

    public Usuario buscarUsuario(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        for (Usuario usuario : usuarios) {
            if (usuario.getId().equalsIgnoreCase(id)) {
                return usuario;
            }
        }

        return null;
    }

    // Deportes

    public void registrarDeporteConjunto(DeporteConjunto deporte) {
        if (deporte == null) {
            throw new IllegalArgumentException(
                    "El deporte no puede ser nulo");
        }

        if (buscarDeporteConjunto(deporte.getNombre()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un deporte de conjunto con ese nombre");
        }

        deportesConjunto.add(deporte);
    }

    public DeporteConjunto buscarDeporteConjunto(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        for (DeporteConjunto deporte : deportesConjunto) {
            if (deporte.getNombre().equalsIgnoreCase(nombre)) {
                return deporte;
            }
        }

        return null;
    }

    public void registrarDeporteIndividual(DeporteIndividual deporte) {
        if (deporte == null) {
            throw new IllegalArgumentException(
                    "El deporte no puede ser nulo");
        }

        if (buscarDeporteIndividual(deporte.getNombre()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un deporte individual con ese nombre");
        }

        deportesIndividuales.add(deporte);
    }

    public DeporteIndividual buscarDeporteIndividual(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        for (DeporteIndividual deporte : deportesIndividuales) {
            if (deporte.getNombre().equalsIgnoreCase(nombre)) {
                return deporte;
            }
        }

        return null;
    }

    public void registrarEquipo(Equipo equipo) {
        if (equipo == null) {
            throw new IllegalArgumentException(
                    "El equipo no puede ser nulo");
        }

        DeporteConjunto deporte = equipo.getDeporte();

        if (!deportesConjunto.contains(deporte)) {
            throw new IllegalArgumentException(
                    "El deporte del equipo no está registrado");
        }

        for (Equipo existente : deporte.getEquipos()) {
            if (existente.getNombre().equalsIgnoreCase(
                    equipo.getNombre())) {

                throw new IllegalArgumentException(
                        "Ya existe un equipo con ese nombre en el deporte");
            }
        }

        deporte.agregarEquipo(equipo);
    }

    public Equipo buscarEquipo(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        for (DeporteConjunto deporte : deportesConjunto) {
            for (Equipo equipo : deporte.getEquipos()) {
                if (equipo.getNombre().equalsIgnoreCase(nombre)) {
                    return equipo;
                }
            }
        }

        return null;
    }

    public InscripcionEquipo inscribirSocioEnEquipo(
            Socio socio, Equipo equipo,
            Posicion posicion, int numeroCamiseta) {

        if (socio == null || equipo == null
                || posicion == null) {

            throw new IllegalArgumentException(
                    "El socio, el equipo y la posición son obligatorios");
        }

        if (buscarUsuario(socio.getId()) != socio) {
            throw new IllegalArgumentException(
                    "El socio no está registrado en el club");
        }

        if (!deportesConjunto.contains(
                equipo.getDeporte())
                || !equipo.getDeporte()
                        .getEquipos().contains(equipo)) {

            throw new IllegalArgumentException(
                    "El equipo no está registrado en el club");
        }

        if (!equipo.isActivo()) {
            throw new IllegalStateException(
                    "El equipo está inactivo");
        }

        for (Equipo otroEquipo : equipo.getDeporte().getEquipos()) {

            boolean mismaCategoria = otroEquipo
                    .getCategoria()
                    .equals(equipo.getCategoria());

            InscripcionEquipo existente = otroEquipo.buscarInscripcion(socio);

            if (mismaCategoria
                    && existente != null
                    && existente.estaActiva()) {

                throw new IllegalArgumentException(
                        "El socio ya pertenece a un equipo "
                                + "activo de esta disciplina y categoría");
            }
        }

        InscripcionEquipo inscripcion = new InscripcionEquipo(
                socio,
                equipo,
                posicion,
                numeroCamiseta);

        equipo.agregarInscripcion(inscripcion);
        return inscripcion;
    }

    public InscripcionAcademia inscribirSocioEnAcademia(
            Socio socio, DeporteIndividual deporte,
            Nivel nivel) {

        if (socio == null || deporte == null
                || nivel == null) {

            throw new IllegalArgumentException(
                    "El socio, el deporte y el nivel son obligatorios");
        }

        if (buscarUsuario(socio.getId()) != socio) {
            throw new IllegalArgumentException(
                    "El socio no está registrado en el club");
        }

        if (!deportesIndividuales.contains(deporte)) {
            throw new IllegalArgumentException(
                    "El deporte individual no está registrado");
        }

        InscripcionAcademia inscripcion = new InscripcionAcademia(
                socio,
                deporte,
                nivel);

        deporte.agregarInscripcion(inscripcion);
        return inscripcion;
    }

    // Competencias y partidos

    public void registrarCompetencia(Competencia competencia) {
        if (competencia == null) {
            throw new IllegalArgumentException(
                    "La competencia no puede ser nula");
        }

        if (!deportesConjunto.contains(
                competencia.getDeporte())) {

            throw new IllegalArgumentException(
                    "El deporte de la competencia no está registrado");
        }

        if (buscarCompetencia(
                competencia.getNombre(),
                competencia.getAnio()) != null) {

            throw new IllegalArgumentException(
                    "Ya existe una competencia con ese nombre y año");
        }

        competencias.add(competencia);
    }

    public Competencia buscarCompetencia(
            String nombre, int anio) {

        if (nombre == null
                || nombre.trim().isEmpty()
                || anio <= 0) {

            return null;
        }

        for (Competencia competencia : competencias) {
            if (competencia.getNombre().equalsIgnoreCase(nombre)
                    && competencia.getAnio() == anio) {

                return competencia;
            }
        }

        return null;
    }

    public void registrarPartido(Partido partido) {
        if (partido == null) {
            throw new IllegalArgumentException(
                    "El partido no puede ser nulo");
        }

        if (partidos.contains(partido)) {
            throw new IllegalArgumentException(
                    "El partido ya está registrado");
        }

        if (partido instanceof PartidoOficial) {
            PartidoOficial oficial = (PartidoOficial) partido;
            Competencia competencia = oficial.getCompetencia();

            if (!competencias.contains(competencia)) {
                throw new IllegalArgumentException(
                        "La competencia no está registrada en el club");
            }

            competencia.programarPartido(oficial);
        } else if (partido instanceof PartidoInterno) {
            PartidoInterno interno = (PartidoInterno) partido;

            if (buscarEquipo(
                    interno.getEquipoLocal().getNombre()) != interno.getEquipoLocal()
                    || buscarEquipo(
                            interno.getEquipoVisitante().getNombre()) != interno.getEquipoVisitante()) {

                throw new IllegalArgumentException(
                        "Los equipos del partido no están registrados");
            }
        } else if (partido instanceof PartidoPractica) {
            PartidoPractica practica = (PartidoPractica) partido;

            if (!deportesIndividuales.contains(
                    practica.getDeporte())) {

                throw new IllegalArgumentException(
                        "El deporte del partido no está registrado");
            }
        }

        partidos.add(partido);
    }

    // Entrenamientos y asistencias

    public void programarSesion(
            SesionEntrenamiento sesion) {

        if (sesion == null) {
            throw new IllegalArgumentException(
                    "La sesión no puede ser nula");
        }

        if (sesionesEntrenamiento.contains(sesion)) {
            throw new IllegalArgumentException(
                    "La sesión ya está registrada");
        }

        if (buscarUsuario(
                sesion.getEntrenador().getId()) != sesion.getEntrenador()) {

            throw new IllegalArgumentException(
                    "El entrenador no está registrado en el club");
        }

        if (buscarInstalacion(
                sesion.getInstalacion().getId()) != sesion.getInstalacion()) {

            throw new IllegalArgumentException(
                    "La instalación no está registrada en el club");
        }

        boolean tieneEquipo = sesion.getEquipo() != null;
        boolean tieneDeporteIndividual = sesion
                .getDeporteIndividual() != null;

        if (tieneEquipo == tieneDeporteIndividual) {
            throw new IllegalArgumentException(
                    "La sesión debe corresponder a un equipo "
                            + "o a un deporte individual");
        }

        if (tieneEquipo
                && buscarEquipo(
                        sesion.getEquipo().getNombre()) != sesion.getEquipo()) {

            throw new IllegalArgumentException(
                    "El equipo no está registrado en el club");
        }

        if (tieneDeporteIndividual
                && !deportesIndividuales.contains(
                        sesion.getDeporteIndividual())) {

            throw new IllegalArgumentException(
                    "El deporte individual no está registrado");
        }

        if (sesion.getHoraInicio().isBefore(horaApertura)
                || sesion.calcularHoraFin().isAfter(horaCierre)) {

            throw new IllegalArgumentException(
                    "La sesión debe realizarse dentro "
                            + "del horario del club");
        }

        if (!consultarDisponibilidad(
                sesion.getInstalacion(),
                sesion.getFecha(),
                sesion.getHoraInicio(),
                sesion.getDuracionMinutos())) {

            throw new IllegalStateException(
                    "La instalación no está disponible "
                            + "en ese horario");
        }

        for (SesionEntrenamiento existente : sesionesEntrenamiento) {

            if (sesion.seCruzaCon(existente)) {
                throw new IllegalStateException(
                        "La sesión se cruza con otra sesión "
                                + "del entrenador o de la instalación");
            }
        }

        sesionesEntrenamiento.add(sesion);
    }

    public void registrarSesion(
            SesionEntrenamiento sesion) {

        programarSesion(sesion);
    }

    public RegistroAsistencia registrarAsistencia(
            SesionEntrenamiento sesion,
            Socio socio) {

        if (sesion == null || socio == null) {
            throw new IllegalArgumentException(
                    "La sesión y el socio son obligatorios");
        }

        if (!sesionesEntrenamiento.contains(sesion)) {
            throw new IllegalArgumentException(
                    "La sesión no está registrada en el club");
        }

        if (buscarUsuario(socio.getId()) != socio) {
            throw new IllegalArgumentException(
                    "El socio no está registrado en el club");
        }

        RegistroAsistencia registro = new RegistroAsistencia(
                sesion,
                socio,
                EstadoAsistencia.PENDIENTE);

        sesion.agregarRegistro(registro);
        return registro;
    }

    // Instalaciones y reservas

    public void registrarInstalacion(Instalacion instalacion) {
        if (instalacion == null) {
            throw new IllegalArgumentException(
                    "La instalación no puede ser nula");
        }

        if (buscarInstalacion(instalacion.getId()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una instalación con ese ID");
        }

        instalaciones.add(instalacion);
    }

    public Instalacion buscarInstalacion(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }

        for (Instalacion instalacion : instalaciones) {
            if (instalacion.getId().equalsIgnoreCase(id)) {
                return instalacion;
            }
        }

        return null;
    }

    public boolean consultarDisponibilidad(
            Instalacion instalacion,
            LocalDate fecha,
            LocalTime inicio,
            int duracionMinutos) {

        if (instalacion == null || fecha == null
                || inicio == null || duracionMinutos <= 0) {
            return false;
        }

        if (buscarInstalacion(instalacion.getId()) != instalacion) {
            return false;
        }

        if (!instalacion.estaDisponible(
                fecha, inicio, duracionMinutos)) {
            return false;
        }

        LocalTime fin = inicio.plusMinutes(duracionMinutos);

        for (SesionEntrenamiento sesion : sesionesEntrenamiento) {

            boolean mismaInstalacion = sesion.getInstalacion() == instalacion;

            boolean mismaFecha = sesion.getFecha().equals(fecha);

            boolean seCruzan = inicio.isBefore(sesion.calcularHoraFin())
                    && sesion.getHoraInicio().isBefore(fin);

            if (mismaInstalacion
                    && mismaFecha
                    && seCruzan) {
                return false;
            }
        }

        return true;
    }

    public void registrarReserva(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException(
                    "La reserva no puede ser nula");
        }

        Instalacion instalacion = reserva.getInstalacion();

        if (buscarInstalacion(instalacion.getId()) != instalacion) {

            throw new IllegalArgumentException(
                    "La instalación no está registrada");
        }

        if (!reserva.esFechaValida()) {
            throw new IllegalArgumentException(
                    "No se permiten reservas en fechas pasadas");
        }

        if (!reserva.cantidadJugadoresValida()) {
            throw new IllegalArgumentException(
                    "La cantidad de jugadores no es válida");
        }

        if (!consultarDisponibilidad(
                instalacion,
                reserva.getFecha(),
                reserva.getHoraInicio(),
                reserva.getDuracionMinutos())) {

            throw new ReservaNoDisponibleException(
                    "La instalación no está disponible");
        }

        instalacion.agregarReserva(reserva);
        reservas.add(reserva);
    }

    // Mensualidades

    public void registrarPagoMensualidad(PagoMensualidad pago) {
        if (pago == null) {
            throw new IllegalArgumentException("El pago no puede ser nulo");
        }

        pagosMensualidad.add(pago);
        pago.getSocio().agregarPago(pago);
    }

    // Productos y ubicaciones

    public void registrarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }

        if (buscarProducto(producto.getNombre()) != null) {
            throw new IllegalArgumentException("Ya existe un producto con ese nombre");
        }

        productos.add(producto);
    }

    public Producto buscarProducto(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }

        return null;
    }

    public void registrarUbicacionInventario(UbicacionInventario ubicacion) {
        if (ubicacion == null) {
            throw new IllegalArgumentException("La ubicación no puede ser nula");
        }

        if (buscarUbicacionInventario(ubicacion.getNombre()) != null) {
            throw new IllegalArgumentException("Ya existe una ubicación con ese nombre");
        }

        ubicacionesInventario.add(ubicacion);
    }

    public UbicacionInventario buscarUbicacionInventario(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        for (UbicacionInventario ubicacion : ubicacionesInventario) {
            if (ubicacion.getNombre().equalsIgnoreCase(nombre)) {
                return ubicacion;
            }
        }

        return null;
    }

    public ExistenciaInventario buscarExistencia(Producto producto, UbicacionInventario ubicacion) {
        if (producto == null || ubicacion == null) {
            return null;
        }

        return ubicacion.buscarExistencia(producto);
    }

    // Movimientos de inventario

    public void registrarMovimiento(MovimientoInventario movimiento) {
        if (movimiento == null || !movimiento.esValido()) {
            throw new IllegalArgumentException("El movimiento no es válido");
        }

        movimientosInventario.add(movimiento);
    }

    public void reabastecerProducto(Producto producto, UbicacionInventario ubicacion, int cantidad) {
        validarProductoYUbicacion(producto, ubicacion);

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        ExistenciaInventario existencia = buscarExistencia(producto, ubicacion);

        if (existencia == null) {
            existencia = new ExistenciaInventario(producto, ubicacion, cantidad);
            ubicacion.agregarExistencia(existencia);
        } else {
            existencia.aumentarCantidad(cantidad);
        }

        MovimientoInventario movimiento = new MovimientoInventario(
                LocalDate.now(), cantidad, TipoMovimiento.REABASTECIMIENTO,
                producto, null, ubicacion);

        registrarMovimiento(movimiento);
    }

    public void trasladarProducto(Producto producto, UbicacionInventario origen,
            UbicacionInventario destino, int cantidad) {

        validarProductoYUbicacion(producto, origen);
        validarProductoYUbicacion(producto, destino);

        if (origen == destino) {
            throw new IllegalArgumentException("El origen y destino deben ser diferentes");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        ExistenciaInventario existenciaOrigen = buscarExistencia(producto, origen);

        if (existenciaOrigen == null || !existenciaOrigen.tieneCantidad(cantidad)) {
            throw new InventarioInsuficienteException(
                    "Inventario insuficiente para: " + producto.getNombre());
        }

        ExistenciaInventario existenciaDestino = buscarExistencia(producto, destino);

        existenciaOrigen.disminuirCantidad(cantidad);

        if (existenciaDestino == null) {
            existenciaDestino = new ExistenciaInventario(producto, destino, cantidad);
            destino.agregarExistencia(existenciaDestino);
        } else {
            existenciaDestino.aumentarCantidad(cantidad);
        }

        MovimientoInventario movimiento = new MovimientoInventario(
                LocalDate.now(), cantidad, TipoMovimiento.TRASLADO,
                producto, origen, destino);

        registrarMovimiento(movimiento);
    }

    private void validarProductoYUbicacion(Producto producto, UbicacionInventario ubicacion) {
        if (producto == null || ubicacion == null) {
            throw new IllegalArgumentException("El producto y la ubicación son obligatorios");
        }

        if (!productos.contains(producto)) {
            throw new IllegalArgumentException("El producto no está registrado");
        }

        if (!ubicacionesInventario.contains(ubicacion)) {
            throw new IllegalArgumentException("La ubicación no está registrada");
        }
    }

    // Ventas

    public void registrarVenta(Venta venta) {
        if (venta == null) {
            throw new IllegalArgumentException("La venta no puede ser nula");
        }

        ventas.add(venta);
    }

    public void procesarVenta(Venta venta, UbicacionInventario ubicacion) {
        if (venta == null || ubicacion == null) {
            throw new IllegalArgumentException("La venta y la ubicación son obligatorias");
        }

        if (!ubicacionesInventario.contains(ubicacion)) {
            throw new IllegalArgumentException("La ubicación no está registrada");
        }

        if (venta.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La venta debe contener productos");
        }

        Map<Producto, Integer> cantidades = new HashMap<Producto, Integer>();

        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            Integer acumulado = cantidades.get(producto);

            if (acumulado == null) {
                acumulado = 0;
            }

            cantidades.put(producto, acumulado + detalle.getCantidad());
        }

        for (Map.Entry<Producto, Integer> entrada : cantidades.entrySet()) {
            ExistenciaInventario existencia = buscarExistencia(entrada.getKey(), ubicacion);

            if (existencia == null || !existencia.tieneCantidad(entrada.getValue())) {
                throw new InventarioInsuficienteException(
                        "Inventario insuficiente para: " + entrada.getKey().getNombre());
            }
        }

        venta.calcularTotal();

        for (Map.Entry<Producto, Integer> entrada : cantidades.entrySet()) {
            Producto producto = entrada.getKey();
            int cantidad = entrada.getValue();
            ExistenciaInventario existencia = buscarExistencia(producto, ubicacion);

            existencia.disminuirCantidad(cantidad);

            MovimientoInventario movimiento = new MovimientoInventario(
                    LocalDate.now(), cantidad, TipoMovimiento.SALIDA,
                    producto, ubicacion, null);

            registrarMovimiento(movimiento);
        }

        if (venta.getComprador() instanceof Socio) {
            Socio socio = (Socio) venta.getComprador();
            socio.agregarPuntos(venta.getPuntosGenerados());
        }

        ventas.add(venta);
    }

    // Sugerencias de productos

    public void registrarSugerencia(SugerenciaProducto sugerencia) {
        if (sugerencia == null) {
            throw new IllegalArgumentException("La sugerencia no puede ser nula");
        }

        sugerenciasProducto.add(sugerencia);
    }

    public void aprobarSugerencia(SugerenciaProducto sugerencia,
            Administrador administrador, Producto producto) {

        if (sugerencia == null || administrador == null || producto == null) {
            throw new IllegalArgumentException("Los datos de aprobación son obligatorios");
        }

        if (!sugerenciasProducto.contains(sugerencia)) {
            throw new IllegalArgumentException("La sugerencia no está registrada");
        }

        sugerencia.aprobar(administrador, producto);
        registrarProducto(producto);
    }

    public void rechazarSugerencia(SugerenciaProducto sugerencia, Administrador administrador) {
        if (sugerencia == null || administrador == null) {
            throw new IllegalArgumentException("Los datos del rechazo son obligatorios");
        }

        if (!sugerenciasProducto.contains(sugerencia)) {
            throw new IllegalArgumentException("La sugerencia no está registrada");
        }

        sugerencia.rechazar(administrador);
    }

    // Reglas de productos en instalaciones

    public void validarIngresoProducto(Producto producto, Instalacion instalacion) {
        if (producto == null || instalacion == null) {
            throw new IllegalArgumentException("El producto y la instalación son obligatorios");
        }

        if (!(producto instanceof Bebida)) {
            return;
        }

        Bebida bebida = (Bebida) producto;

        if (!bebida.isCaliente()) {
            return;
        }

        boolean esPadel = instalacion.getTipo() == TipoInstalacion.CANCHA_PADEL;
        boolean esMesaTechada = instalacion.getTipo() == TipoInstalacion.MESA_TENIS_MESA
                && instalacion.isTechada();

        if (esPadel || esMesaTechada) {
            throw new IllegalArgumentException(
                    "No se permiten bebidas calientes en esta instalación");
        }
    }

    // Turnos

    public void asignarTurno(
            Empleado empleado, Turno turno) {

        if (empleado == null || turno == null) {
            throw new IllegalArgumentException(
                    "El empleado y el turno son obligatorios");
        }

        if (buscarUsuario(empleado.getId()) != empleado) {
            throw new IllegalArgumentException(
                    "El empleado no está registrado en el club");
        }

        if (turno.getHoraInicio().isBefore(horaApertura)
                || turno.getHoraFin().isAfter(horaCierre)) {

            throw new IllegalArgumentException(
                    "El turno debe estar dentro del horario del club");
        }

        empleado.agregarTurno(turno);
    }

    public SolicitudCambioTurno solicitarCambioTurno(
            Empleado empleado,
            Turno turnoOriginal,
            DiaSemana nuevoDia,
            LocalTime nuevaHoraInicio,
            LocalTime nuevaHoraFin) {

        if (empleado == null) {
            throw new IllegalArgumentException(
                    "El empleado es obligatorio");
        }

        if (buscarUsuario(empleado.getId()) != empleado) {
            throw new IllegalArgumentException(
                    "El empleado no está registrado en el club");
        }

        SolicitudCambioTurno solicitud = empleado.solicitarCambioTurno(
                turnoOriginal,
                nuevoDia,
                nuevaHoraInicio,
                nuevaHoraFin);

        solicitudesCambioTurno.add(solicitud);
        return solicitud;
    }

    public void registrarSolicitudCambio(
            SolicitudCambioTurno solicitud) {

        if (solicitud == null) {
            throw new IllegalArgumentException(
                    "La solicitud no puede ser nula");
        }

        Empleado empleado = solicitud.getEmpleadoSolicitante();

        if (buscarUsuario(empleado.getId()) != empleado) {
            throw new IllegalArgumentException(
                    "El empleado no está registrado en el club");
        }

        if (!empleado.getSolicitudesCambio().contains(solicitud)) {
            throw new IllegalArgumentException(
                    "La solicitud no pertenece al empleado");
        }

        if (!solicitudesCambioTurno.contains(solicitud)) {
            solicitudesCambioTurno.add(solicitud);
        }
    }

    public void aprobarCambioTurno(
            SolicitudCambioTurno solicitud,
            Administrador administrador) {

        validarSolicitudYAdministrador(
                solicitud,
                administrador);

        if (!validarCobertura(solicitud)) {
            throw new IllegalStateException(
                    "El cambio no conserva la cobertura mínima "
                            + "o presenta un cruce de horario");
        }

        administrador.aprobarSolicitud(solicitud);

        solicitud.getEmpleadoSolicitante()
                .aplicarCambioTurno(solicitud);
    }

    public void rechazarCambioTurno(
            SolicitudCambioTurno solicitud,
            Administrador administrador) {

        validarSolicitudYAdministrador(
                solicitud,
                administrador);

        administrador.rechazarSolicitud(solicitud);
    }

    private void validarSolicitudYAdministrador(
            SolicitudCambioTurno solicitud,
            Administrador administrador) {

        if (solicitud == null || administrador == null) {
            throw new IllegalArgumentException(
                    "La solicitud y el administrador son obligatorios");
        }

        if (!solicitudesCambioTurno.contains(solicitud)) {
            throw new IllegalArgumentException(
                    "La solicitud no está registrada en el club");
        }

        if (buscarUsuario(administrador.getId()) != administrador) {

            throw new IllegalArgumentException(
                    "El administrador no está registrado en el club");
        }

        if (!solicitud.estaPendiente()) {
            throw new IllegalStateException(
                    "La solicitud ya fue decidida");
        }
    }

    public boolean cumpleCoberturaMinima(Turno turno) {
        if (turno == null) {
            return false;
        }

        int entrenadores = 0;
        int fisioterapeutas = 0;

        for (Usuario usuario : usuarios) {
            if (!(usuario instanceof Empleado)) {
                continue;
            }

            Empleado empleado = (Empleado) usuario;

            if (!empleado.getTurnos().contains(turno)) {
                continue;
            }

            if (empleado instanceof Entrenador) {
                entrenadores++;
            } else if (empleado instanceof Fisioterapeuta) {
                fisioterapeutas++;
            }
        }

        return entrenadores >= minEntrenadoresEnTurno
                && fisioterapeutas >= minFisioterapeutasEnTurno;
    }

    public boolean validarCobertura(SolicitudCambioTurno solicitud) {
        if (solicitud == null) {
            return false;
        }

        Turno turnoOriginal = solicitud.getTurnoOriginal();
        Empleado solicitante = solicitud.getEmpleadoSolicitante();

        if (turnoOriginal == null || solicitante == null) {
            return false;
        }

        int entrenadores = 0;
        int fisioterapeutas = 0;

        for (Usuario usuario : usuarios) {
            if (!(usuario instanceof Empleado)) {
                continue;
            }

            Empleado empleado = (Empleado) usuario;
            boolean permanece = empleado != solicitante
                    && empleado.getTurnos().contains(turnoOriginal);

            if (!permanece) {
                continue;
            }

            if (empleado instanceof Entrenador) {
                entrenadores++;
            } else if (empleado instanceof Fisioterapeuta) {
                fisioterapeutas++;
            }
        }

        boolean conservaCobertura = entrenadores >= minEntrenadoresEnTurno
                && fisioterapeutas >= minFisioterapeutasEnTurno;

        boolean dentroHorario = !solicitud.getNuevaHoraInicio().isBefore(horaApertura)
                && !solicitud.getNuevaHoraFin().isAfter(horaCierre);

        Turno nuevoTurno = new Turno(solicitud.getNuevoDia(),
                solicitud.getNuevaHoraInicio(), solicitud.getNuevaHoraFin());

        for (Turno turno : solicitante.getTurnos()) {
            if (turno != turnoOriginal && turno.seCruzaCon(nuevoTurno)) {
                return false;
            }
        }

        return conservaCobertura && dentroHorario;
    }

    // Getters

    public String getNombre() {
        return nombre;
    }

    public String getInstitucion() {
        return institucion;
    }

    public LocalTime getHoraApertura() {
        return horaApertura;
    }

    public LocalTime getHoraCierre() {
        return horaCierre;
    }

    public int getMinEntrenadoresEnTurno() {
        return minEntrenadoresEnTurno;
    }

    public int getMinFisioterapeutasEnTurno() {
        return minFisioterapeutasEnTurno;
    }

    public ArrayList<Usuario> getUsuarios() {
        return new ArrayList<Usuario>(usuarios);
    }

    public ArrayList<DeporteConjunto> getDeportesConjunto() {
        return new ArrayList<DeporteConjunto>(deportesConjunto);
    }

    public ArrayList<DeporteIndividual> getDeportesIndividuales() {
        return new ArrayList<DeporteIndividual>(deportesIndividuales);
    }

    public ArrayList<Competencia> getCompetencias() {
        return new ArrayList<Competencia>(competencias);
    }

    public ArrayList<Partido> getPartidos() {
        return new ArrayList<Partido>(partidos);
    }

    public ArrayList<Instalacion> getInstalaciones() {
        return new ArrayList<Instalacion>(instalaciones);
    }

    public ArrayList<Reserva> getReservas() {
        return new ArrayList<Reserva>(reservas);
    }

    public ArrayList<SesionEntrenamiento> getSesionesEntrenamiento() {
        return new ArrayList<SesionEntrenamiento>(sesionesEntrenamiento);
    }

    public ArrayList<SesionEntrenamiento> getSesiones() {
        return getSesionesEntrenamiento();
    }

    public ArrayList<SolicitudCambioTurno> getSolicitudesCambioTurno() {
        return new ArrayList<SolicitudCambioTurno>(
                solicitudesCambioTurno);
    }

    public ArrayList<PagoMensualidad> getPagosMensualidad() {
        return new ArrayList<PagoMensualidad>(pagosMensualidad);
    }

    public ArrayList<Producto> getProductos() {
        return new ArrayList<Producto>(productos);
    }

    public ArrayList<UbicacionInventario> getUbicacionesInventario() {
        return new ArrayList<UbicacionInventario>(ubicacionesInventario);
    }

    public ArrayList<MovimientoInventario> getMovimientosInventario() {
        return new ArrayList<MovimientoInventario>(movimientosInventario);
    }

    public ArrayList<SugerenciaProducto> getSugerenciasProducto() {
        return new ArrayList<SugerenciaProducto>(sugerenciasProducto);
    }

    public ArrayList<Venta> getVentas() {
        return new ArrayList<Venta>(ventas);
    }
}

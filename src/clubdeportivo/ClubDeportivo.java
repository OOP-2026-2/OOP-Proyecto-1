package clubdeportivo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import clubdeportivo.deportes.DeporteConjunto;
import clubdeportivo.deportes.DeporteIndividual;
import clubdeportivo.entrenamientos.SesionEntrenamiento;
import clubdeportivo.instalaciones.Instalacion;
import clubdeportivo.instalaciones.Reserva;
import clubdeportivo.instalaciones.TipoInstalacion;
import clubdeportivo.membresias.PagoMensualidad;
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
    private final ArrayList<Instalacion> instalaciones;
    private final ArrayList<Reserva> reservas;
    private final ArrayList<SesionEntrenamiento> sesionesEntrenamiento;
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
        instalaciones = new ArrayList<Instalacion>();
        reservas = new ArrayList<Reserva>();
        sesionesEntrenamiento = new ArrayList<SesionEntrenamiento>();
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
            throw new IllegalArgumentException("El deporte no puede ser nulo");
        }

        if (!deportesConjunto.contains(deporte)) {
            deportesConjunto.add(deporte);
        }
    }

    public void registrarDeporteIndividual(DeporteIndividual deporte) {
        if (deporte == null) {
            throw new IllegalArgumentException("El deporte no puede ser nulo");
        }

        if (!deportesIndividuales.contains(deporte)) {
            deportesIndividuales.add(deporte);
        }
    }

    // Instalaciones y reservas

    public void registrarInstalacion(Instalacion instalacion) {
        if (instalacion == null) {
            throw new IllegalArgumentException("La instalación no puede ser nula");
        }

        if (buscarInstalacion(instalacion.getId()) != null) {
            throw new IllegalArgumentException("Ya existe una instalación con ese ID");
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

    public boolean consultarDisponibilidad(Instalacion instalacion, LocalDate fecha,
            LocalTime inicio, int duracionMinutos) {

        if (instalacion == null || fecha == null || inicio == null || duracionMinutos <= 0) {
            return false;
        }

        if (buscarInstalacion(instalacion.getId()) != instalacion) {
            return false;
        }

        if (!instalacion.estaDisponible(fecha, inicio, duracionMinutos)) {
            return false;
        }

        LocalTime fin = inicio.plusMinutes(duracionMinutos);

        for (SesionEntrenamiento sesion : sesionesEntrenamiento) {
            boolean mismaInstalacion = sesion.getInstalacion() == instalacion;
            boolean mismaFecha = sesion.getFecha().equals(fecha);
            boolean seCruzan = inicio.isBefore(sesion.calcularHoraFin())
                    && sesion.getHoraInicio().isBefore(fin);

            if (mismaInstalacion && mismaFecha && seCruzan) {
                return false;
            }
        }

        return true;
    }

    public void registrarReserva(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula");
        }

        Instalacion instalacion = reserva.getInstalacion();

        if (buscarInstalacion(instalacion.getId()) != instalacion) {
            throw new IllegalArgumentException("La instalación no está registrada");
        }

        if (!reserva.esFechaValida()) {
            throw new IllegalArgumentException("No se permiten reservas en fechas pasadas");
        }

        if (!reserva.cantidadJugadoresValida()) {
            throw new IllegalArgumentException("La cantidad de jugadores no es válida");
        }

        if (!consultarDisponibilidad(instalacion, reserva.getFecha(),
                reserva.getHoraInicio(), reserva.getDuracionMinutos())) {

            throw new ReservaNoDisponibleException("La instalación no está disponible");
        }

        instalacion.agregarReserva(reserva);
        reservas.add(reserva);
    }

    // Sesiones de entrenamiento

    public void registrarSesion(SesionEntrenamiento sesion) {
        programarSesion(sesion);
    }

    public void programarSesion(SesionEntrenamiento sesion) {
        if (sesion == null) {
            throw new IllegalArgumentException("La sesión no puede ser nula");
        }

        if (buscarInstalacion(sesion.getInstalacion().getId()) != sesion.getInstalacion()) {
            throw new IllegalArgumentException("La instalación no está registrada");
        }

        boolean dentroHorario = !sesion.getHoraInicio().isBefore(horaApertura)
                && !sesion.calcularHoraFin().isAfter(horaCierre);

        if (!dentroHorario) {
            throw new IllegalArgumentException("La sesión está fuera del horario del club");
        }

        if (!consultarDisponibilidad(sesion.getInstalacion(), sesion.getFecha(),
                sesion.getHoraInicio(), sesion.getDuracionMinutos())) {

            throw new IllegalArgumentException("La instalación no está disponible");
        }

        for (SesionEntrenamiento existente : sesionesEntrenamiento) {
            if (existente.seCruzaCon(sesion)) {
                throw new IllegalArgumentException("La sesión se cruza con otra sesión");
            }
        }

        sesionesEntrenamiento.add(sesion);
    }

    public ArrayList<SesionEntrenamiento> consultarSesiones(LocalDate fecha) {
        ArrayList<SesionEntrenamiento> resultado = new ArrayList<SesionEntrenamiento>();

        if (fecha == null) {
            return resultado;
        }

        for (SesionEntrenamiento sesion : sesionesEntrenamiento) {
            if (sesion.getFecha().equals(fecha)) {
                resultado.add(sesion);
            }
        }

        return resultado;
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
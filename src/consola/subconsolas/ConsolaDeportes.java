package consola.subconsolas;

import clubdeportivo.ClubDeportivo;
import clubdeportivo.deportes.Categoria;
import clubdeportivo.deportes.DeporteConjunto;
import clubdeportivo.deportes.DeporteIndividual;
import clubdeportivo.deportes.Equipo;
import clubdeportivo.deportes.InscripcionAcademia;
import clubdeportivo.deportes.InscripcionEquipo;
import clubdeportivo.deportes.Nivel;
import clubdeportivo.deportes.Posicion;
import consola.Subconsola;
import usuarios.tipos.Socio;

import static consola.InputHandler.*;

public class ConsolaDeportes extends Subconsola {
        public ConsolaDeportes(ClubDeportivo club) {
                super(club);
        }

        @Override
        public String getTitulo() {
                return "Deportes y equipos";
        }

        @Override
        protected String[] getOpciones() {
                return new String[] {
                                "Registrar deporte de conjunto",
                                "Registrar deporte individual",
                                "Registrar equipo",
                                "Inscribir socio en equipo",
                                "Inscribir socio en academia",
                                "Ver deportes y equipos",
                                "Ver inscripciones deportivas"
                };
        }

        @Override
        protected void ejecutarOpcion(int opcion) {
                switch (opcion) {
                        case 1:
                                registrarDeporteConjunto();
                                break;
                        case 2:
                                registrarDeporteIndividual();
                                break;
                        case 3:
                                registrarEquipo();
                                break;
                        case 4:
                                inscribirSocioEnEquipo();
                                break;
                        case 5:
                                inscribirSocioEnAcademia();
                                break;
                        case 6:
                                verDeportesYEquipos();
                                break;
                        case 7:
                                verInscripcionesDeportivas();
                                break;
                }
        }

        private void registrarDeporteConjunto() {
                System.out.println();
                System.out.println("--- REGISTRAR DEPORTE DE CONJUNTO ---");

                String nombre = readString("Nombre del deporte: ");
                int minimoJugadores = readInt("Mínimo de jugadores: ");
                int cantidadPosiciones = readInt("Cantidad de posiciones: ");

                try {
                        DeporteConjunto deporte = new DeporteConjunto(
                                        nombre, minimoJugadores);

                        for (int i = 1; i <= cantidadPosiciones; i++) {
                                String nombrePosicion = readString(
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

                String nombre = readString("Nombre del deporte: ");
                int maximoInscripciones = readInt(
                                "Máximo de inscripciones activas: ");

                try {
                        DeporteIndividual deporte = new DeporteIndividual(
                                        nombre,
                                        maximoInscripciones);

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

                String nombreDeporte = readString(
                                "Nombre del deporte de conjunto: ");

                DeporteConjunto deporte = club.buscarDeporteConjunto(nombreDeporte);

                if (deporte == null) {
                        System.out.println(
                                        "No existe ese deporte de conjunto.");
                        return;
                }

                String nombreEquipo = readString(
                                "Nombre del equipo: ");

                System.out.println("Categorías disponibles:");

                for (Categoria categoria : Categoria.values()) {
                        System.out.println("- " + categoria);
                }

                String categoriaTexto = readString(
                                "Categoría: ").toUpperCase();

                int cupoMaximo = readInt(
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

                Socio socio = seleccionarSocio("ID del socio: ");

                if (socio == null) {
                        return;
                }

                String nombreEquipo = readString(
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

                String nombrePosicion = readString("Posición: ");
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

                int numeroCamiseta = readInt(
                                "Número de camiseta: ");

                try {
                        InscripcionEquipo inscripcion = club.inscribirSocioEnEquipo(
                                        socio,
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

                Socio socio = seleccionarSocio("ID del socio: ");

                if (socio == null) {
                        return;
                }

                String nombreDeporte = readString(
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

                String nivelTexto = readString(
                                "Nivel: ").toUpperCase();

                try {
                        Nivel nivel = Nivel.valueOf(nivelTexto);

                        InscripcionAcademia inscripcion = club.inscribirSocioEnAcademia(
                                        socio,
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
                                                        + " | máximo de inscripciones: "
                                                        + deporte.getMaximoInscripciones());
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
}

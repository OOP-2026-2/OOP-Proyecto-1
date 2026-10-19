package consola.subconsolas;

import clubdeportivo.ClubDeportivo;
import consola.Subconsola;
import usuarios.Usuario;
import usuarios.tipos.Socio;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import clubdeportivo.membresias.PagoMensualidad;

import static consola.InputHandler.*;

public class ConsolaSocios extends Subconsola {
    public ConsolaSocios(ClubDeportivo club) {
        super(club);
    }

    @Override
    public String getTitulo() {
        return "Socios y usuarios";
    }

    @Override
    protected String[] getOpciones() {
        return new String[] {
                "Registrar socio",
                "Consultar usuario",
                "Registrar pago de mensualidad",
                "Ver pagos de mensualidad"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                registrarSocio();
                break;
            case 2:
                consultarUsuario();
                break;
            case 3:
                registrarPagoMensualidad();
                break;
            case 4:
                verPagosMensualidad();
                break;
        }
    }

    private void registrarSocio() {
        System.out.println();
        System.out.println("--- REGISTRAR SOCIO ---");

        String id = readString("ID: ");
        String nombre = readString("Nombre: ");
        String fechaTexto = readString("Fecha de nacimiento (AAAA-MM-DD): ");
        String login = readString("Login: ");
        String contrasena = readString("Contraseña: ");

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

        String id = readString("ID del usuario: ");
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

    private void registrarPagoMensualidad() {
        System.out.println();
        System.out.println("--- REGISTRAR PAGO DE MENSUALIDAD ---");

        Socio socio = seleccionarSocio("ID del socio: ");

        if (socio == null) {
            return;
        }

        String periodo = readString("Periodo del pago: ");
        double valor = readDouble("Valor de la mensualidad: ");

        try {
            PagoMensualidad pago = new PagoMensualidad(
                    LocalDate.now(), periodo, valor, socio);

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
}

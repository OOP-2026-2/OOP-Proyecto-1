package consola;

import clubdeportivo.ClubDeportivo;
import usuarios.Usuario;
import usuarios.tipos.Empleado;
import usuarios.tipos.Socio;

import static consola.InputHandler.*;

public abstract class Subconsola {
    protected final ClubDeportivo club;

    protected Subconsola(ClubDeportivo club) {
        this.club = club;
    }

    public abstract String getTitulo();
    protected abstract String[] getOpciones();
    protected abstract void ejecutarOpcion(int opcion);

    public final void ejecutar() {
        String[] opciones = getOpciones();

        while (true) {
            System.out.println();
            System.out.println("--- " + getTitulo().toUpperCase() + " ---");

            for (int i = 0; i < opciones.length; i++) {
                System.out.println((i + 1) + ". " + opciones[i]);
            }
            System.out.println("0. Volver");

            int opcion = readInt("Seleccione una opción: ");

            if (opcion == 0) {
                return;
            }

            if (opcion < 1 || opcion > opciones.length) {
                System.out.println("Opción inválida.");
                continue;
            }

            ejecutarOpcion(opcion);
        }
    }

    protected Socio seleccionarSocio(String prompt) {
        String id = readString(prompt);
        Usuario usuario = club.buscarUsuario(id);

        if (!(usuario instanceof Socio)) {
            System.out.println("No existe un socio con ese ID.");
            return null;
        }

        return (Socio) usuario;
    }

    protected Empleado seleccionarEmpleado() {
        String id = readString("ID del empleado: ");
        Usuario usuario = club.buscarUsuario(id);

        if (!(usuario instanceof Empleado)) {
            System.out.println("No existe un empleado con ese ID.");
            return null;
        }

        return (Empleado) usuario;
    }
}

package consola;

import clubdeportivo.ClubDeportivo;
import consola.subconsolas.*;
import utilidades.persistencia.PersistenciaClub;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private ClubDeportivo club;
    private PersistenciaClub persistencia;
    private boolean ejecutando;
    private List<Subconsola> secciones;

    public Main() {
        new InputHandler(new Scanner(System.in));

        club = new ClubDeportivo();
        persistencia = new PersistenciaClub();
        ejecutando = true;

        cargarDatos();

        secciones = List.of(
                new ConsolaSocios(club),
                new ConsolaComercial(club),
                new ConsolaInstalaciones(club),
                new ConsolaDeportes(club),
                new ConsolaCompetencias(club),
                new ConsolaEntrenamientos(club),
                new ConsolaEmpleados(club));
    }

    public void ejecutar() {
        System.out.println("================================");
        System.out.println("   CLUB DEPORTIVO SÉNECA");
        System.out.println("================================");

        while (ejecutando) {
            mostrarMenu();

            int opcion = InputHandler.readInt("Seleccione una sección: ");

            ejecutarOpcion(opcion);
        }

        guardarDatos();

        System.out.println("Programa finalizado.");
    }

    private void mostrarMenu() {
        System.out.println();

        for (int i = 0; i < secciones.size(); i++) {
            System.out.println((i + 1) + ". " + secciones.get(i).getTitulo());
        }
        System.out.println("0. Salir");
    }

    private void ejecutarOpcion(int opcion) {
        if (opcion == 0) {
            ejecutando = false;
        } else if (opcion >= 1 && opcion <= secciones.size()) {
            secciones.get(opcion - 1).ejecutar();
        } else {
            System.out.println("Opción inválida.");
        }
    }

    private void cargarDatos() {
        try {
            persistencia.cargar(club);
            System.out.println("Datos cargados correctamente.");
        } catch (IOException | RuntimeException e) {
            System.out.println("No fue posible cargar los datos: " + e.getMessage());
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

    public static void main(String[] args) {
        new Main().ejecutar();
    }
}

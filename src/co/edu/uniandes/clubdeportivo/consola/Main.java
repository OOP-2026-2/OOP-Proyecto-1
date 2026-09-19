package co.edu.uniandes.clubdeportivo.consola;

import java.time.LocalDate;
import co.edu.uniandes.clubdeportivo.usuarios.Administrador;
import co.edu.uniandes.clubdeportivo.usuarios.Entrenador;
import co.edu.uniandes.clubdeportivo.usuarios.Fisioterapeuta;
import co.edu.uniandes.clubdeportivo.usuarios.Socio;

public class Main {
    public static void main(String[] args) {
        Socio socio = new Socio(
                "S001",
                "Samuel",
                LocalDate.of(2007, 5, 10));

        Entrenador entrenador = new Entrenador(
                "E001",
                "Carlos",
                "DESC-CARLOS");

        Fisioterapeuta fisioterapeuta = new Fisioterapeuta(
                "F001",
                "Laura",
                "DESC-LAURA");

        Administrador administrador = new Administrador(
                "A001",
                "Andrea");

        socio.agregarPuntos(20);

        System.out.println(socio);
        System.out.println("Puntos del socio: " + socio.getPuntosFidelidad());
        System.out.println(entrenador);
        System.out.println(fisioterapeuta);
        System.out.println(administrador);
    }
}

package co.edu.uniandes.clubdeportivo.persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import co.edu.uniandes.clubdeportivo.ClubDeportivo;
import co.edu.uniandes.clubdeportivo.inventario.ArticuloTienda;
import co.edu.uniandes.clubdeportivo.inventario.ExistenciaInventario;
import co.edu.uniandes.clubdeportivo.inventario.Producto;
import co.edu.uniandes.clubdeportivo.inventario.UbicacionInventario;
import co.edu.uniandes.clubdeportivo.usuarios.Socio;
import co.edu.uniandes.clubdeportivo.usuarios.Usuario;

public class PersistenciaClub {
    private static final Path CARPETA = Paths.get("datos");
    private static final Path ARCHIVO_SOCIOS = CARPETA.resolve("socios.txt");
    private static final Path ARCHIVO_PRODUCTOS = CARPETA.resolve("productos.txt");
    private static final Path ARCHIVO_INVENTARIO = CARPETA.resolve("inventario.txt");

    public void guardar(ClubDeportivo club) throws IOException {
        Files.createDirectories(CARPETA);
        guardarSocios(club);
        guardarProductos(club);
        guardarInventario(club);
    }

    public void cargar(ClubDeportivo club) throws IOException {
        Files.createDirectories(CARPETA);
        cargarSocios(club);
        cargarProductos(club);
        cargarInventario(club);
    }

    private void guardarSocios(ClubDeportivo club) throws IOException {
        BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_SOCIOS);

        try {
            for (Usuario usuario : club.getUsuarios()) {
                if (usuario instanceof Socio) {
                    Socio socio = (Socio) usuario;

                    escritor.write(
                            socio.getId() + ";"
                                    + socio.getNombre() + ";"
                                    + socio.getFechaNacimiento() + ";"
                                    + socio.getPuntosFidelidad());

                    escritor.newLine();
                }
            }
        } finally {
            escritor.close();
        }
    }

    private void guardarProductos(ClubDeportivo club) throws IOException {
        BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_PRODUCTOS);

        try {
            for (Producto producto : club.getProductos()) {
                if (producto instanceof ArticuloTienda) {
                    ArticuloTienda articulo = (ArticuloTienda) producto;

                    escritor.write(
                            articulo.getNombre() + ";"
                                    + articulo.getPrecio() + ";"
                                    + articulo.getCategoria());

                    escritor.newLine();
                }
            }
        } finally {
            escritor.close();
        }
    }

    private void guardarInventario(ClubDeportivo club) throws IOException {
        BufferedWriter escritor = Files.newBufferedWriter(ARCHIVO_INVENTARIO);

        try {
            for (ExistenciaInventario existencia : club.getExistencias()) {
                escritor.write(
                        existencia.getProducto().getNombre() + ";"
                                + existencia.getUbicacion().getNombre() + ";"
                                + existencia.getCantidad());

                escritor.newLine();
            }
        } finally {
            escritor.close();
        }
    }

    private void cargarSocios(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_SOCIOS)) {
            return;
        }

        BufferedReader lector = Files.newBufferedReader(ARCHIVO_SOCIOS);

        try {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";");

                Socio socio = new Socio(
                        datos[0],
                        datos[1],
                        LocalDate.parse(datos[2]));

                socio.agregarPuntos(Double.parseDouble(datos[3]));
                club.registrarUsuario(socio);
            }
        } finally {
            lector.close();
        }
    }

    private void cargarProductos(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_PRODUCTOS)) {
            return;
        }

        BufferedReader lector = Files.newBufferedReader(ARCHIVO_PRODUCTOS);

        try {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";");

                ArticuloTienda producto = new ArticuloTienda(
                        datos[0],
                        Double.parseDouble(datos[1]),
                        datos[2]);

                club.registrarProducto(producto);
            }
        } finally {
            lector.close();
        }
    }

    private void cargarInventario(ClubDeportivo club) throws IOException {
        if (!Files.exists(ARCHIVO_INVENTARIO)) {
            return;
        }

        Map<String, UbicacionInventario> ubicaciones = new HashMap<String, UbicacionInventario>();

        BufferedReader lector = Files.newBufferedReader(ARCHIVO_INVENTARIO);

        try {
            String linea;

            while ((linea = lector.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";");
                Producto producto = buscarProducto(club, datos[0]);

                if (producto == null) {
                    continue;
                }

                UbicacionInventario ubicacion = ubicaciones.get(datos[1]);

                if (ubicacion == null) {
                    ubicacion = new UbicacionInventario(datos[1]);
                    ubicaciones.put(datos[1], ubicacion);
                }

                int cantidad = Integer.parseInt(datos[2]);
                club.reabastecerProducto(producto, ubicacion, cantidad);
            }
        } finally {
            lector.close();
        }
    }

    private Producto buscarProducto(ClubDeportivo club, String nombre) {
        for (Producto producto : club.getProductos()) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                return producto;
            }
        }

        return null;
    }
}
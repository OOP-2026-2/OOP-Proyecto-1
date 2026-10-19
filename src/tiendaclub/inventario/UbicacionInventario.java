package tiendaclub.inventario;

import java.util.ArrayList;
import java.util.List;

public class UbicacionInventario {
    private final String nombre;
    private final List<ExistenciaInventario> existencias;

    public UbicacionInventario(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre de la ubicación es obligatorio");
        }

        this.nombre = nombre;
        this.existencias = new ArrayList<ExistenciaInventario>();
    }

    public void agregarExistencia(
            ExistenciaInventario existencia) {

        if (existencia == null) {
            throw new IllegalArgumentException(
                    "La existencia no puede ser nula");
        }

        if (existencia.getUbicacion() != this) {
            throw new IllegalArgumentException(
                    "La existencia pertenece a otra ubicación");
        }

        ExistenciaInventario existente = buscarExistencia(
                existencia.getProducto());

        if (existente != null) {
            throw new IllegalArgumentException(
                    "El producto ya tiene una existencia "
                            + "en esta ubicación");
        }

        existencias.add(existencia);
    }

    public ExistenciaInventario buscarExistencia(
            Producto producto) {

        if (producto == null) {
            return null;
        }

        for (ExistenciaInventario existencia : existencias) {

            if (existencia.getProducto() == producto) {
                return existencia;
            }
        }

        return null;
    }

    public String getNombre() {
        return nombre;
    }

    public List<ExistenciaInventario> getExistencias() {

        return new ArrayList<ExistenciaInventario>(
                existencias);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
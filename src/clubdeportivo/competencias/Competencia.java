package clubdeportivo.competencias;

import java.util.ArrayList;

import clubdeportivo.competencias.Partidos.PartidoOficial;
import clubdeportivo.deportes.Categoria;
import clubdeportivo.deportes.DeporteConjunto;

public class Competencia {
    private String nombre;
    private String organizador;
    private int anio;
    private Categoria categoria;
    private String formato;
    private DeporteConjunto deporte;
    private ArrayList<PartidoOficial> partidos;

    public Competencia(String nombre, String organizador, int anio, Categoria categoria, String formato,
            DeporteConjunto deporte) {
        this.nombre = nombre;
        this.organizador = organizador;
        this.anio = anio;
        this.categoria = categoria;
        this.formato = formato;
        this.deporte = deporte;
        this.partidos = new ArrayList<PartidoOficial>();
    }

    public void agregarPartido(PartidoOficial partido) {
        if (partido != null) {
            partidos.add(partido);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public String getOrganizador() {
        return organizador;
    }

    public int getAnio() {
        return anio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getFormato() {
        return formato;
    }

    public DeporteConjunto getDeporte() {
        return deporte;
    }

    public ArrayList<PartidoOficial> getPartidos() {
        return new ArrayList<PartidoOficial>(partidos);
    }
}

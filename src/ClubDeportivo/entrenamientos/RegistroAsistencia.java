package ClubDeportivo.entrenamientos;

import ClubDeportivo.usuarios.Socio;

public class RegistroAsistencia {
    private SesionEntrenamiento sesion;
    private Socio socio;
    private EstadoAsistencia estado;
    private Double notaDesempeno;
    private String comentario;

    public RegistroAsistencia(SesionEntrenamiento sesion, Socio socio, EstadoAsistencia estado) {
        this.sesion = sesion;
        this.socio = socio;
        this.estado = estado;
        this.notaDesempeno = null;
        this.comentario = null;
    }

    public void registrarEvaluacion(Double notaDesempeno, String comentario) {
        if (notaDesempeno != null
                && (notaDesempeno < 1 || notaDesempeno > 5)) {
            throw new IllegalArgumentException(
                    "La nota debe estar entre 1 y 5");
        }

        this.notaDesempeno = notaDesempeno;
        this.comentario = comentario;
    }

    public SesionEntrenamiento getSesion() {
        return sesion;
    }

    public Socio getSocio() {
        return socio;
    }

    public EstadoAsistencia getEstado() {
        return estado;
    }

    public Double getNotaDesempeno() {
        return notaDesempeno;
    }

    public String getComentario() {
        return comentario;
    }
}

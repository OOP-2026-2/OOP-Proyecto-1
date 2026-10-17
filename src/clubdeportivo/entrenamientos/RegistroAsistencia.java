package clubdeportivo.entrenamientos;

import usuarios.tipos.Socio;

public class RegistroAsistencia {
    private final SesionEntrenamiento sesion;
    private final Socio socio;
    private EstadoAsistencia estado;
    private Double notaDesempeno;
    private String comentario;

    public RegistroAsistencia(SesionEntrenamiento sesion, Socio socio, EstadoAsistencia estado) {

        if (sesion == null) {
            throw new IllegalArgumentException(
                    "La sesión de entrenamiento es obligatoria");
        }

        if (socio == null) {
            throw new IllegalArgumentException(
                    "El socio es obligatorio");
        }

        if (estado == null) {
            throw new IllegalArgumentException(
                    "El estado de asistencia es obligatorio");
        }

        this.sesion = sesion;
        this.socio = socio;
        this.estado = estado;
        this.notaDesempeno = null;
        this.comentario = null;
    }

    public void registrarAsistencia(
            EstadoAsistencia estado) {

        if (estado == null) {
            throw new IllegalArgumentException(
                    "El estado de asistencia es obligatorio");
        }

        this.estado = estado;
    }

    public void registrarEvaluacion(Double notaDesempeno, String comentario) {

        if (estado != EstadoAsistencia.ASISTIO && estado != EstadoAsistencia.TARDE) {
            throw new IllegalStateException("Solo se puede evaluar a un socio " + "que asistió o llegó tarde");
        }

        if (notaDesempeno == null) {
            throw new IllegalArgumentException(
                    "La nota de desempeño es obligatoria");
        }

        if (notaDesempeno < 1
                || notaDesempeno > 5) {

            throw new IllegalArgumentException(
                    "La nota debe estar entre 1 y 5");
        }

        this.notaDesempeno = notaDesempeno;
        this.comentario = comentario;
    }

    public boolean notaValida() {
        return notaDesempeno == null
                || (notaDesempeno >= 1
                        && notaDesempeno <= 5);
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

    @Override
    public String toString() {

        String resultado = socio.getNombre() + " - " + estado;

        if (notaDesempeno != null) {
            resultado += " - nota: " + notaDesempeno;
        }

        return resultado;
    }
}
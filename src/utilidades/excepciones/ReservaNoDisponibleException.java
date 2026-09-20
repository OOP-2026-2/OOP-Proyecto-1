package utilidades.excepciones;

public class ReservaNoDisponibleException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ReservaNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}

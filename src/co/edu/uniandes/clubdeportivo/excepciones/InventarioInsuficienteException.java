package co.edu.uniandes.clubdeportivo.excepciones;

public class InventarioInsuficienteException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InventarioInsuficienteException(String mensaje) {
        super(mensaje);
    }
}

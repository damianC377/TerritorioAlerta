package TerritorioAlerta.application.exception;

/** Excepción para representar incumplimientos de reglas de negocio. */
public class BusinessException extends Exception {

    /** Crea una excepción de negocio con el mensaje indicado. */
    public BusinessException(String message) {
        super(message);
    }
    
}

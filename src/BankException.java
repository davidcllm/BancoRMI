import java.io.Serializable;

/**
 Excepcion personalizada para errores del dominio bancario.
 */
public class BankException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public BankException(String message) {
        super(message);
    }
}
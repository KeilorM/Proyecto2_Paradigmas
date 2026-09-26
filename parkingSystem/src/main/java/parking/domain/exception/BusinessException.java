package parking.domain.exception;

/**
 * Common root of every business-rule violation raised by the parking
 * lot domain. Catching this type is enough to intercept any business
 * rule failure without needing to know each specific subclass.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a business exception with the given explanatory message.
     *
     * @param message human-readable description of the violated rule
     */
    public BusinessException(String message) {
        super(message);
    }
}

package parking.domain.exception;

/**
 * Thrown when attempting to register an exit for a vehicle that has no
 * currently active ticket.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class TicketNotActiveException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param plate the plate of the vehicle with no active ticket
     */
    public TicketNotActiveException(String plate) {
        super("There is no active ticket for vehicle " + plate);
    }
}

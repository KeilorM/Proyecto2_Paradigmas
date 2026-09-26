package parking.domain.exception;

/**
 * Thrown when attempting to pay a ticket that has not been closed yet
 * (business rule: "an active ticket cannot be paid").
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class TicketStillActiveException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param ticketNumber the number of the still-active ticket
     */
    public TicketStillActiveException(int ticketNumber) {
        super("Ticket #" + ticketNumber + " is still active, it cannot be paid");
    }
}

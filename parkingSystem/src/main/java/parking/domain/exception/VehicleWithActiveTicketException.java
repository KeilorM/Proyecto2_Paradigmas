package parking.domain.exception;

/**
 * Thrown when a vehicle attempts to check in while it already has an
 * {@code ACTIVE} ticket open (assignment rule: "a vehicle cannot have
 * two active tickets").
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class VehicleWithActiveTicketException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param plate the plate of the vehicle that already has an active ticket
     */
    public VehicleWithActiveTicketException(String plate) {
        super("Vehicle " + plate + " already has an active ticket");
    }
}

package parking.domain.model;

/**
 * Lifecycle states of a {@link ParkingTicket}.
 * <p>
 * The valid transition path is strictly
 * {@code ACTIVE -> CLOSED -> PAID}. A ticket can only be paid once it is
 * {@code CLOSED}, and a vehicle can only check in again once its
 * previous ticket has left the {@code ACTIVE} state.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public enum TicketStatus {

    /** The vehicle is currently inside the lot; the stay is
     * still being timed. */
    ACTIVE,

    /** The vehicle has exited and the final amount has been calculated,
     * but not yet paid. */
    CLOSED,

    /** The ticket's amount has been collected. */
    PAID
}

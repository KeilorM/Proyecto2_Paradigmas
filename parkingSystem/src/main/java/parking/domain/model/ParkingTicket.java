package parking.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Represents one vehicle's stay in the parking lot, from entry to
 * closing.
 * <p>
 * A ticket owns its {@link TicketStatus} lifecycle
 * ({@code ACTIVE -> CLOSED -> PAID}) and is the only class allowed to
 * compute its own {@link #getAmount() amount}: {@link #close(LocalDateTime)}
 * always delegates that calculation to the ticket's vehicle's
 * {@link Vehicle#getRate() rate}, never hard-coding or duplicating the
 * pricing logic itself.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class ParkingTicket {

    private final int number;
    private final Vehicle vehicle;
    private final ParkingSpace space;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private TicketStatus status;
    private long amount;

    /**
     * Opens a new, {@link TicketStatus#ACTIVE active} ticket.
     *
     * @param number    unique ticket number
     * @param vehicle   the vehicle that is entering
     * @param space     the space assigned to the vehicle
     * @param entryTime the moment the vehicle entered
     */
    public ParkingTicket(int number, Vehicle vehicle, ParkingSpace space,
            LocalDateTime entryTime) {
        this.number = number;
        this.vehicle = vehicle;
        this.space = space;
        this.entryTime = entryTime;
        this.status = TicketStatus.ACTIVE;
        this.amount = 0;
    }

    /**
     * Closes the ticket: records the exit time, computes the final
     * amount via {@link Vehicle#getRate()}, and moves the ticket to
     * {@link TicketStatus#CLOSED}.
     *
     * @param exitTime the moment the vehicle left
     * @return the computed amount, in the same units as 
     * {@link Rate#calculateAmount(long)}
     * @throws IllegalStateException if the ticket is not currently 
     * {@link TicketStatus#ACTIVE}
     */
    public long close(LocalDateTime exitTime) {
        if (status != TicketStatus.ACTIVE) {
            throw new IllegalStateException("Ticket " + number +
                    " is not active");
        }
        this.exitTime = exitTime;
        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        this.amount = vehicle.getRate().calculateAmount(minutes);
        this.status = TicketStatus.CLOSED;
        return this.amount;
    }

    /**
     * Marks the ticket as paid.
     *
     * @throws IllegalStateException if the ticket is not currently 
     * {@link TicketStatus#CLOSED}
     */
    public void markAsPaid() {
        if (status != TicketStatus.CLOSED) {
            throw new IllegalStateException("Ticket " + number +
                    " must be closed before it can be paid");
        }
        this.status = TicketStatus.PAID;
    }

    /**
     * Computes how long the vehicle has stayed so far. If the ticket has
     * not been closed yet, the stay is measured up to the current moment.
     *
     * @return minutes elapsed between entry and exit (or now, if still active)
     */
    public long getStayInMinutes() {
        LocalDateTime until = (exitTime != null) ? exitTime :
                LocalDateTime.now();
        return Duration.between(entryTime, until).toMinutes();
    }

    /**
     * @return this ticket's unique number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @return the vehicle associated with this ticket
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * @return the space assigned to this ticket
     */
    public ParkingSpace getSpace() {
        return space;
    }

    /**
     * @return the moment the vehicle entered
     */
    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    /**
     * @return the moment the vehicle exited, or {@code null} 
     * if the ticket is still active
     */
    public LocalDateTime getExitTime() {
        return exitTime;
    }

    /**
     * @return this ticket's current status
     */
    public TicketStatus getStatus() {
        return status;
    }

    /**
     * @return the amount computed when the ticket was closed, or 
     * {@code 0} while still active
     */
    public long getAmount() {
        return amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Ticket #" + number + " - " + vehicle.getPlate() +
                " - " + status + " - amount: " + amount;
    }
}

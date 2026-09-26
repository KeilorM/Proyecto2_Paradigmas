package parking.domain.repository;

import parking.domain.model.ParkingTicket;

import java.util.List;
import java.util.Optional;

/**
 * Persistence port for {@link ParkingTicket} instances, including the
 * generation of unique ticket numbers.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public interface ParkingTicketRepository {

    /**
     * Persists a ticket. Safe to call again on a ticket that has already
     * been saved, since in this system tickets are mutated in place.
     *
     * @param ticket the ticket to store
     */
    void save(ParkingTicket ticket);

    /**
     * @return every ticket ever created, in any status
     */
    List<ParkingTicket> findAll();

    /**
     * @return every ticket currently in {@code ACTIVE} status
     */
    List<ParkingTicket> findActive();

    /**
     * Finds the active ticket, if any, for the vehicle with the given plate.
     *
     * @param plate the plate to search for
     * @return the matching active ticket, or {@link Optional#empty()} 
     * if the vehicle is not currently parked
     */
    Optional<ParkingTicket> findActiveByPlate(String plate);

    /**
     * Finds a ticket by its unique number, in any status.
     *
     * @param number the ticket number to search for
     * @return the matching ticket, or {@link Optional#empty()} 
     * if no ticket has that number
     */
    Optional<ParkingTicket> findByNumber(int number);

    /**
     * Reserves and returns the next unique ticket number.
     *
     * @return a ticket number not previously returned 
     * by this repository instance
     */
    int nextTicketNumber();
}

package parking.application.usecase;

import parking.domain.exception.TicketNotActiveException;
import parking.domain.model.ParkingTicket;
import parking.domain.repository.ParkingTicketRepository;

import java.time.LocalDateTime;

/**
 * Checks a vehicle out of the parking lot: closes its active ticket
 * (which computes the final amount) and releases the space it occupied.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class CheckOutVehicleUseCase {

    private final ParkingTicketRepository ticketRepository;

    /**
     * @param ticketRepository port used to find the vehicle's active ticket
     */
    public CheckOutVehicleUseCase(ParkingTicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Executes the check-out.
     *
     * @param plate    the plate of the exiting vehicle
     * @param exitTime the moment of exit
     * @return the now {@code CLOSED} ticket, with its final amount computed
     * @throws TicketNotActiveException if the vehicle has no active ticket
     */
    public ParkingTicket execute(String plate, LocalDateTime exitTime) {
        ParkingTicket ticket = ticketRepository.findActiveByPlate(plate)
                .orElseThrow(() -> new TicketNotActiveException(plate));

        ticket.close(exitTime);
        ticket.getSpace().release();
        return ticket;
    }
}

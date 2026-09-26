package parking.application.usecase;

import parking.domain.model.ParkingTicket;
import parking.domain.repository.ParkingTicketRepository;

import java.util.Optional;

/**
 * Looks up a ticket by its number, regardless of status.
 * <p>
 * Used by delivery mechanisms (such as an interactive menu) that only
 * have the ticket number typed by the user and need the actual
 * {@link ParkingTicket} instance to pass into
 * {@link RegisterPaymentUseCase}.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class GetTicketByNumberUseCase {

    private final ParkingTicketRepository ticketRepository;

    /**
     * @param ticketRepository port used to query tickets
     */
    public GetTicketByNumberUseCase(ParkingTicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * @param number the ticket number to search for
     * @return the matching ticket, or {@link Optional#empty()} 
     * if no ticket has that number
     */
    public Optional<ParkingTicket> execute(int number) {
        return ticketRepository.findByNumber(number);
    }
}

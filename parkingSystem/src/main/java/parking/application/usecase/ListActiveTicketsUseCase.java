package parking.application.usecase;

import parking.domain.model.ParkingTicket;
import parking.domain.repository.ParkingTicketRepository;

import java.util.List;

/**
 * Lists every currently active ticket.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class ListActiveTicketsUseCase {

    private final ParkingTicketRepository ticketRepository;

    /**
     * @param ticketRepository port used to query active tickets
     */
    public ListActiveTicketsUseCase(ParkingTicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * @return every ticket currently in {@code ACTIVE} status
     */
    public List<ParkingTicket> execute() {
        return ticketRepository.findActive();
    }
}

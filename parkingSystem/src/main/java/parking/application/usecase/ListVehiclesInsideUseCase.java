package parking.application.usecase;

import parking.domain.model.ParkingTicket;
import parking.domain.model.Vehicle;
import parking.domain.repository.ParkingTicketRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Lists every vehicle that is currently inside the parking lot, i.e.
 * every vehicle whose ticket is still {@code ACTIVE}.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class ListVehiclesInsideUseCase {

    private final ParkingTicketRepository ticketRepository;

    /**
     * @param ticketRepository port used to query active tickets
     */
    public ListVehiclesInsideUseCase(ParkingTicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * @return the vehicles currently parked
     */
    public List<Vehicle> execute() {
        return ticketRepository.findActive().stream()
                .map(ParkingTicket::getVehicle)
                .collect(Collectors.toList());
    }
}

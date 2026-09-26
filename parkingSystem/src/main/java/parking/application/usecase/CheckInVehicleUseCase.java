package parking.application.usecase;

import parking.domain.exception.NoAvailableSpaceException;
import parking.domain.exception.VehicleWithActiveTicketException;
import parking.domain.model.ParkingSpace;
import parking.domain.model.ParkingTicket;
import parking.domain.model.Vehicle;
import parking.domain.repository.ParkingSpaceRepository;
import parking.domain.repository.ParkingTicketRepository;

import java.time.LocalDateTime;

/**
 * Checks a vehicle into the parking lot: validates it does not already
 * have an active ticket, finds and occupies a compatible available
 * space, and opens a new ticket for it.
 * <p>
 * This use case coordinates two repositories but performs no rate or
 * compatibility logic itself; that logic stays inside {@link Vehicle}
 * and {@link ParkingSpace} respectively.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class CheckInVehicleUseCase {

    private final ParkingSpaceRepository spaceRepository;
    private final ParkingTicketRepository ticketRepository;

    /**
     * @param spaceRepository  port used to find and update spaces
     * @param ticketRepository port used to persist the new ticket and check
     * for existing active ones
     */
    public CheckInVehicleUseCase(ParkingSpaceRepository spaceRepository,
            ParkingTicketRepository ticketRepository) {
        this.spaceRepository = spaceRepository;
        this.ticketRepository = ticketRepository;
    }

    /**
     * Executes the check-in.
     *
     * @param vehicle   the entering vehicle
     * @param entryTime the moment of entry
     * @return the newly opened, {@code ACTIVE} ticket
     * @throws VehicleWithActiveTicketException if the vehicle already has 
     * an active ticket
     * @throws NoAvailableSpaceException         
     * if there is no available space compatible with the vehicle
     */
    public ParkingTicket execute(Vehicle vehicle, LocalDateTime entryTime) {
        if (ticketRepository.findActiveByPlate(vehicle.getPlate()).isPresent()){
            throw new VehicleWithActiveTicketException(vehicle.getPlate());
        }

        ParkingSpace space = spaceRepository.findAvailableByType(
                vehicle.getCompatibleSpaceType())
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoAvailableSpaceException(
                        vehicle.getCompatibleSpaceType()));

        space.occupy();
        ParkingTicket ticket = new ParkingTicket(
                ticketRepository.nextTicketNumber(), vehicle, space, entryTime);
        ticketRepository.save(ticket);
        return ticket;
    }
}

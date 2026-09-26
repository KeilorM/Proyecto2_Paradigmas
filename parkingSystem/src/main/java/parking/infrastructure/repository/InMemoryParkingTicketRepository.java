package parking.infrastructure.repository;

import parking.domain.model.ParkingTicket;
import parking.domain.model.TicketStatus;
import parking.domain.repository.ParkingTicketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * In-memory {@link ParkingTicketRepository} implementation, backed by an
 * {@link ArrayList} and an internal, monotonically increasing counter
 * for ticket numbers.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class InMemoryParkingTicketRepository implements ParkingTicketRepository{

    private final List<ParkingTicket> tickets = new ArrayList<>();
    private int nextNumber = 1;

    /**
     * {@inheritDoc}
     * <p>
     * If the ticket is already present (by reference), it is not added
     * again; this keeps {@code save} idempotent when a use case saves a
     * ticket it had already retrieved and mutated in place.
     */
    @Override
    public void save(ParkingTicket ticket) {
        if (!tickets.contains(ticket)) {
            tickets.add(ticket);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParkingTicket> findAll() {
        return List.copyOf(tickets);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParkingTicket> findActive() {
        return tickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ParkingTicket> findActiveByPlate(String plate) {
        return tickets.stream()
                .filter(t -> t.getStatus() == TicketStatus.ACTIVE &&
                        t.getVehicle().getPlate().equalsIgnoreCase(plate))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ParkingTicket> findByNumber(int number) {
        return tickets.stream()
                .filter(t -> t.getNumber() == number)
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int nextTicketNumber() {
        return nextNumber++;
    }
}

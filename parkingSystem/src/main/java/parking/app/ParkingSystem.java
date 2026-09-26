package parking.app;

import parking.application.usecase.*;
import parking.domain.repository.ParkingSpaceRepository;
import parking.domain.repository.ParkingTicketRepository;
import parking.domain.repository.PaymentRepository;
import parking.domain.repository.VehicleRepository;
import parking.infrastructure.repository.InMemoryParkingSpaceRepository;
import parking.infrastructure.repository.InMemoryParkingTicketRepository;
import parking.infrastructure.repository.InMemoryPaymentRepository;
import parking.infrastructure.repository.InMemoryVehicleRepository;

/**
 * Composition root of the application.
 * <p>
 * This is the single place where concrete {@code infrastructure}
 * classes are instantiated and wired into the {@code application}
 * use cases through the {@code domain} repository interfaces. Every
 * other class in the system - the use cases, the domain entities -
 * remains unaware of which concrete repository implementation is
 * actually running underneath.
 * <p>
 * Both {@link Main} and {@link ParkingTests} build one instance of this
 * class instead of constructing repositories and use cases by hand,
 * which keeps the wiring logic in exactly one place.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class ParkingSystem {

    private final VehicleRepository vehicleRepository = 
            new InMemoryVehicleRepository();
    private final ParkingSpaceRepository spaceRepository =
            new InMemoryParkingSpaceRepository();
    private final ParkingTicketRepository ticketRepository =
            new InMemoryParkingTicketRepository();
    private final PaymentRepository paymentRepository =
            new InMemoryPaymentRepository();

    /** Registers a new vehicle in the system. */
    public final RegisterVehicleUseCase registerVehicle =
            new RegisterVehicleUseCase(vehicleRepository);

    /** Registers a new physical parking space. */
    public final RegisterParkingSpaceUseCase registerParkingSpace =
            new RegisterParkingSpaceUseCase(spaceRepository);

    /** Checks a vehicle into the lot, assigning it a compatible space and
     * opening a ticket. */
    public final CheckInVehicleUseCase checkInVehicle =
            new CheckInVehicleUseCase(spaceRepository, ticketRepository);

    /** Validates, in isolation, whether a specific space could be assigned
     * to a specific vehicle. */
    public final ValidateSpaceAssignmentUseCase validateSpaceAssignment =
            new ValidateSpaceAssignmentUseCase();

    /** Checks a vehicle out of the lot, closing its ticket and 
     * releasing its space. */
    public final CheckOutVehicleUseCase checkOutVehicle =
            new CheckOutVehicleUseCase(ticketRepository);

    /** Registers the payment for a closed ticket. */
    public final RegisterPaymentUseCase registerPayment =
            new RegisterPaymentUseCase(paymentRepository);

    /** Lists the currently available spaces of a given type. */
    public final ListAvailableSpacesUseCase listAvailableSpaces =
            new ListAvailableSpacesUseCase(spaceRepository);

    /** Lists the vehicles currently parked inside the lot. */
    public final ListVehiclesInsideUseCase listVehiclesInside =
            new ListVehiclesInsideUseCase(ticketRepository);

    /** Lists every currently active ticket. */
    public final ListActiveTicketsUseCase listActiveTickets =
            new ListActiveTicketsUseCase(ticketRepository);

    /** Computes space occupancy counts grouped by space type. */
    public final GetOccupancyByTypeUseCase getOccupancyByType =
            new GetOccupancyByTypeUseCase(spaceRepository);

    /** Computes the total revenue collected across every payment. */
    public final GetTotalRevenueUseCase getTotalRevenue =
            new GetTotalRevenueUseCase(paymentRepository);

    /** Lists every vehicle ever registered, parked or not. */
    public final ListRegisteredVehiclesUseCase listRegisteredVehicles =
            new ListRegisteredVehiclesUseCase(vehicleRepository);

    /** Lists every payment ever registered. */
    public final ListPaymentsUseCase listPayments =
            new ListPaymentsUseCase(paymentRepository);

    /** Looks up a registered vehicle by its plate. */
    public final GetVehicleByPlateUseCase getVehicleByPlate =
            new GetVehicleByPlateUseCase(vehicleRepository);

    /** Looks up a ticket by its number. */
    public final GetTicketByNumberUseCase getTicketByNumber =
            new GetTicketByNumberUseCase(ticketRepository);
}

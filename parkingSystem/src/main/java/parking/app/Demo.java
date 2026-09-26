package parking.app;

import parking.domain.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Scripted console demonstration that walks through every control the assignment
 * asks for in its problem description: registered vehicles, parking
 * spaces, vehicle entries, active tickets, exits, stay-time calculation,
 * amount due, payments made, current occupancy, and total revenue
 * generated.
 * <p>
 * Everything below is driven entirely through the use cases exposed by
 * {@link ParkingSystem}; this class contains no business logic of its
 * own. The formal, mandatory test cases live in {@link ParkingTests}.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class Demo {

    /**
     * Runs the demonstration.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        ParkingSystem system = new ParkingSystem();

        // ---------- 1. Parking spaces ----------
        section("1. Registering parking spaces");
        system.registerParkingSpace.execute(new ParkingSpace("A-1",
                SpaceType.CAR));
        system.registerParkingSpace.execute(new ParkingSpace("A-2",
                SpaceType.CAR));
        system.registerParkingSpace.execute(new ParkingSpace("M-1",
                SpaceType.MOTORCYCLE));
        system.registerParkingSpace.execute(new ParkingSpace("C-1",
                SpaceType.CARGO));
        System.out.println("4 spaces registered: A-1, A-2 (CAR),"
                + " M-1 (MOTORCYCLE), C-1 (CARGO)");

        // ---------- 2. Registered vehicles ----------
        section("2. Registering vehicles");
        Vehicle car1 = new Car("ABC123", "Toyota", "Corolla", "Red");
        Vehicle car2 = new Car("XYZ789", "Mazda", "3", "Black");
        Vehicle motorcycle = new Motorcycle("MOT111", "Honda", "CB190", "Blue");
        Vehicle cargoVehicle = new CargoVehicle(
                "CAR999", "Freightliner", "M2", "White");
        system.registerVehicle.execute(car1);
        system.registerVehicle.execute(car2);
        system.registerVehicle.execute(motorcycle);
        system.registerVehicle.execute(cargoVehicle);

        List<Vehicle> registered = system.listRegisteredVehicles.execute();
        System.out.println(registered.size() + " vehicles registered:");
        registered.forEach(v -> System.out.println("  - " + v));

        // ---------- 3. Vehicle entries ----------
        section("3. Registering entries (check-in)");
        LocalDateTime entryTime = LocalDateTime.of(2026, 9, 16, 8, 0);
        ParkingTicket ticket1 = system.checkInVehicle.execute(car1, entryTime);
        ParkingTicket ticket2 = system.checkInVehicle.execute(
                car2, entryTime.plusMinutes(15));
        ParkingTicket ticket3 = system.checkInVehicle.execute(
                motorcycle, entryTime.plusMinutes(30));
        System.out.println("Entry registered: " + ticket1);
        System.out.println("Entry registered: " + ticket2);
        System.out.println("Entry registered: " + ticket3);
        // cargoVehicle deliberately stays out for now, to show it later
        // as "registered but not parked".

        // ---------- 4. Active tickets ----------
        section("4. Querying active tickets");
        List<ParkingTicket> active = system.listActiveTickets.execute();
        System.out.println(active.size() + " active tickets:");
        active.forEach(t -> System.out.println("  - " + t));

        // ------ 5. Current occupancy (while vehicles are still inside) ------
        section("5. Current occupancy by space type");
        printOccupancy(system.getOccupancyByType.execute());

        // ---------- 6. Stay-time calculation ----------
        section("6. Stay-time calculation (before closing)");
        LocalDateTime checkTime = entryTime.plusHours(2).plusMinutes(10);
        System.out.println("Vehicle " + car1.getPlate() 
                + " has been parked for "
                + java.time.Duration.between(
                        ticket1.getEntryTime(), checkTime).toMinutes()
                + " minutes so far (ticket still active).");

        // ---------- 7. Exits + amount due ----------
        section("7. Registering exits (check-out) and computing the amount due");
        LocalDateTime exitTime1 = entryTime.plusHours(2).plusMinutes(30);
        LocalDateTime exitTime2 = entryTime.plusMinutes(15).plusHours(1);
        system.checkOutVehicle.execute(car1.getPlate(), exitTime1);
        system.checkOutVehicle.execute(car2.getPlate(), exitTime2);
        System.out.println("Exit registered for " + car1.getPlate() 
                + " -> amount due: " + ticket1.getAmount());
        System.out.println("Exit registered for " + car2.getPlate() 
                + " -> amount due: " + ticket2.getAmount());
        // motorcycle (ticket3) is left active on purpose, to show it is
        // still occupying a space after the exits above.

        // ---------- 8. Payments made ----------
        section("8. Registering and listing payments made");
        system.registerPayment.execute(ticket1, PaymentType.CARD, exitTime1);
        system.registerPayment.execute(ticket2, PaymentType.CASH, exitTime2);

        List<Payment> payments = system.listPayments.execute();
        System.out.println(payments.size() + " payments registered:");
        payments.forEach(p -> System.out.println("  - " + p));

        // ---------- 9. Current occupancy (after some exits) ----------
        section("9. Current occupancy by space type (after check-outs)");
        printOccupancy(system.getOccupancyByType.execute());

        System.out.println("Vehicles still inside: ");
        system.listVehiclesInside.execute().forEach(
                v -> System.out.println("  - " + v));

        // ---------- 10. Total revenue generated ----------
        section("10. Total revenue generated");
        System.out.println("Total revenue so far: " +
                system.getTotalRevenue.execute());
    }

    private static void printOccupancy(Map<SpaceType, long[]> occupancy) {
        occupancy.forEach((type, counts) ->
                System.out.println("  " + type + " -> available: " + counts[0]
                        + ", occupied: " + counts[1] +
                        ", out of service: " + counts[2]));
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("=== " + title + " ===");
    }
}

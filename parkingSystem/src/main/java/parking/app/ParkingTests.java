package parking.app;

import parking.domain.exception.*;
import parking.domain.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple test harness (no external dependencies) covering the 15
 * mandatory cases from section 12 of the assignment, exercised strictly
 * through the {@link ParkingSystem} use cases rather than by poking at
 * repositories or entities directly - the same way any real delivery
 * mechanism (a CLI, a REST controller) would use the application.
 * <p>
 * Each test builds a fresh {@link ParkingSystem} so tests never share
 * state, prints input, expected result and actual result, and at the
 * end prints a summary that can be pasted directly into the report's
 * test table.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class ParkingTests {

    private static int total = 0;
    private static int passed = 0;
    private static final List<String> tableRows = new ArrayList<>();

    /**
     * Runs every test case and prints a final summary.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        test1CarEntry();
        test2MotorcycleEntry();
        test3CargoEntry();
        test4OccupiedSpace();
        test5OutOfServiceSpace();
        test6IncompatibleSpace();
        test7VehicleWithActiveTicket();
        test8OneMinuteStay();
        test9SixtyMinuteStay();
        test10SixtyOneMinuteStay();
        test11StayWithDailyCap();
        test12CorrectTicketClosing();
        test13CorrectPayment();
        test14SpaceRelease();
        test15TotalRevenue();

        System.out.println();
        System.out.println("========================================");
        System.out.println("SUMMARY:" + passed + " / " + total + " tests passed");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Test table (copy into the report):");
        for (String row : tableRows) {
            System.out.println(row);
        }
    }

    // ---------- Entry cases ----------

    private static void test1CarEntry() {
        ParkingSystem system = new ParkingSystem();
        system.registerParkingSpace.execute(new ParkingSpace("A-1",
                SpaceType.CAR));
        Vehicle v = new Car("AAA111", "Toyota", "Corolla", "Red");
        system.registerVehicle.execute(v);
        ParkingTicket ticket = system.checkInVehicle.execute(
                v, LocalDateTime.now());
        verify("1. Correct entry of a car",
                "Ticket ACTIVE and space OCCUPIED",
                ticket.getStatus() == TicketStatus.ACTIVE &&
                        ticket.getSpace().getStatus() == SpaceStatus.OCCUPIED);
    }

    private static void test2MotorcycleEntry() {
        ParkingSystem system = new ParkingSystem();
        system.registerParkingSpace.execute(new ParkingSpace("M-1",
                SpaceType.MOTORCYCLE));
        Vehicle v = new Motorcycle("MOT111", "Honda", "CB190", "Black");
        system.registerVehicle.execute(v);
        ParkingTicket ticket = system.checkInVehicle.execute(
                v, LocalDateTime.now());
        verify("2. Correct entry of a motorcycle",
                "Ticket ACTIVE and space OCCUPIED",
                ticket.getStatus() == TicketStatus.ACTIVE &&
                        ticket.getSpace().getStatus() == SpaceStatus.OCCUPIED);
    }

    private static void test3CargoEntry() {
        ParkingSystem system = new ParkingSystem();
        system.registerParkingSpace.execute(new ParkingSpace(
                "C-1", SpaceType.CARGO));
        Vehicle v = new CargoVehicle("CAR111", "Freightliner", "M2", "White");
        system.registerVehicle.execute(v);
        ParkingTicket ticket = system.checkInVehicle.execute(
                v, LocalDateTime.now());
        verify("3. Correct entry of a cargo vehicle",
                "Ticket ACTIVE and space OCCUPIED",
                ticket.getStatus() == TicketStatus.ACTIVE &&
                        ticket.getSpace().getStatus() == SpaceStatus.OCCUPIED);
    }

    // ---------- Invalid assignment cases ----------

    private static void test4OccupiedSpace() {
        ParkingSystem system = new ParkingSystem();
        ParkingSpace space = new ParkingSpace("A-1", SpaceType.CAR);
        space.occupy(); // already occupied beforehand
        Vehicle v = new Car("BBB222", "Kia", "Rio", "Blue");

        boolean threwException = false;
        try {
            system.validateSpaceAssignment.execute(space, v);
        } catch (SpaceOccupiedException e) {
            threwException = true;
        }
        verify("4. Attempt to assign an occupied space",
                "SpaceOccupiedException",
                threwException);
    }

    private static void test5OutOfServiceSpace() {
        ParkingSystem system = new ParkingSystem();
        ParkingSpace space = new ParkingSpace("A-2", SpaceType.CAR);
        space.markOutOfService();
        Vehicle v = new Car("CCC333", "Nissan", "Sentra", "Gray");

        boolean threwException = false;
        try {
            system.validateSpaceAssignment.execute(space, v);
        } catch (SpaceOutOfServiceException e) {
            threwException = true;
        }
        verify("5. Attempt to assign a space that is out of service",
                "SpaceOutOfServiceException",
                threwException);
    }

    private static void test6IncompatibleSpace() {
        ParkingSystem system = new ParkingSystem();
        ParkingSpace space = new ParkingSpace("M-2", SpaceType.MOTORCYCLE);
        Vehicle v = new Car("DDD444", "Mazda", "3", "Black");

        boolean threwException = false;
        try {
            system.validateSpaceAssignment.execute(space, v);
        } catch (IncompatibleSpaceException e) {
            threwException = true;
        }
        verify("6. Attempt to assign an incompatible space",
                "IncompatibleSpaceException",
                threwException);
    }

    private static void test7VehicleWithActiveTicket() {
        ParkingSystem system = new ParkingSystem();
        system.registerParkingSpace.execute(new ParkingSpace("A-1",
                SpaceType.CAR));
        system.registerParkingSpace.execute(new ParkingSpace("A-2",
                SpaceType.CAR));
        Vehicle v = new Car("EEE555", "Hyundai", "Tucson", "White");
        system.registerVehicle.execute(v);
        system.checkInVehicle.execute(v, LocalDateTime.now());

        boolean threwException = false;
        try {
            system.checkInVehicle.execute(v, LocalDateTime.now());
        } catch (VehicleWithActiveTicketException e) {
            threwException = true;
        }
        verify("7. Attempt to check in a vehicle that already has an "
                + "active ticket",
                "VehicleWithActiveTicketException",
                threwException);
    }

    // ---------- Stay and rate cases ----------

    private static void test8OneMinuteStay() {
        Vehicle v = new Car("FFF666", "Suzuki", "Swift", "Red");
        long amount = v.getRate().calculateAmount(1);
        verify("8. One-minute stay",
                "Charged one full hour (900)",
                amount == 900);
    }

    private static void test9SixtyMinuteStay() {
        Vehicle v = new Car("GGG777", "Ford", "Fiesta", "Black");
        long amount = v.getRate().calculateAmount(60);
        verify("9. Sixty-minute stay",
                "Charged one full hour (900)",
                amount == 900);
    }

    private static void test10SixtyOneMinuteStay() {
        Vehicle v = new Car("HHH888", "Chevrolet", "Spark", "Blue");
        long amount = v.getRate().calculateAmount(61);
        verify("10. Sixty-one-minute stay",
                "Charged two full hours (1800); the fraction rounds up to "
                        + "a full hour",
                amount == 1800);
    }

    private static void test11StayWithDailyCap() {
        Vehicle v = new Car("III999", "Volkswagen", "Gol", "Gray");
        long minutes = 11 * 60;
        long amount = v.getRate().calculateAmount(minutes);
        verify("11. Stay of more than 10 hours applying the maximum rate",
                "The 7000 daily cap is applied instead of 9900",
                amount == 7000);
    }

    // ---------- Closing, payment and query cases ----------

    private static void test12CorrectTicketClosing() {
        ParkingSystem system = new ParkingSystem();
        system.registerParkingSpace.execute(new ParkingSpace("A-1",
                SpaceType.CAR));
        Vehicle v = new Car("JJJ000", "Renault", "Logan", "Red");
        system.registerVehicle.execute(v);
        LocalDateTime entry = LocalDateTime.of(2026, 9, 16, 8, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 9, 16, 9, 30);
        ParkingTicket ticket = system.checkInVehicle.execute(v, entry);
        system.checkOutVehicle.execute(v.getPlate(), exit);
        verify("12. Correct ticket closing",
                "Ticket CLOSED with amount 1800 (2 hours)",
                ticket.getStatus() == TicketStatus.CLOSED &&
                        ticket.getAmount() == 1800);
    }

    private static void test13CorrectPayment() {
        ParkingSystem system = new ParkingSystem();
        system.registerParkingSpace.execute(new ParkingSpace("A-1",
                SpaceType.CAR));
        Vehicle v = new Car("KKK111", "Fiat", "Cronos", "White");
        system.registerVehicle.execute(v);
        LocalDateTime entry = LocalDateTime.of(2026, 9, 16, 8, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 9, 16, 9, 0);
        ParkingTicket ticket = system.checkInVehicle.execute(v, entry);
        system.checkOutVehicle.execute(v.getPlate(), exit);
        Payment payment = system.registerPayment.execute(ticket,
                PaymentType.CASH, exit);
        verify("13. Correct payment",
                "Ticket PAID and payment registered with the ticket's amount",
                ticket.getStatus() == TicketStatus.PAID &&
                        payment.getAmount() == ticket.getAmount());
    }

    private static void test14SpaceRelease() {
        ParkingSystem system = new ParkingSystem();
        ParkingSpace space = new ParkingSpace("A-1", SpaceType.CAR);
        system.registerParkingSpace.execute(space);
        Vehicle v = new Car("LLL222", "Peugeot", "208", "Black");
        system.registerVehicle.execute(v);
        system.checkInVehicle.execute(v, LocalDateTime.now());
        system.checkOutVehicle.execute(v.getPlate(),
                LocalDateTime.now().plusHours(1));
        verify("14. Space release",
                "The space becomes AVAILABLE again",
                space.getStatus() == SpaceStatus.AVAILABLE);
    }

    private static void test15TotalRevenue() {
        ParkingSystem system = new ParkingSystem();
        system.registerParkingSpace.execute(new ParkingSpace("A-1",
                SpaceType.CAR));
        system.registerParkingSpace.execute(new ParkingSpace("M-1",
                SpaceType.MOTORCYCLE));

        Vehicle car = new Car("MMM333", "Subaru", "Impreza", "Red");
        Vehicle motorcycle = new Motorcycle("NNN444", "Yamaha", "FZ", "Black");
        system.registerVehicle.execute(car);
        system.registerVehicle.execute(motorcycle);

        LocalDateTime entry = LocalDateTime.of(2026, 9, 16, 8, 0);
        LocalDateTime exit = LocalDateTime.of(2026, 9, 16, 9, 0);

        ParkingTicket t1 = system.checkInVehicle.execute(car, entry);
        ParkingTicket t2 = system.checkInVehicle.execute(motorcycle, entry);
        system.checkOutVehicle.execute(car.getPlate(), exit);
        system.checkOutVehicle.execute(motorcycle.getPlate(), exit);
        system.registerPayment.execute(t1, PaymentType.CARD, exit);
        system.registerPayment.execute(t2, PaymentType.SINPE_MOVIL, exit);

        long revenue = system.getTotalRevenue.execute();
        verify("15. Total revenue calculation",
                "900 (car) + 500 (motorcycle) = 1400",
                revenue == 1400);
    }

    // ---------- Verification helper ----------

    /**
     * Records and prints the outcome of a single test case.
     *
     * @param name      short description of the test case
     * @param expected  human-readable description of the expected result
     * @param condition the actual boolean outcome of the test
     */
    private static void verify(String name, String expected, boolean condition){
        total++;
        String actual = condition ? "Meets expectation" :
                "DOES NOT meet expectation";
        if (condition) {
            passed++;
        }
        System.out.println((condition ? "[OK]   " : "[FAIL] ") + name);
        tableRows.add(String.format("| %s | %s | %s |", name, expected, actual));
    }
}

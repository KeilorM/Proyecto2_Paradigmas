package parking.app;

import parking.domain.exception.BusinessException;
import parking.domain.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Interactive, text-based menu that lets a real user operate the
 * parking lot from the console: type a plate, pick a vehicle type,
 * register an entry, pay a ticket, and so on - instead of the values
 * being hard-coded in a script.
 * <p>
 * This class is the only place in the whole project that reads from
 * {@link System#in} or prints menus/prompts; it contains no business
 * logic of its own. Every action is delegated to the corresponding use
 * case exposed by {@link ParkingSystem}, and every
 * {@link BusinessException} thrown by those use cases is caught here
 * and shown to the user as a friendly message instead of crashing the
 * program.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class ConsoleMenu {

    private final ParkingSystem system;
    private final Scanner scanner;

    /**
     * Creates the menu on top of an already-wired {@link ParkingSystem}.
     *
     * @param system  the wired use cases the menu will delegate to
     * @param scanner the input source to read the user's choices from
     */
    public ConsoleMenu(ParkingSystem system, Scanner scanner) {
        this.system = system;
        this.scanner = scanner;
    }

    /**
     * Runs the menu loop until the user chooses to exit.
     */
    public void run() {
        System.out.println("==============================================");
        System.out.println(" PARKING COTO - Private Parking Lot Management");
        System.out.println("==============================================");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("Choose an option: ").trim();
            System.out.println();
            try {
                switch (choice) {
                    case "1" -> registerVehicle();
                    case "2" -> registerSpace();
                    case "3" -> listAvailableSpaces();
                    case "4" -> checkIn();
                    case "5" -> listVehiclesInside();
                    case "6" -> checkOut();
                    case "7" -> registerPayment();
                    case "8" -> showTotalRevenue();
                    case "9" -> listActiveTickets();
                    case "10" -> showOccupancy();
                    case "11" -> listRegisteredVehicles();
                    case "12" -> listPayments();
                    case "0" -> {
                        running = false;
                        System.out.println("Goodbye!");
                    }
                    default -> System.out.println("Invalid option, try again.");
                }
            } catch (BusinessException e) {
                // Every business rule violation lands here, already as a
                // human-readable message thrown by the domain/application
                // layers; the menu just displays it and keeps running.
                System.out.println("Could not complete the operation: " +
                        e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("That doesn't look like a valid number. "
                        + "Try again.");
            }
            System.out.println();
        }
    }

    private void printMenu() {
        System.out.println("--------------------------------------------");
        System.out.println(" 1. Register vehicle");
        System.out.println(" 2. Register parking space");
        System.out.println(" 3. Query available spaces");
        System.out.println(" 4. Register vehicle entry (check-in)");
        System.out.println(" 5. Query vehicles currently inside");
        System.out.println(" 6. Register exit (check-out)");
        System.out.println(" 7. Register payment");
        System.out.println(" 8. Query total revenue");
        System.out.println(" 9. Query active tickets");
        System.out.println("10. Query occupancy by space type");
        System.out.println("11. Query registered vehicles");
        System.out.println("12. Query payments made");
        System.out.println(" 0. Exit");
        System.out.println("--------------------------------------------");
    }

    // ---------- 1. Register vehicle ----------

    private void registerVehicle() {
        System.out.println("-- Register vehicle --");
        String plate = readLine("Plate: ");
        String brand = readLine("Brand: ");
        String model = readLine("Model: ");
        String color = readLine("Color: ");
        SpaceType type = readVehicleType();

        Vehicle vehicle = switch (type) {
            case CAR -> new Car(plate, brand, model, color);
            case MOTORCYCLE -> new Motorcycle(plate, brand, model, color);
            case CARGO -> new CargoVehicle(plate, brand, model, color);
        };

        system.registerVehicle.execute(vehicle);
        System.out.println("Vehicle registered: " + vehicle);
    }

    // ---------- 2. Register space ----------

    private void registerSpace() {
        System.out.println("-- Register parking space --");
        String number = readLine("Space number/identifier (e.g. A-1): ");
        SpaceType type = readSpaceType();
        system.registerParkingSpace.execute(new ParkingSpace(number, type));
        System.out.println("Space " + number + " (" + type + ") registered.");
    }

    // ---------- 3. Available spaces ----------

    private void listAvailableSpaces() {
        System.out.println("-- Available spaces --");
        SpaceType type = readSpaceType();
        List<ParkingSpace> available = system.listAvailableSpaces.execute(type);
        if (available.isEmpty()) {
            System.out.println("No available spaces of type " + type + ".");
        } else {
            available.forEach(s -> System.out.println("  - " + s));
        }
    }

    // ---------- 4. Check-in ----------

    private void checkIn() {
        System.out.println("-- Register vehicle entry --");
        String plate = readLine("Vehicle plate: ");
        Optional<Vehicle> vehicle = system.getVehicleByPlate.execute(plate);
        if (vehicle.isEmpty()) {
            System.out.println("There is no vehicle registered with plate " +
                    plate + ". Register it first (option 1).");
            return;
        }

        ParkingTicket ticket = system.checkInVehicle.execute(vehicle.get(),
                LocalDateTime.now());
        System.out.println("Entry registered. Ticket #" + ticket.getNumber()
                + " - space " + ticket.getSpace().getNumber()
                + " - entry time: " + ticket.getEntryTime());
    }

    // ---------- 5. Vehicles inside ----------

    private void listVehiclesInside() {
        System.out.println("-- Vehicles currently inside the lot --");
        List<Vehicle> inside = system.listVehiclesInside.execute();
        if (inside.isEmpty()) {
            System.out.println("The lot is currently empty.");
        } else {
            inside.forEach(v -> System.out.println("  - " + v));
        }
    }

    // ---------- 6. Check-out ----------

    private void checkOut() {
        System.out.println("-- Register vehicle exit --");
        String plate = readLine("Vehicle plate: ");
        ParkingTicket ticket = system.checkOutVehicle.execute(plate,
                LocalDateTime.now());

        long minutes = ticket.getStayInMinutes();
        long billableHours = TimeUtil.billableHours(minutes);

        System.out.println("Exit registered for ticket #" +
                ticket.getNumber() + ".");
        System.out.println("Stay time: " + minutes + " minute(s) -> " +
                billableHours + " billable hour(s).");
        System.out.println("Amount due: " + ticket.getAmount()
                + " (pending payment - use option 7 with ticket #" +
                ticket.getNumber() + ").");
    }

    // ---------- 7. Register payment ----------

    private void registerPayment() {
        System.out.println("-- Register payment --");
        int ticketNumber = readInt("Ticket number: ");
        Optional<ParkingTicket> ticket = system.getTicketByNumber.execute(
                ticketNumber);
        if (ticket.isEmpty()) {
            System.out.println("There is no ticket #" + ticketNumber + ".");
            return;
        }

        PaymentType paymentType = readPaymentType();
        Payment payment = system.registerPayment.execute(ticket.get(),
                paymentType, LocalDateTime.now());
        System.out.println("Payment registered: " + payment);
    }

    // ---------- 8. Total revenue ----------

    private void showTotalRevenue() {
        System.out.println("-- Total revenue --");
        System.out.println("Total revenue generated so far: " +
                system.getTotalRevenue.execute());
    }

    // ---------- 9. Active tickets ----------

    private void listActiveTickets() {
        System.out.println("-- Active tickets --");
        List<ParkingTicket> active = system.listActiveTickets.execute();
        if (active.isEmpty()) {
            System.out.println("There are no active tickets.");
        } else {
            active.forEach(t -> System.out.println("  - " + t));
        }
    }

    // ---------- 10. Occupancy ----------

    private void showOccupancy() {
        System.out.println("-- Occupancy by space type --");
        Map<SpaceType, long[]> occupancy = system.getOccupancyByType.execute();
        occupancy.forEach((type, counts) ->
                System.out.println("  " + type + " -> available: " + counts[0]
                        + ", occupied: " + counts[1] + ", out of service: " +
                        counts[2]));
    }

    // ---------- 11. Registered vehicles ----------

    private void listRegisteredVehicles() {
        System.out.println("-- Registered vehicles --");
        List<Vehicle> registered = system.listRegisteredVehicles.execute();
        if (registered.isEmpty()) {
            System.out.println("No vehicles have been registered yet.");
        } else {
            registered.forEach(v -> System.out.println("  - " + v));
        }
    }

    // ---------- 12. Payments made ----------

    private void listPayments() {
        System.out.println("-- Payments made --");
        List<Payment> payments = system.listPayments.execute();
        if (payments.isEmpty()) {
            System.out.println("No payments have been registered yet.");
        } else {
            payments.forEach(p -> System.out.println("  - " + p));
        }
    }

    // ---------- Input helpers ----------

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private SpaceType readVehicleType() {
        while (true) {
            System.out.println("Vehicle type: 1) Car  2) Motorcycle  "
                    + "3) Cargo vehicle");
            String choice = readLine("Choose: ").trim();
            switch (choice) {
                case "1": return SpaceType.CAR;
                case "2": return SpaceType.MOTORCYCLE;
                case "3": return SpaceType.CARGO;
                default: System.out.println("Invalid option, try again.");
            }
        }
    }

    private SpaceType readSpaceType() {
        while (true) {
            System.out.println("Space type: 1) Car  2) Motorcycle  3) Cargo");
            String choice = readLine("Choose: ").trim();
            switch (choice) {
                case "1": return SpaceType.CAR;
                case "2": return SpaceType.MOTORCYCLE;
                case "3": return SpaceType.CARGO;
                default: System.out.println("Invalid option, try again.");
            }
        }
    }

    private PaymentType readPaymentType() {
        while (true) {
            System.out.println("Payment type: 1) Cash  2) Card  3) SINPE Movil");
            String choice = readLine("Choose: ").trim();
            switch (choice) {
                case "1": return PaymentType.CASH;
                case "2": return PaymentType.CARD;
                case "3": return PaymentType.SINPE_MOVIL;
                default: System.out.println("Invalid option, try again.");
            }
        }
    }
}

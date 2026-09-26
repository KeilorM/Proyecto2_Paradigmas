package parking.app;

import java.util.Scanner;

/**
 * Entry point of the console application. Wires a fresh
 * {@link ParkingSystem} and hands control to {@link ConsoleMenu}, which
 * reads the user's choices from {@link System#in} and drives every
 * available operation interactively - registering vehicles and spaces,
 * checking vehicles in and out, registering payments, and querying the
 * lot's state - with no data hard-coded anywhere.
 * <p>
 * For a non-interactive, scripted walkthrough of the same operations
 * (useful for quickly generating sample output for the report), see
 * {@link Demo}. The 15 mandatory test cases live in {@link ParkingTests}.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class Main {

    /**
     * Starts the interactive console application.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            ParkingSystem system = new ParkingSystem();
            new ConsoleMenu(system, scanner).run();
        }
    }
}

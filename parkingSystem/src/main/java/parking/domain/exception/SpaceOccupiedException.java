package parking.domain.exception;

/**
 * Thrown when attempting to assign a space that is already
 * {@code OCCUPIED} by another vehicle.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class SpaceOccupiedException extends SpaceNotAvailableException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param number the number of the already-occupied space
     */
    public SpaceOccupiedException(String number) {
        super("Space " + number + " is already occupied");
    }
}

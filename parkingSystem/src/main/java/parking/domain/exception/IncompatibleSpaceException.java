package parking.domain.exception;

/**
 * Thrown when attempting to assign a space whose {@code SpaceType} does
 * not match the vehicle's required type.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class IncompatibleSpaceException extends SpaceNotAvailableException {

    private static final long serialVersionUID = 1L;

    /**
     * @param number the number of the incompatible space
     * @param plate  the plate of the vehicle that was rejected
     */
    public IncompatibleSpaceException(String number, String plate) {
        super("Space " + number + " is not compatible with vehicle " + plate);
    }
}

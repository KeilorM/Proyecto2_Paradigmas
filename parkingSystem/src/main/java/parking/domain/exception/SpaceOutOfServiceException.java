package parking.domain.exception;

/**
 * Thrown when attempting to assign a space that is marked
 * {@code OUT_OF_SERVICE}.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class SpaceOutOfServiceException extends SpaceNotAvailableException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates the exception.
     *
     * @param number the number of the out-of-service space
     */
    public SpaceOutOfServiceException(String number) {
        super("Space " + number + " is out of service");
    }
}

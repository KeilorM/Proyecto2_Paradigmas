package parking.domain.exception;

/**
 * Root exception for the cases where one specific, already-identified
 * space cannot be assigned to a vehicle. See the more specific
 * subclasses {@link SpaceOccupiedException},
 * {@link SpaceOutOfServiceException} and {@link IncompatibleSpaceException}.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class SpaceNotAvailableException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * @param message human-readable description of why the space 
     * cannot be assigned
     */
    public SpaceNotAvailableException(String message) {
        super(message);
    }
}

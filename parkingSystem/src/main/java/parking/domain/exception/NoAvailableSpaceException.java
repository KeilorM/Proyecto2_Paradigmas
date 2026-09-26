package parking.domain.exception;

import parking.domain.model.SpaceType;

/**
 * Thrown when there is no available, compatible space to assign to an
 * entering vehicle.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class NoAvailableSpaceException extends BusinessException {

    private static final long serialVersionUID = 1L;

    /**
     * @param type the space type that has no available spaces
     */
    public NoAvailableSpaceException(SpaceType type) {
        super("No available spaces for type " + type);
    }
}

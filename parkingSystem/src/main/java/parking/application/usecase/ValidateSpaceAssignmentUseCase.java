package parking.application.usecase;

import parking.domain.exception.IncompatibleSpaceException;
import parking.domain.exception.SpaceOccupiedException;
import parking.domain.exception.SpaceOutOfServiceException;
import parking.domain.model.ParkingSpace;
import parking.domain.model.SpaceStatus;
import parking.domain.model.Vehicle;

/**
 * Validates, in isolation, whether one specific space could be assigned
 * to one specific vehicle.
 * <p>
 * This use case is not part of the normal check-in flow (see
 * {@link CheckInVehicleUseCase}, which finds a space automatically); it
 * exists so each individual assignment rule (occupied space, out of
 * service space, incompatible space) can be exercised and tested on its
 * own.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class ValidateSpaceAssignmentUseCase {

    /**
     * Creates the use case. It has no dependencies, since it only
     * evaluates the state of the objects it is given.
     */
    public ValidateSpaceAssignmentUseCase() {
    }

    /**
     * Validates the assignment, throwing the exception that matches the
     * first violated rule.
     *
     * @param space   the candidate space
     * @param vehicle the candidate vehicle
     * @throws SpaceOutOfServiceException if the space is out of service
     * @throws SpaceOccupiedException     if the space is already occupied
     * @throws IncompatibleSpaceException if the space's type does not match 
     * the vehicle's required type
     */
    public void execute(ParkingSpace space, Vehicle vehicle) {
        if (space.getStatus() == SpaceStatus.OUT_OF_SERVICE) {
            throw new SpaceOutOfServiceException(space.getNumber());
        }
        if (space.getStatus() == SpaceStatus.OCCUPIED) {
            throw new SpaceOccupiedException(space.getNumber());
        }
        if (!space.isCompatibleWith(vehicle)) {
            throw new IncompatibleSpaceException(space.getNumber(),
                    vehicle.getPlate());
        }
    }
}

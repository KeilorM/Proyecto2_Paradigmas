package parking.domain.model;

/**
 * Lifecycle states of a {@link ParkingSpace}.
 * <p>
 * A space transitions {@code AVAILABLE -> OCCUPIED -> AVAILABLE} on every
 * normal entry/exit cycle. {@code OUT_OF_SERVICE} is an administrative
 * state that blocks any assignment until it is manually lifted.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public enum SpaceStatus {

    /** The space is free and can be assigned to a compatible vehicle. */
    AVAILABLE,

    /** The space currently holds a vehicle and cannot be reassigned. */
    OCCUPIED,

    /** The space is disabled for maintenance or any other reason and cannot 
     * be assigned. */
    OUT_OF_SERVICE
}

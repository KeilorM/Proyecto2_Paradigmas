package parking.domain.model;

/**
 * A physical space in the parking lot.
 * <p>
 * A space is responsible for its own {@link SpaceStatus} and for
 * deciding whether it is compatible with a given {@link Vehicle}
 * ({@link #isCompatibleWith(Vehicle)}). No class outside this one is
 * allowed to mutate its status directly: every transition goes through
 * {@link #occupy()}, {@link #release()} or
 * {@link #markOutOfService()}, each of which enforces its own
 * preconditions.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class ParkingSpace {

    private final String number;
    private final SpaceType type;
    private SpaceStatus status;

    /**
     * Creates a new space, initially {@link SpaceStatus#AVAILABLE}.
     *
     * @param number unique identifier shown to staff and customers
     * (e.g. {@code "A-1"})
     * @param type   the kind of vehicle this space is built for
     */
    public ParkingSpace(String number, SpaceType type) {
        this.number = number;
        this.type = type;
        this.status = SpaceStatus.AVAILABLE;
    }

    /**
     * Determines whether this space can host the given vehicle, by
     * comparing this space's {@link #getType()} against the vehicle's
     * {@link Vehicle#getCompatibleSpaceType()}.
     *
     * @param vehicle the vehicle to check
     * @return {@code true} if the space's type matches the vehicle's 
     * required type
     */
    public boolean isCompatibleWith(Vehicle vehicle) {
        return this.type == vehicle.getCompatibleSpaceType();
    }

    /**
     * @return {@code true} if this space's status is 
     * {@link SpaceStatus#AVAILABLE}
     */
    public boolean isAvailable() {
        return status == SpaceStatus.AVAILABLE;
    }

    /**
     * Marks this space as occupied.
     *
     * @throws IllegalStateException if the space is out of service 
     * or already occupied
     */
    public void occupy() {
        if (status == SpaceStatus.OUT_OF_SERVICE) {
            throw new IllegalStateException("Space " + number +
                    " is out of service");
        }
        if (status == SpaceStatus.OCCUPIED) {
            throw new IllegalStateException("Space " + number +
                    " is already occupied");
        }
        this.status = SpaceStatus.OCCUPIED;
    }

    /**
     * Marks this space as available again, typically after a vehicle exits.
     *
     * @throws IllegalStateException if the space is out of service
     */
    public void release() {
        if (status == SpaceStatus.OUT_OF_SERVICE) {
            throw new IllegalStateException("Space " + number +
                    " is out of service");
        }
        this.status = SpaceStatus.AVAILABLE;
    }

    /**
     * Marks this space as out of service, making it ineligible for any
     * assignment until it is manually set back to available.
     */
    public void markOutOfService() {
        this.status = SpaceStatus.OUT_OF_SERVICE;
    }

    /**
     * @return this space's unique identifier
     */
    public String getNumber() {
        return number;
    }

    /**
     * @return the kind of vehicle this space is built for
     */
    public SpaceType getType() {
        return type;
    }

    /**
     * @return this space's current status
     */
    public SpaceStatus getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Space " + number + " [" + type + "] - " + status;
    }
}

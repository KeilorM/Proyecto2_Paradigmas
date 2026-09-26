package parking.domain.model;

import java.util.Objects;

/**
 * Common abstraction for every vehicle that can enter the parking lot.
 * <p>
 * {@code Vehicle} encapsulates the attributes shared by all vehicle
 * types and delegates two decisions to its concrete subclasses through
 * abstract methods: which {@link Rate} applies to it
 * ({@link #getRate()}) and which {@link SpaceType} it needs
 * ({@link #getCompatibleSpaceType()}). No other class in the system uses
 * {@code instanceof} or a type enum to branch on the vehicle's concrete
 * class; every decision that depends on the vehicle's type is resolved
 * polymorphically through these two methods.
 * <p>
 * Two vehicles are considered equal when they share the same
 * {@link #getPlate() plate}, since a plate uniquely identifies a
 * vehicle regardless of any other attribute.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public abstract class Vehicle {

    private final String plate;
    private final String brand;
    private final String model;
    private final String color;

    /**
     * Creates a vehicle with its identifying and descriptive attributes.
     *
     * @param plate license plate; required, used as the vehicle's identity
     * @param brand manufacturer brand
     * @param model vehicle model
     * @param color vehicle color
     * @throws IllegalArgumentException if {@code plate} is 
     * {@code null} or blank
     */
    protected Vehicle(String plate, String brand, String model, String color) {
        if (plate == null || plate.isBlank()) {
            throw new IllegalArgumentException("The plate is required");
        }
        this.plate = plate.trim().toUpperCase();
        this.brand = brand;
        this.model = model;
        this.color = color;
    }

    /**
     * Returns the fee-calculation strategy for this vehicle.
     * <p>
     * Each subclass composes and returns its own {@link Rate}
     * instance (see the Strategy/Decorator collaboration in
     * {@link RateWithDailyCap}); callers must never attempt to infer or
     * duplicate this logic themselves.
     *
     * @return the {@link Rate} that must be used to charge this vehicle
     */
    public abstract Rate getRate();

    /**
     * Returns the type of {@link ParkingSpace} this vehicle needs.
     *
     * @return the compatible {@link SpaceType} for this vehicle
     */
    public abstract SpaceType getCompatibleSpaceType();

    /**
     * Returns a human-readable name for this vehicle's type, used only
     * for display purposes (reports, logs, {@link #toString()}).
     *
     * @return a short description such as {@code "Car"} or {@code "Motorcycle"}
     */
    public abstract String getTypeDescription();

    /**
     * @return the vehicle's license plate, normalized to upper case
     */
    public String getPlate() {
        return plate;
    }

    /**
     * @return the vehicle's brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @return the vehicle's model
     */
    public String getModel() {
        return model;
    }

    /**
     * @return the vehicle's color
     */
    public String getColor() {
        return color;
    }

    /**
     * Two vehicles are equal when they have the same plate.
     *
     * @param o the object to compare against
     * @return {@code true} if {@code o} is a {@code Vehicle} 
     * with the same plate
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) o;
        return plate.equals(vehicle.plate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(plate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getTypeDescription() + " [" + plate + "] " + brand + " " +
                model + " (" + color + ")";
    }
}

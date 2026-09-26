package parking.domain.model;

/**
 * A cargo or freight vehicle.
 * <p>
 * Charged {@value #RATE_PER_HOUR} per billable hour, capped at
 * {@value #DAILY_CAP_AMOUNT} per 24-hour period once the stay reaches
 * {@value #MIN_HOURS_FOR_CAP} billable hours, and requires a
 * {@link SpaceType#CARGO} space.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class CargoVehicle extends Vehicle {

    private static final long RATE_PER_HOUR = 1500;
    private static final long MIN_HOURS_FOR_CAP = 10;
    private static final long DAILY_CAP_AMOUNT = 11000;

    /**
     * Creates a cargo vehicle.
     *
     * @param plate license plate
     * @param brand manufacturer brand
     * @param model vehicle model
     * @param color vehicle color
     */
    public CargoVehicle(String plate, String brand, String model, String color){
        super(plate, brand, model, color);
    }

    /**
     * {@inheritDoc}
     *
     * @return a new {@link RateWithDailyCap} wrapping an {@link HourlyRate}
     *         configured with this class's constants
     */
    @Override
    public Rate getRate() {
        return new RateWithDailyCap(new HourlyRate(RATE_PER_HOUR),
                MIN_HOURS_FOR_CAP, DAILY_CAP_AMOUNT);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@link SpaceType#CARGO}
     */
    @Override
    public SpaceType getCompatibleSpaceType() {
        return SpaceType.CARGO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTypeDescription() {
        return "Cargo vehicle";
    }
}

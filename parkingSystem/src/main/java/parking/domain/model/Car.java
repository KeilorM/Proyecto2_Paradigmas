package parking.domain.model;

/**
 * A regular passenger car.
 * <p>
 * Charged {@value #RATE_PER_HOUR} per billable hour, capped at
 * {@value #DAILY_CAP_AMOUNT} per 24-hour period once the stay reaches
 * {@value #MIN_HOURS_FOR_CAP} billable hours, and requires a
 * {@link SpaceType#CAR} space.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class Car extends Vehicle {

    private static final long RATE_PER_HOUR = 900;
    private static final long MIN_HOURS_FOR_CAP = 10;
    private static final long DAILY_CAP_AMOUNT = 7000;

    /**
     * Creates a car.
     *
     * @param plate license plate
     * @param brand manufacturer brand
     * @param model vehicle model
     * @param color vehicle color
     */
    public Car(String plate, String brand, String model, String color) {
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
     * @return {@link SpaceType#CAR}
     */
    @Override
    public SpaceType getCompatibleSpaceType() {
        return SpaceType.CAR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTypeDescription() {
        return "Car";
    }
}

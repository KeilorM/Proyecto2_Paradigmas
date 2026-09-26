package parking.domain.model;

/**
 * Base {@link Rate} implementation: charges a fixed amount for every
 * billable hour (see {@link TimeUtil#billableHours(long)}), with no cap.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class HourlyRate implements Rate {

    private final long valuePerHour;

    /**
     * Creates an hourly rate.
     *
     * @param valuePerHour amount charged per billable hour; must 
     * not be negative
     * @throws IllegalArgumentException if {@code valuePerHour} is negative
     */
    public HourlyRate(long valuePerHour) {
        if (valuePerHour < 0) {
            throw new IllegalArgumentException(
                    "The value per hour cannot be negative");
        }
        this.valuePerHour = valuePerHour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long calculateAmount(long minutesStayed) {
        return TimeUtil.billableHours(minutesStayed) * valuePerHour;
    }

    /**
     * @return the configured amount charged per billable hour
     */
    public long getValuePerHour() {
        return valuePerHour;
    }
}

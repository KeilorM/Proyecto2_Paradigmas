package parking.domain.model;

/**
 * {@link Rate} decorator (Decorator pattern) that applies a maximum
 * amount per 24-hour period once the stay reaches a configured minimum
 * number of hours.
 * <p>
 * This class is the single place where the "daily cap" business rule
 * lives. It wraps any other {@code Rate} (typically an
 * {@link HourlyRate}) without that base rate needing to know about caps
 * at all. As a consequence, the rule can evolve - a different threshold,
 * a different cap amount, or even a new decorator stacked on top of this
 * one - without touching {@link Vehicle}, {@link ParkingTicket}, or the
 * application's use cases.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class RateWithDailyCap implements Rate {

    private static final long HOURS_PER_PERIOD = 24;

    private final Rate baseRate;
    private final long minHoursToApplyCap;
    private final long capAmountPerPeriod;

    /**
     * Creates a rate that caps a base rate's result per 24-hour period.
     *
     * @param baseRate           the rate whose result this decorator caps
     * @param minHoursToApplyCap minimum number of billable hours at which
     *                           the cap starts to apply; stays shorter
     *                           than this are charged at {@code baseRate}
     *                           with no cap
     * @param capAmountPerPeriod maximum amount chargeable per each
     *                           complete or partial 24-hour period
     */
    public RateWithDailyCap(Rate baseRate, long minHoursToApplyCap,
            long capAmountPerPeriod) {
        this.baseRate = baseRate;
        this.minHoursToApplyCap = minHoursToApplyCap;
        this.capAmountPerPeriod = capAmountPerPeriod;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Delegates to the wrapped {@link #baseRate} for the raw amount and,
     * only when the stay is long enough, compares it against the daily
     * cap for the corresponding number of 24-hour periods, returning
     * whichever amount is lower.
     */
    @Override
    public long calculateAmount(long minutesStayed) {
        long amountWithoutCap = baseRate.calculateAmount(minutesStayed);
        long hours = TimeUtil.billableHours(minutesStayed);

        if (hours < minHoursToApplyCap) {
            return amountWithoutCap;
        }

        long dailyPeriods = (hours + HOURS_PER_PERIOD - 1) / HOURS_PER_PERIOD;
        long amountWithCap = dailyPeriods * capAmountPerPeriod;

        return Math.min(amountWithoutCap, amountWithCap);
    }
}

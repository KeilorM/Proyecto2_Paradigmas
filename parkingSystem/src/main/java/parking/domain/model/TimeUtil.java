package parking.domain.model;

/**
 * Stateless time-calculation helpers shared by the rate classes.
 * <p>
 * This class exists to centralize a single business rule -
 * "any fraction of an hour is billed as a full hour" - in one place, so
 * every rate implementation applies it identically instead of each one
 * re-implementing (and possibly getting slightly wrong) its own rounding.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public final class TimeUtil {

    private TimeUtil() {
        // Utility class: not meant to be instantiated.
    }

    /**
     * Rounds a stay expressed in minutes up to the number of full hours
     * that must be billed.
     *
     * @param minutesStayed minutes the vehicle stayed in the lot; zero or
     *                       negative values are treated as no time elapsed
     * @return the number of billable hours (a partial hour always counts
     *         as one full hour)
     */
    public static long billableHours(long minutesStayed) {
        if (minutesStayed <= 0) {
            return 0;
        }
        return (minutesStayed + 59) / 60;
    }
}

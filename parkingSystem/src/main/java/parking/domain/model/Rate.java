package parking.domain.model;

/**
 * Fee-calculation strategy (Strategy pattern).
 * <p>
 * Every {@link Vehicle} owns its own {@code Rate} instance through
 * composition, obtained from {@link Vehicle#getRate()}. No other class in
 * the system inspects the vehicle's concrete type to decide how much to
 * charge; everything is resolved by invoking {@link #calculateAmount(long)}
 * polymorphically on whichever {@code Rate} the vehicle hands back.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public interface Rate {

    /**
     * Calculates the amount that must be charged for a given stay.
     *
     * @param minutesStayed total minutes the vehicle remained parked
     * @return the amount to charge, in the currency's smallest common
     *         accounting unit used throughout the system (colones, no
     *         decimals)
     */
    long calculateAmount(long minutesStayed);
}

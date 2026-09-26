package parking.domain.model;

/**
 * Enumerates the kinds of parking space that the lot can offer.
 * <p>
 * Every {@link Vehicle} subclass declares, through
 * {@link Vehicle#getCompatibleSpaceType()}, exactly one value of this
 * enum as the only space type it can occupy. This is the mechanism the
 * whole system relies on to decide compatibility polymorphically,
 * instead of asking "what concrete type is this vehicle" anywhere else.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public enum SpaceType {

    /** Space sized and reserved for motorcycles. */
    MOTORCYCLE,

    /** Space sized and reserved for regular cars. */
    CAR,

    /** Space sized and reserved for cargo/freight vehicles. */
    CARGO
}

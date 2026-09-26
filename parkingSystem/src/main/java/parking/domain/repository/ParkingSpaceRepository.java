package parking.domain.repository;

import parking.domain.model.ParkingSpace;
import parking.domain.model.SpaceType;

import java.util.List;

/**
 * Persistence port for {@link ParkingSpace} instances.
 * <p>
 * As with the other repository ports, only the contract lives in the
 * domain layer; the in-memory (or any future) implementation lives in
 * {@code parking.infrastructure.repository}.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public interface ParkingSpaceRepository {

    /**
     * Persists a parking space.
     *
     * @param space the space to store
     */
    void save(ParkingSpace space);

    /**
     * @return every registered space, regardless of status
     */
    List<ParkingSpace> findAll();

    /**
     * Finds every currently available space of the given type.
     *
     * @param type the space type to filter by
     * @return available spaces of that type, in no particular order
     */
    List<ParkingSpace> findAvailableByType(SpaceType type);
}

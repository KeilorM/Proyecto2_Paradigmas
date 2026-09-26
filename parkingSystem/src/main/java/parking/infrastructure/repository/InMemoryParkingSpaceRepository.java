package parking.infrastructure.repository;

import parking.domain.model.ParkingSpace;
import parking.domain.model.SpaceType;
import parking.domain.repository.ParkingSpaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In-memory {@link ParkingSpaceRepository} implementation, backed by an
 * {@link ArrayList}.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class InMemoryParkingSpaceRepository implements ParkingSpaceRepository {

    private final List<ParkingSpace> spaces = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(ParkingSpace space) {
        spaces.add(space);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParkingSpace> findAll() {
        return List.copyOf(spaces);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ParkingSpace> findAvailableByType(SpaceType type) {
        return spaces.stream()
                .filter(ParkingSpace::isAvailable)
                .filter(s -> s.getType() == type)
                .collect(Collectors.toList());
    }
}

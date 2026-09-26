package parking.application.usecase;

import parking.domain.model.ParkingSpace;
import parking.domain.model.SpaceStatus;
import parking.domain.model.SpaceType;
import parking.domain.repository.ParkingSpaceRepository;

import java.util.EnumMap;
import java.util.Map;

/**
 * Computes how many spaces of each type are available, occupied, and
 * out of service.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class GetOccupancyByTypeUseCase {

    private final ParkingSpaceRepository spaceRepository;

    /**
     * @param spaceRepository port used to query every registered space
     */
    public GetOccupancyByTypeUseCase(ParkingSpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    /**
     * @return a map from each {@link SpaceType} to a 3-element array
     *         {@code [available, occupied, outOfService]} with the count
     *         of spaces of that type in each status
     */
    public Map<SpaceType, long[]> execute() {
        Map<SpaceType, long[]> result = new EnumMap<>(SpaceType.class);
        var allSpaces = spaceRepository.findAll();

        for (SpaceType type : SpaceType.values()) {
            long available = allSpaces.stream()
                    .filter(s -> s.getType() == type && s.getStatus() 
                            == SpaceStatus.AVAILABLE)
                    .count();
            long occupied = allSpaces.stream()
                    .filter(s -> s.getType() == type && s.getStatus() 
                            == SpaceStatus.OCCUPIED)
                    .count();
            long outOfService = allSpaces.stream()
                    .filter(s -> s.getType() == type && s.getStatus() 
                            == SpaceStatus.OUT_OF_SERVICE)
                    .count();
            result.put(type, new long[]{available, occupied, outOfService});
        }
        return result;
    }
}

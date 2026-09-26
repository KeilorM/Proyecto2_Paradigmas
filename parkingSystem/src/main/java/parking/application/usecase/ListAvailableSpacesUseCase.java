package parking.application.usecase;

import parking.domain.model.ParkingSpace;
import parking.domain.model.SpaceType;
import parking.domain.repository.ParkingSpaceRepository;

import java.util.List;

/**
 * Lists every currently available space of a given type.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class ListAvailableSpacesUseCase {

    private final ParkingSpaceRepository spaceRepository;

    /**
     * @param spaceRepository port used to query spaces
     */
    public ListAvailableSpacesUseCase(ParkingSpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    /**
     * @param type the space type to filter by
     * @return the available spaces of that type
     */
    public List<ParkingSpace> execute(SpaceType type) {
        return spaceRepository.findAvailableByType(type);
    }
}

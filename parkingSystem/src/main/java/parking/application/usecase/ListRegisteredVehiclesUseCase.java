package parking.application.usecase;

import parking.domain.model.Vehicle;
import parking.domain.repository.VehicleRepository;

import java.util.List;

/**
 * Lists every vehicle ever registered in the system, regardless of
 * whether it is currently parked or not.
 * <p>
 * Contrast with {@link ListVehiclesInsideUseCase}, which only returns
 * vehicles that are currently inside the lot (active ticket).
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class ListRegisteredVehiclesUseCase {

    private final VehicleRepository vehicleRepository;

    /**
     * @param vehicleRepository port used to query registered vehicles
     */
    public ListRegisteredVehiclesUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * @return every vehicle registered in the system
     */
    public List<Vehicle> execute() {
        return vehicleRepository.findAll();
    }
}

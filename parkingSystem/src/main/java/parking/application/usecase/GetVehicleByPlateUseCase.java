package parking.application.usecase;

import parking.domain.model.Vehicle;
import parking.domain.repository.VehicleRepository;

import java.util.Optional;

/**
 * Looks up a registered vehicle by its plate.
 * <p>
 * Used by delivery mechanisms (such as an interactive menu) that only
 * have the plate typed by the user and need the actual {@link Vehicle}
 * instance to pass into other use cases like
 * {@link CheckInVehicleUseCase}.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class GetVehicleByPlateUseCase {

    private final VehicleRepository vehicleRepository;

    /**
     * @param vehicleRepository port used to query vehicles
     */
    public GetVehicleByPlateUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * @param plate the plate to search for
     * @return the matching vehicle, or {@link Optional#empty()} 
     * if none is registered
     */
    public Optional<Vehicle> execute(String plate) {
        return vehicleRepository.findByPlate(plate);
    }
}

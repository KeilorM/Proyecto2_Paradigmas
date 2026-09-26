package parking.application.usecase;

import parking.domain.exception.BusinessException;
import parking.domain.model.Vehicle;
import parking.domain.repository.VehicleRepository;

/**
 * Registers a new vehicle in the system so it becomes eligible to check in.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class RegisterVehicleUseCase {

    private final VehicleRepository vehicleRepository;

    /**
     * Creates the use case.
     *
     * @param vehicleRepository port used to persist vehicles
     */
    public RegisterVehicleUseCase(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    /**
     * Registers the given vehicle.
     *
     * @param vehicle the vehicle to register
     * @throws BusinessException if a vehicle with the same plate is 
     * already registered
     */
    public void execute(Vehicle vehicle) {
        if (vehicleRepository.existsByPlate(vehicle.getPlate())) {
            throw new BusinessException("Vehicle with plate " 
                    + vehicle.getPlate() + " is already registered");
        }
        vehicleRepository.save(vehicle);
    }
}

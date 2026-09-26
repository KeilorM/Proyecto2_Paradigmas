package parking.domain.repository;

import parking.domain.model.Vehicle;

import java.util.List;
import java.util.Optional;

/**
 * Persistence port for {@link Vehicle} instances.
 * <p>
 * This interface belongs to the domain layer (it is a "port", in
 * hexagonal-architecture terms), but it has no implementation here.
 * The concrete storage mechanism lives in the {@code infrastructure}
 * layer and implements this contract, so the domain and application
 * layers never depend on how or where vehicles are actually stored.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public interface VehicleRepository {

    /**
     * Persists a vehicle.
     *
     * @param vehicle the vehicle to store
     */
    void save(Vehicle vehicle);

    /**
     * Looks up a vehicle by its plate.
     *
     * @param plate the plate to search for
     * @return the matching vehicle, or {@link Optional#empty()} 
     * if none is registered
     */
    Optional<Vehicle> findByPlate(String plate);

    /**
     * Checks whether a vehicle with the given plate is already registered.
     *
     * @param plate the plate to check
     * @return {@code true} if a vehicle with that plate is already stored
     */
    boolean existsByPlate(String plate);

    /**
     * @return every registered vehicle
     */
    List<Vehicle> findAll();
}

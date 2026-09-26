package parking.infrastructure.repository;

import parking.domain.model.Vehicle;
import parking.domain.repository.VehicleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory {@link VehicleRepository} implementation, backed by an
 * {@link ArrayList}.
 * <p>
 * This is an "infrastructure" (outer-layer) adapter: it implements a
 * port defined by the domain layer. Swapping it for, say, a JDBC- or
 * file-backed implementation would require no change whatsoever to the
 * domain or application layers, since they only ever depend on the
 * {@link VehicleRepository} interface.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class InMemoryVehicleRepository implements VehicleRepository {

    private final List<Vehicle> vehicles = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Vehicle> findByPlate(String plate) {
        return vehicles.stream()
                .filter(v -> v.getPlate().equalsIgnoreCase(plate))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByPlate(String plate) {
        return findByPlate(plate).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vehicle> findAll() {
        return List.copyOf(vehicles);
    }
}

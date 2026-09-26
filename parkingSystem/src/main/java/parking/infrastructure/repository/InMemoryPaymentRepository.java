package parking.infrastructure.repository;

import parking.domain.model.Payment;
import parking.domain.repository.PaymentRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory {@link PaymentRepository} implementation, backed by an
 * {@link ArrayList} and an internal, monotonically increasing counter
 * for payment identifiers.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class InMemoryPaymentRepository implements PaymentRepository {

    private final List<Payment> payments = new ArrayList<>();
    private int nextId = 1;

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Payment payment) {
        payments.add(payment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Payment> findAll() {
        return List.copyOf(payments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int nextPaymentId() {
        return nextId++;
    }
}

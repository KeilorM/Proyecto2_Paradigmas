package parking.domain.repository;

import parking.domain.model.Payment;

import java.util.List;

/**
 * Persistence port for {@link Payment} instances, including the
 * generation of unique payment identifiers.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public interface PaymentRepository {

    /**
     * Persists a payment.
     *
     * @param payment the payment to store
     */
    void save(Payment payment);

    /**
     * @return every payment ever registered
     */
    List<Payment> findAll();

    /**
     * Reserves and returns the next unique payment identifier.
     *
     * @return a payment id not previously returned by this repository instance
     */
    int nextPaymentId();
}

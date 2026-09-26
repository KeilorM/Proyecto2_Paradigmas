package parking.application.usecase;

import parking.domain.model.Payment;
import parking.domain.repository.PaymentRepository;

/**
 * Computes the total revenue collected across every registered payment.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class GetTotalRevenueUseCase {

    private final PaymentRepository paymentRepository;

    /**
     * @param paymentRepository port used to query every registered payment
     */
    public GetTotalRevenueUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * @return the sum of the amounts of every registered payment
     */
    public long execute() {
        return paymentRepository.findAll().stream()
                .mapToLong(Payment::getAmount)
                .sum();
    }
}

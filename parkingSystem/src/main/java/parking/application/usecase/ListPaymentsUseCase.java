package parking.application.usecase;

import parking.domain.model.Payment;
import parking.domain.repository.PaymentRepository;

import java.util.List;

/**
 * Lists every payment ever registered in the system.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class ListPaymentsUseCase {

    private final PaymentRepository paymentRepository;

    /**
     * @param paymentRepository port used to query registered payments
     */
    public ListPaymentsUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * @return every payment registered so far
     */
    public List<Payment> execute() {
        return paymentRepository.findAll();
    }
}

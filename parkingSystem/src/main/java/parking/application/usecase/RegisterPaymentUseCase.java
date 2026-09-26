package parking.application.usecase;

import parking.domain.exception.BusinessException;
import parking.domain.exception.TicketStillActiveException;
import parking.domain.model.Payment;
import parking.domain.model.ParkingTicket;
import parking.domain.model.PaymentType;
import parking.domain.model.TicketStatus;
import parking.domain.repository.PaymentRepository;

import java.time.LocalDateTime;

/**
 * Registers the payment for a closed ticket and marks the ticket as paid.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class RegisterPaymentUseCase {

    private final PaymentRepository paymentRepository;

    /**
     * Creates the use case.
     *
     * @param paymentRepository port used to persist the payment
     */
    public RegisterPaymentUseCase(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Executes the payment.
     *
     * @param ticket      the ticket being paid; must be 
     * {@link TicketStatus#CLOSED}
     * @param paymentType the method used to pay
     * @param dateTime    the moment the payment was made
     * @return the newly created payment record
     * @throws TicketStillActiveException if the ticket has not been closed yet
     * @throws BusinessException          if the ticket has already been paid
     */
    public Payment execute(ParkingTicket ticket, PaymentType paymentType,
            LocalDateTime dateTime) {
        if (ticket.getStatus() == TicketStatus.ACTIVE) {
            throw new TicketStillActiveException(ticket.getNumber());
        }
        if (ticket.getStatus() == TicketStatus.PAID) {
            throw new BusinessException("Ticket #" + ticket.getNumber() +
                    " has already been paid");
        }

        Payment payment = new Payment(paymentRepository.nextPaymentId(),
                ticket, dateTime, ticket.getAmount(), paymentType);
        paymentRepository.save(payment);
        ticket.markAsPaid();
        return payment;
    }
}

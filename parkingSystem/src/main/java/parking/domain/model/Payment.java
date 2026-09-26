package parking.domain.model;

import java.time.LocalDateTime;

/**
 * An immutable record of a payment made against an already-closed
 * {@link ParkingTicket}.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public class Payment {

    private final int id;
    private final ParkingTicket ticket;
    private final LocalDateTime dateTime;
    private final long amount;
    private final PaymentType paymentType;

    /**
     * Creates a payment record.
     *
     * @param id          unique payment identifier
     * @param ticket      the ticket being paid; must be 
     * {@link TicketStatus#CLOSED}
     * @param dateTime    the moment the payment was made
     * @param amount      the amount collected
     * @param paymentType the method used to pay
     */
    public Payment(int id, ParkingTicket ticket, LocalDateTime dateTime,
            long amount, PaymentType paymentType) {
        this.id = id;
        this.ticket = ticket;
        this.dateTime = dateTime;
        this.amount = amount;
        this.paymentType = paymentType;
    }

    /**
     * @return this payment's unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * @return the ticket this payment settles
     */
    public ParkingTicket getTicket() {
        return ticket;
    }

    /**
     * @return the moment the payment was made
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * @return the amount collected
     */
    public long getAmount() {
        return amount;
    }

    /**
     * @return the method used to pay
     */
    public PaymentType getPaymentType() {
        return paymentType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Payment #" + id + " - ticket #" + ticket.getNumber() +
                " - " + paymentType + " - " + amount;
    }
}

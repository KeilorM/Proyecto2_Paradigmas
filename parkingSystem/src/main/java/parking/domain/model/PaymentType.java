package parking.domain.model;

/**
 * Payment methods accepted when settling a {@link ParkingTicket}.
 * 
 * @author Keilor MC
 * @author Randall AC
 */
public enum PaymentType {

    /** Payment made in physical cash. */
    CASH,

    /** Payment made with a debit or credit card. */
    CARD,

    /** Payment made through Costa Rica's SINPE Movil mobile transfer system. */
    SINPE_MOVIL
}

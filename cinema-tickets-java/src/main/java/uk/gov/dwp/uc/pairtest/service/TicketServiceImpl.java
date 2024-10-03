package uk.gov.dwp.uc.pairtest.service;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validator.TicketValidator;

public class TicketServiceImpl implements TicketService {

    public static final String INVALID_ACCOUNT_ID = "Invalid account ID supplied";
    public static final String MAX_TICKET_REQUEST = "More than maximum tickets requested";
    public static final String NO_ADULT_TICKET = "No adult ticket requested";
    public static final String NOT_ENOUGH_ADULT_TICKETS = "Insufficient adult tickets for number of infant tickets requested ";

    private final TicketValidator ticketValidator;
    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;

    public TicketServiceImpl(final TicketValidator ticketValidator,
                             final TicketPaymentService ticketPaymentService,
                             final SeatReservationService seatReservationService) {
        this.ticketValidator = ticketValidator;
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
    }

    /**
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if (!ticketValidator.isValidAccountId(accountId)) {
            throw new InvalidPurchaseException(INVALID_ACCOUNT_ID);
        }

        if (!ticketValidator.isValidTotalTicketsRequested(ticketTypeRequests)) {
            throw new InvalidPurchaseException(MAX_TICKET_REQUEST);
        }

        if (!ticketValidator.adultPresentInTicketsRequested(ticketTypeRequests)) {
            throw new InvalidPurchaseException(NO_ADULT_TICKET);
        }

        if (!ticketValidator.sufficientAdultTicketsRequested(ticketTypeRequests)) {
            throw new InvalidPurchaseException(NOT_ENOUGH_ADULT_TICKETS);
        }

        final int totalPayment = 0;
        final int totalSeats = 0;

        ticketPaymentService.makePayment(accountId, totalPayment);
        seatReservationService.reserveSeat(accountId, totalSeats);
    }


}

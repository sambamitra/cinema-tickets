package uk.gov.dwp.uc.pairtest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validator.TicketValidator;

import static uk.gov.dwp.uc.pairtest.exception.ErrorMessages.*;

public class TicketServiceImpl implements TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketValidator ticketValidator;
    private final TicketCalculationService ticketCalculationService;
    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;

    public TicketServiceImpl(final TicketValidator ticketValidator,
                             final TicketCalculationService ticketCalculationService,
                             final TicketPaymentService ticketPaymentService,
                             final SeatReservationService seatReservationService) {
        this.ticketValidator = ticketValidator;
        this.ticketCalculationService = ticketCalculationService;
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
    }

    /**
     * Should only have private methods other than the one below.
     */

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if (!ticketValidator.isValidAccountId(accountId)) {
            logger.error(INVALID_ACCOUNT_ID);
            throw new InvalidPurchaseException(INVALID_ACCOUNT_ID);
        }

        if (!ticketValidator.isValidTotalTicketsRequested(ticketTypeRequests)) {
            logger.error(MAX_TICKET_REQUEST);
            throw new InvalidPurchaseException(MAX_TICKET_REQUEST);
        }

        if (!ticketValidator.adultPresentInTicketsRequested(ticketTypeRequests)) {
            logger.error(NO_ADULT_TICKET);
            throw new InvalidPurchaseException(NO_ADULT_TICKET);
        }

        if (!ticketValidator.sufficientAdultTicketsRequested(ticketTypeRequests)) {
            logger.error(NOT_ENOUGH_ADULT_TICKETS);
            throw new InvalidPurchaseException(NOT_ENOUGH_ADULT_TICKETS);
        }

        try {
            final int totalPayment = ticketCalculationService.calculateTotalAmountToPay(ticketTypeRequests);
            final int totalSeats = ticketCalculationService.calculateTotalSeatsToReserve(ticketTypeRequests);

            ticketPaymentService.makePayment(accountId, totalPayment);
            seatReservationService.reserveSeat(accountId, totalSeats);
            logger.info("Tickets have been successfully purchased");
        } catch (Exception ex) {
            logger.error(GENERIC_ERROR);
            throw new InvalidPurchaseException(GENERIC_ERROR, ex);
        }
    }


}

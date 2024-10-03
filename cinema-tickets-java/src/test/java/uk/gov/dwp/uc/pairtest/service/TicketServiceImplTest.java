package uk.gov.dwp.uc.pairtest.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validator.TicketValidator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.*;
import static uk.gov.dwp.uc.pairtest.service.TicketServiceImpl.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    TicketValidator ticketValidator;
    @Mock
    TicketCalculationServiceImpl ticketCalculationService;
    @Mock
    private TicketPaymentServiceImpl ticketPaymentService;
    @Mock
    private SeatReservationServiceImpl seatReservationService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    void shouldThrowException_IfAccountIdIsInvalid() {
        when(ticketValidator.isValidAccountId(null)).thenReturn(false);

        Exception exception = assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(null, new TicketTypeRequest(ADULT, 1));
        });

        assertTrue(exception.getMessage().contains(INVALID_ACCOUNT_ID));
        verifyNoMoreInteractions(ticketValidator, ticketCalculationService, ticketPaymentService, seatReservationService);
    }

    @Test
    void shouldThrowException_IfInvalidTotalTicketRequested() {
        when(ticketValidator.isValidAccountId(1L)).thenReturn(true);
        when(ticketValidator.isValidTotalTicketsRequested(any(TicketTypeRequest.class))).thenReturn(false);

        Exception exception = assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, new TicketTypeRequest(ADULT, 30));
        });

        assertTrue(exception.getMessage().contains(MAX_TICKET_REQUEST));
        verifyNoMoreInteractions(ticketValidator, ticketCalculationService, ticketPaymentService, seatReservationService);
    }

    @Test
    void shouldThrowException_IfNoAdultPresentInTicket() {
        when(ticketValidator.isValidAccountId(1L)).thenReturn(true);
        when(ticketValidator.isValidTotalTicketsRequested(any(TicketTypeRequest.class))).thenReturn(true);
        when(ticketValidator.adultPresentInTicketsRequested(any(TicketTypeRequest.class))).thenReturn(false);

        Exception exception = assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, new TicketTypeRequest(CHILD, 1));
        });

        assertTrue(exception.getMessage().contains(NO_ADULT_TICKET));
        verifyNoMoreInteractions(ticketValidator, ticketCalculationService, ticketPaymentService, seatReservationService);
    }

    @Test
    void shouldThrowException_IfSufficientAdultTicketsNotRequested() {
        when(ticketValidator.isValidAccountId(1L)).thenReturn(true);
        when(ticketValidator.isValidTotalTicketsRequested(any(TicketTypeRequest.class))).thenReturn(true);
        when(ticketValidator.adultPresentInTicketsRequested(any(TicketTypeRequest.class))).thenReturn(true);
        when(ticketValidator.sufficientAdultTicketsRequested(any(TicketTypeRequest.class))).thenReturn(false);

        Exception exception = assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, new TicketTypeRequest(CHILD, 1));
        });

        assertTrue(exception.getMessage().contains(NOT_ENOUGH_ADULT_TICKETS));
        verifyNoMoreInteractions(ticketValidator, ticketCalculationService, ticketPaymentService, seatReservationService);
    }

    @Test
    void shouldPurchaseOneTicket_ValidRequest() {
        when(ticketValidator.isValidAccountId(1L)).thenReturn(true);
        when(ticketValidator.isValidTotalTicketsRequested(any(TicketTypeRequest.class))).thenReturn(true);
        when(ticketValidator.adultPresentInTicketsRequested(any(TicketTypeRequest.class))).thenReturn(true);
        when(ticketValidator.sufficientAdultTicketsRequested(any(TicketTypeRequest.class))).thenReturn(true);
        when(ticketCalculationService.calculateTotalAmountToPay(any(TicketTypeRequest.class))).thenReturn(50);
        when(ticketCalculationService.calculateTotalSeatsToReserve(any(TicketTypeRequest.class))).thenReturn(2);

        final long accountId = 1L;
        ticketService.purchaseTickets(accountId, new TicketTypeRequest(ADULT, 2));

        verify(ticketPaymentService, times(1)).makePayment(accountId, 50);
        verify(seatReservationService, times(1)).reserveSeat(accountId, 2);

        verifyNoMoreInteractions(ticketValidator, ticketCalculationService, ticketPaymentService, seatReservationService);
    }

    @Test
    void shouldPurchaseMultipleTickets_ValidRequest() {
        when(ticketValidator.isValidAccountId(1L)).thenReturn(true);
        when(ticketValidator.isValidTotalTicketsRequested(any(TicketTypeRequest.class), any(TicketTypeRequest.class))).thenReturn(true);
        when(ticketValidator.adultPresentInTicketsRequested(any(TicketTypeRequest.class), any(TicketTypeRequest.class))).thenReturn(true);
        when(ticketValidator.sufficientAdultTicketsRequested(any(TicketTypeRequest.class), any(TicketTypeRequest.class))).thenReturn(true);
        when(ticketCalculationService.calculateTotalAmountToPay(any(TicketTypeRequest.class), any(TicketTypeRequest.class))).thenReturn(90);
        when(ticketCalculationService.calculateTotalSeatsToReserve(any(TicketTypeRequest.class), any(TicketTypeRequest.class))).thenReturn(3);

        final long accountId = 1L;
        ticketService.purchaseTickets(accountId,
                new TicketTypeRequest(ADULT, 2),
                new TicketTypeRequest(CHILD, 1));

        verify(ticketPaymentService, times(1)).makePayment(accountId, 90);
        verify(seatReservationService, times(1)).reserveSeat(accountId, 3);

        verifyNoMoreInteractions(ticketValidator, ticketCalculationService, ticketPaymentService, seatReservationService);
    }

}

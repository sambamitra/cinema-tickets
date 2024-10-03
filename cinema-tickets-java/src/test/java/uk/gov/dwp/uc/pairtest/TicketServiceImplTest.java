package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static uk.gov.dwp.uc.pairtest.TicketServiceImpl.INVALID_ACCOUNT_ID;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.ADULT;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketPaymentServiceImpl ticketPaymentService;
    @Mock
    private SeatReservationServiceImpl seatReservationService;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    void shouldThrowExceptionIfAccountIdIsNull() {
        Exception exception = assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(null, new TicketTypeRequest(ADULT, 1));
        });

        assertTrue(exception.getMessage().contains(INVALID_ACCOUNT_ID));
    }

    @Test
    void shouldThrowExceptionIfAccountIdIsZero() {
        Exception exception = assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(0L, new TicketTypeRequest(ADULT, 1));
        });

        assertTrue(exception.getMessage().contains(INVALID_ACCOUNT_ID));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -2L, -50L, -999L})
    void shouldThrowExceptionIfAccountIdIsLessThanZero(final Long accountId) {
        Exception exception = assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(accountId, new TicketTypeRequest(ADULT, 1));
        });

        assertTrue(exception.getMessage().contains(INVALID_ACCOUNT_ID));
    }

    @Test
    void shouldMakePaymentAndReserveSeats() {
        final long accountId = 1L;
        ticketService.purchaseTickets(accountId, new TicketTypeRequest(ADULT, 1));

        verify(ticketPaymentService, times(1)).makePayment(accountId, 0);
        verify(seatReservationService, times(1)).reserveSeat(accountId, 0);

        verifyNoMoreInteractions(ticketPaymentService);
        verifyNoMoreInteractions(seatReservationService);
    }
}

package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.dwp.uc.pairtest.TicketServiceImpl.INVALID_ACCOUNT_ID;


class TicketServiceImplTest {

    private TicketService ticketService = new TicketServiceImpl();

    @Test
    void shouldThrowExceptionIfAccountIdIsNull() {
        Exception exception = assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(null, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));
        });

        assertTrue(exception.getMessage().contains(INVALID_ACCOUNT_ID));
    }

    @Test
    void shouldThrowExceptionIfAccountIdIsZero() {
        Exception exception = assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(0L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));
        });

        assertTrue(exception.getMessage().contains(INVALID_ACCOUNT_ID));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -2L, -50L, -999L})
    void shouldThrowExceptionIfAccountIdIsLessThanZero(final Long accountId) {
        Exception exception = assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(accountId, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));
        });

        assertTrue(exception.getMessage().contains(INVALID_ACCOUNT_ID));
    }
}

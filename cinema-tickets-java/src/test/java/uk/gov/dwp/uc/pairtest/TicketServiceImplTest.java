package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.Test;
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

    @Test
    void shouldThrowExceptionIfAccountIdIsLessThanZero() {
        Exception exception = assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(-1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1));
        });

        assertTrue(exception.getMessage().contains(INVALID_ACCOUNT_ID));
    }
}

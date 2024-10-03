package uk.gov.dwp.uc.pairtest.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.*;
import static uk.gov.dwp.uc.pairtest.validator.TicketValidator.MAX_TICKETS;

class TicketValidatorTest {

    private final TicketValidator ticketValidator = new TicketValidator();

    @Nested
    class IsValidAccountIdTest {
        @Test
        void invalid_accountIdIsNull() {
            assertFalse(ticketValidator.isValidAccountId(null));
        }

        @Test
        void invalid_accountIdIsZeero() {
            assertFalse(ticketValidator.isValidAccountId(0L));
        }

        @ParameterizedTest
        @ValueSource(longs = {-1L, -2L, -50L, -999L})
        void invalid_accountIdIsLessThanZeero(final Long accountId) {
            assertFalse(ticketValidator.isValidAccountId(accountId));
        }

        @ParameterizedTest
        @ValueSource(longs = {1L, 2L, 55L, 99999L})
        void valid_accountIdIsGreaterThanZeero(final Long accountId) {
            assertTrue(ticketValidator.isValidAccountId(accountId));
        }
    }

    @Nested
    class IsValidTotalTicketsRequestedTest {
        @Test
        void invalid_NoTicketRequested() {
            assertFalse(ticketValidator.isValidTotalTicketsRequested());
        }

        @Test
        void invalid_TicketRequestIsNull() {
            assertFalse(ticketValidator.isValidTotalTicketsRequested(null));
        }

        @ParameterizedTest
        @ValueSource(ints = {26, 27, 33, 58})
        void invalid_MoreThanMaxTicketsRequested(final int numberOfTickets) {
            assertFalse(ticketValidator.isValidTotalTicketsRequested(new TicketTypeRequest(ADULT, numberOfTickets)));
        }

        @Test
        void valid_AtLeastOneTicketRequested() {
            assertTrue(ticketValidator.isValidTotalTicketsRequested(new TicketTypeRequest(ADULT, 1)));
        }

        @Test
        void valid_MoreThanOneTicketRequested() {
            assertTrue(ticketValidator.isValidTotalTicketsRequested(
                    new TicketTypeRequest(ADULT, 1),
                    new TicketTypeRequest(ADULT, 10)));
        }

        @Test
        void valid_MaxTicketsRequested() {
            assertTrue(ticketValidator.isValidTotalTicketsRequested(new TicketTypeRequest(ADULT, MAX_TICKETS)));
        }
    }

    @Nested
    class AdultPresentInTicketRequestedTest {
        @ParameterizedTest
        @EnumSource(value = TicketTypeRequest.Type.class, names = "ADULT", mode = EnumSource.Mode.EXCLUDE)
        void invalid_NoAdultPresentInTicket(final TicketTypeRequest.Type type) {
            assertFalse(ticketValidator.adultPresentInTicketsRequested(new TicketTypeRequest(type, 1)));
        }

        @Test
        void invalid_AdultPresentInTicketWithZeroNumber() {
            assertFalse(ticketValidator.adultPresentInTicketsRequested(
                    new TicketTypeRequest(ADULT, 0),
                    new TicketTypeRequest(CHILD, 10),
                    new TicketTypeRequest(INFANT, 5)));
        }

        @Test
        void valid_AdultPresentInTicket() {
            assertTrue(ticketValidator.adultPresentInTicketsRequested(
                    new TicketTypeRequest(ADULT, 1),
                    new TicketTypeRequest(CHILD, 10),
                    new TicketTypeRequest(INFANT, 5)));
        }
    }

    @Nested
    class SufficientAdultTicketsRequestedTest {
        @ParameterizedTest
        @CsvSource({
                "0, 1",
                "1, 2",
                "15, 20"})
        void invalid_AdultsLessThanInfantsRequested(final int adults, final int infants) {
            assertFalse(ticketValidator.sufficientAdultTicketsRequested(
                    new TicketTypeRequest(ADULT, adults),
                    new TicketTypeRequest(INFANT, infants)));
        }

        @ParameterizedTest
        @CsvSource({
                "1, 1",
                "2, 1",
                "20, 19"})
        void valid_AdultsMoreThanOrEqualToInfantsRequested(final int adults, final int infants) {
            assertTrue(ticketValidator.sufficientAdultTicketsRequested(
                    new TicketTypeRequest(ADULT, adults),
                    new TicketTypeRequest(INFANT, infants)));
        }
    }

}

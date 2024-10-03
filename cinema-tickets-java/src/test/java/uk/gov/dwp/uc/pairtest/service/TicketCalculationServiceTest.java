package uk.gov.dwp.uc.pairtest.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.*;

public class TicketCalculationServiceTest {

    private final TicketCalculationService ticketCalculationService = new TicketCalculationServiceImpl();

    @Nested
    class CalculateTotalSeatsToReserveTest {
        @Test
        void one_Adult() {
            final int totalSeats = ticketCalculationService.calculateTotalSeatsToReserve(new TicketTypeRequest(ADULT, 1));
            assertEquals(1, totalSeats);
        }

        @Test
        void two_Adults() {
            final int totalSeats = ticketCalculationService.calculateTotalSeatsToReserve(new TicketTypeRequest(ADULT, 2));
            assertEquals(2, totalSeats);
        }

        @Test
        void one_Adult_One_Child() {
            final int totalSeats = ticketCalculationService.calculateTotalSeatsToReserve(
                    new TicketTypeRequest(ADULT, 1),
                    new TicketTypeRequest(CHILD, 1));
            assertEquals(2, totalSeats);
        }

        @Test
        void one_Adult_One_Child_One_Infant() {
            final int totalSeats = ticketCalculationService.calculateTotalSeatsToReserve(
                    new TicketTypeRequest(ADULT, 1),
                    new TicketTypeRequest(CHILD, 1),
                    new TicketTypeRequest(INFANT, 1));
            assertEquals(2, totalSeats);
        }

        @Test
        void five_Adults_Six_Children_Three_Infants() {
            final int totalSeats = ticketCalculationService.calculateTotalSeatsToReserve(
                    new TicketTypeRequest(ADULT, 5),
                    new TicketTypeRequest(CHILD, 6),
                    new TicketTypeRequest(INFANT, 3));
            assertEquals(11, totalSeats);
        }

        @Test
        void invalid_NullRequest() {
            final int totalSeats = ticketCalculationService.calculateTotalSeatsToReserve(null);
            assertEquals(0, totalSeats);
        }

        @Test
        void invalid_NoRequest() {
            final int totalSeats = ticketCalculationService.calculateTotalSeatsToReserve();
            assertEquals(0, totalSeats);
        }
    }

    @Nested
    class CalculateTotalAmountToPayTest {
        @Test
        void one_Adult() {
            final int totalAmount = ticketCalculationService.calculateTotalAmountToPay(new TicketTypeRequest(ADULT, 1));
            assertEquals(25, totalAmount);
        }

        @Test
        void two_Adults() {
            final int totalAmount = ticketCalculationService.calculateTotalAmountToPay(new TicketTypeRequest(ADULT, 2));
            assertEquals(50, totalAmount);
        }

        @Test
        void one_Adult_One_Child() {
            final int totalAmount = ticketCalculationService.calculateTotalAmountToPay(
                    new TicketTypeRequest(ADULT, 1),
                    new TicketTypeRequest(CHILD, 1));
            assertEquals(40, totalAmount);
        }

        @Test
        void one_Adult_One_Child_One_Infant() {
            final int totalAmount = ticketCalculationService.calculateTotalAmountToPay(
                    new TicketTypeRequest(ADULT, 1),
                    new TicketTypeRequest(CHILD, 1),
                    new TicketTypeRequest(INFANT, 1));
            assertEquals(40, totalAmount);
        }

        @Test
        void five_Adults_Six_Children_Three_Infants() {
            final int totalAmount = ticketCalculationService.calculateTotalAmountToPay(
                    new TicketTypeRequest(ADULT, 5),
                    new TicketTypeRequest(CHILD, 6),
                    new TicketTypeRequest(INFANT, 3));
            assertEquals(215, totalAmount);
        }

        @Test
        void invalid_NullRequest() {
            final int totalAmount = ticketCalculationService.calculateTotalAmountToPay(null);
            assertEquals(0, totalAmount);
        }

        @Test
        void invalid_NoRequest() {
            final int totalAmount = ticketCalculationService.calculateTotalAmountToPay();
            assertEquals(0, totalAmount);
        }
    }
}

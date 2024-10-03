package uk.gov.dwp.uc.pairtest.service;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.Arrays;

public class TicketCalculationServiceImpl implements TicketCalculationService {

    @Override
    public int calculateTotalSeatsToReserve(final TicketTypeRequest... ticketTypeRequests) {
        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            return 0;
        }
        return Arrays.stream(ticketTypeRequests)
                .filter(ticketTypeRequest -> ticketTypeRequest.getTicketType() != TicketTypeRequest.Type.INFANT)
                .mapToInt(TicketTypeRequest::getNoOfTickets).sum();
    }

    @Override
    public int calculateTotalAmountToPay(final TicketTypeRequest... ticketTypeRequests) {
        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            return 0;
        }
        int totalPayment = 0;
        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {
            totalPayment += ticketTypeRequest.getTicketType().getPrice() * ticketTypeRequest.getNoOfTickets();
        }
        return totalPayment;
    }
}

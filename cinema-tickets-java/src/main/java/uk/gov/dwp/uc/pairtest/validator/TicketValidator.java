package uk.gov.dwp.uc.pairtest.validator;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.Arrays;

import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.ADULT;
import static uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest.Type.INFANT;

public class TicketValidator {

    public static final int MAX_TICKETS = 25;

    public boolean isValidAccountId(final Long accountId) {
        return accountId != null && accountId > 0;
    }

    public boolean isValidTotalTicketsRequested(final TicketTypeRequest... ticketTypeRequests) {
        return ticketTypeRequests != null && ticketTypeRequests.length > 0 &&
                Arrays.stream(ticketTypeRequests).mapToInt(TicketTypeRequest::getNoOfTickets).sum() <= MAX_TICKETS;
    }

    public boolean adultPresentInTicketsRequested(final TicketTypeRequest... ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests).anyMatch(ticketTypeRequest -> ticketTypeRequest.getTicketType() == ADULT && ticketTypeRequest.getNoOfTickets() > 0);
    }

    public boolean sufficientAdultTicketsRequested(final TicketTypeRequest... ticketTypeRequests) {
        int totalAdultTicketsRequested = 0;
        int totalInfantTicketsRequested = 0;
        for (TicketTypeRequest ticketTypeRequest : ticketTypeRequests) {
            if (ticketTypeRequest.getTicketType() == ADULT) {
                totalAdultTicketsRequested += ticketTypeRequest.getNoOfTickets();
            } else if (ticketTypeRequest.getTicketType() == INFANT) {
                totalInfantTicketsRequested += ticketTypeRequest.getNoOfTickets();
            }
        }
        return totalAdultTicketsRequested >= totalInfantTicketsRequested;
    }


}

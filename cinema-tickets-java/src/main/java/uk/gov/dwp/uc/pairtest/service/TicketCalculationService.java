package uk.gov.dwp.uc.pairtest.service;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public interface TicketCalculationService {
    int calculateTotalSeatsToReserve(final TicketTypeRequest... ticketTypeRequests);

    int calculateTotalAmountToPay(final TicketTypeRequest... ticketTypeRequests);
}

package uk.gov.dwp.uc.pairtest.exception;

public final class ErrorMessages {
    private ErrorMessages() {}

    public static final String INVALID_ACCOUNT_ID = "Invalid account ID supplied";
    public static final String MAX_TICKET_REQUEST = "More than maximum tickets requested";
    public static final String NO_ADULT_TICKET = "No adult ticket requested";
    public static final String NOT_ENOUGH_ADULT_TICKETS = "Insufficient adult tickets for number of infant tickets requested";
    public static final String GENERIC_ERROR = "Exception whilst purchasing tickets";

}

package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable Object
 */

public final class TicketTypeRequest {

    private int noOfTickets;
    private Type type;

    public TicketTypeRequest(Type type, int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public enum Type {
        ADULT(25), CHILD(15) , INFANT(0);

        private final int unitPrice;

        Type(int unitPrice) {
            this.unitPrice = unitPrice;
        }

        public int getUnitPrice() {
            return unitPrice;
        }
    }

}

package ad.bingo.generator.records;

import ad.bingo.Config;

public record TicketStrip(Ticket[] tickets) {

    public Ticket ticket(int id) {
        return tickets[id];
    }

    public TicketStrip() {
        this(new Ticket[Config.TicketsInStrip]);
        for (int i = 0; i < tickets.length; i++) {
            tickets[i] = new Ticket();
        }
    }
}

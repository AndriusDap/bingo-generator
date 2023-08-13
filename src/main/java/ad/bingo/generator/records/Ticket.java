package ad.bingo.generator.records;

import ad.bingo.Config;

public record Ticket(int[][] fields) {
    public Ticket() {
        this(new int[Config.RowsPerTicket][]);
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new int[Config.ColumnsPerTicket];
        }
    }
}

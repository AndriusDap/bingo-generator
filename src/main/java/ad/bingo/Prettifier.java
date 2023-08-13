package ad.bingo;

import ad.bingo.generator.records.Ticket;
import ad.bingo.generator.records.TicketStrip;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Prettifier {

    public static void prettyPrint(TicketStrip strip) {
        for (Ticket ticket : strip.tickets()) {
            prettyPrint(ticket);
        }
    }

    private static String formatRow(int[] row) {
        return Arrays.stream(row).mapToObj(i ->
                i == 0 ? "    " : String.format("%3d ", i)).collect(Collectors.joining("│")
        );
    }

    public static void prettyPrint(Ticket ticket) {
        var builder = new StringBuilder();
        //Let's build the top line of the table
        builder.append("╔");
        builder.append("════╦".repeat(Config.ColumnsPerTicket - 1));
        builder.append("════╗\n");
        // Now comes the content
        var rows = Arrays.stream(ticket.fields())
                .map(Prettifier::formatRow)
                .map(row -> "║" + row + "║")
                .collect(Collectors
                        .joining("\n╠" + "────┼".repeat(Config.ColumnsPerTicket - 1) + "────╣\n")
                );
        builder.append(rows);
        // And finally the bottom of the table
        builder.append("\n╚");
        builder.append("════╩".repeat(Config.ColumnsPerTicket - 1));
        builder.append("════╝\n");

        System.out.println(builder);
    }
}

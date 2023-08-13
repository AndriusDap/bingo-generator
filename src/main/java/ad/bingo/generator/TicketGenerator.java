package ad.bingo.generator;

import ad.bingo.Config;
import ad.bingo.generator.records.TicketStrip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class TicketGenerator {

    /**
     * Generating ticket strip can be broken down into three stages:
     * <p>
     * Stage 1:
     * Figure out the distribution of numbers in all the columns for every ticket:
     * <pre>
     * {@code
     * ticket#1  [1, 1, 2, 1, 2, 2, 1, 2, 3]
     * ticket#2  [1, 2, 2, 3, 2, 1, 2, 1, 1]
     * ticket#3  [1, 3, 2, 1, 1, 1, 3, 2, 1]
     * ticket#4  [2, 1, 1, 2, 2, 3, 1, 1, 2]
     * ticket#5  [2, 1, 2, 2, 2, 2, 1, 2, 1]
     * ticket#6  [2, 2, 1, 1, 1, 1, 2, 2, 3]
     * }
     * </pre>
     * <p>
     * This way we can guarantee that we can use all the necessary numbers in the strip.
     * <p>
     * Stage 2:
     * Expand each ticket template into how we want the ticket to look like, figure out how numbers
     * are placed into columns:
     * <pre>
     * {@code
     * [1, 1, 2, 1, 2, 2, 1, 2, 3]
     * is transformed to
     * [0, 1, 1, 0, 1, 0, 0, 1, 1]
     * [0, 0, 1, 0, 0, 1, 1, 1, 1]
     * [1, 0, 0, 1, 1, 1, 0, 0, 1]
     * }
     * </pre>
     * Stage 3:
     * Populate columns with numbers in the designated locations - {@link #fillColumn(Random, int, ArrayList, TicketStrip)}
     */
    public TicketStrip ticketStrip(Random random) {


        var template = MatrixFiller.produce(
                random,
                Config.NumbersPerStripColumn,
                Config.NumbersPerStripTicket,
                Config.MinimumNumbersPerColumn,
                Config.MaximumNumbersPerColumn
        );

        var ticketShapes = new ArrayList<int[][]>();

        for (var ticket : template) {
            ticketShapes.add(MatrixFiller.produce(
                    random,
                    ticket,
                    Config.NumbersPerTicketRow,
                    0, // Since here we are just generating a shape for the ticket we're using 0 as empty and 1 as a number placeholder
                    1
            ));

        }

        var ticketStrip = new TicketStrip();

        for (int i = 0; i < Config.ColumnsPerTicket; i++) {
            fillColumn(random, i, ticketShapes, ticketStrip);
        }

        return ticketStrip;
    }

    private int columnStartValue(int columnId) {
        return columnId == 0 ? 1 : columnId * 10;
    }

    private void fillColumn(Random random, int columnId, ArrayList<int[][]> ticketTemplate, TicketStrip strip) {
        var counter = columnStartValue(columnId);
        var fillOrder = Arrays.asList(0, 1, 2, 3, 4, 5);
        Collections.shuffle(fillOrder, random);

        for (int i : fillOrder) {
            var ticket = strip.ticket(i);
            var template = ticketTemplate.get(i);

            for (int j = 0; j < template.length; j++) {
                if (template[j][columnId] == 1) {
                    ticket.fields()[j][columnId] = counter++;
                }
            }
        }
    }
}


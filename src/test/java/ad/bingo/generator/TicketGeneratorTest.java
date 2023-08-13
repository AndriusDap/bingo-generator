package ad.bingo.generator;

import ad.bingo.generator.records.TicketStrip;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TicketGeneratorTest {

    public static final int ITERATIONS_TO_RUN = 100;

    static Stream<TicketStrip> ticketStrips() {
        Random random = new Random();
        TicketGenerator generator = new TicketGenerator();
        return Stream.generate(() -> generator.ticketStrip(random)).limit(ITERATIONS_TO_RUN);
    }


    @ParameterizedTest
    @MethodSource("ticketStrips")
    public void stripContainsSixTickets(TicketStrip strip) {
        assertEquals(6, strip.tickets().length);
    }

    @ParameterizedTest
    @MethodSource("ticketStrips")
    public void allNumbersFrom1To90AppearOnTheStrip(TicketStrip strip) {
        var numbers = new int[91];
        for (var ticket : strip.tickets()) {
            for (int[] row : ticket.fields()) {
                for (int i : row) {
                    numbers[i]++;
                }
            }
        }
        assertEquals(72, numbers[0]);
        for (int i = 1; i < numbers.length; i++) {
            assertEquals(1, numbers[i], "Did not find " + i);
        }
    }

    @ParameterizedTest
    @MethodSource("ticketStrips")
    public void ticketConsistsOf9ColumnsAnd3Rows(TicketStrip strip) {
        assertTrue(strip.tickets().length > 0);
        for (var ticket : strip.tickets()) {
            assertEquals(3, ticket.fields().length);
            for (var row : ticket.fields()) {
                assertEquals(9, row.length);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("ticketStrips")
    public void eachRowContains5NumbersAndFourBlanks(TicketStrip strip) {
        for (var ticket : strip.tickets()) {
            for (var row : ticket.fields()) {
                var blanks = Arrays.stream(row).filter(i -> i == 0).count();
                var filledIn = Arrays.stream(row).filter(i -> i != 0).count();
                assertEquals(4, blanks);
                assertEquals(5, filledIn);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("ticketStrips")
    public void columnsAreNotBlank(TicketStrip strip) {
        for (var ticket : strip.tickets()) {
            var columnsHaveValues = new int[9];

            for (var row : ticket.fields()) {
                for (int i = 0; i < row.length; i++) {
                    if (row[i] != 0) {
                        columnsHaveValues[i]++;
                    }
                }
            }
            var blankCount = Arrays.stream(columnsHaveValues).filter(i -> i == 0).count();
            assertEquals(0, blankCount);
        }
    }

    @ParameterizedTest
    @MethodSource("ticketStrips")
    public void columnsContainProperNumbers(TicketStrip strip) {
        for (var ticket : strip.tickets()) {
            for (var row : ticket.fields()) {
                for (int i = 0; i < row.length; i++) {
                    if (row[i] != 0) {
                        switch (i) {
                            case 0 -> assertTrue(row[i] > 0 && row[i] <= 9);
                            case 1 -> assertTrue(row[i] >= 10 && row[i] <= 19);
                            case 2 -> assertTrue(row[i] >= 20 && row[i] <= 29);
                            case 3 -> assertTrue(row[i] >= 30 && row[i] <= 39);
                            case 4 -> assertTrue(row[i] >= 40 && row[i] <= 49);
                            case 5 -> assertTrue(row[i] >= 50 && row[i] <= 59);
                            case 6 -> assertTrue(row[i] >= 60 && row[i] <= 69);
                            case 7 -> assertTrue(row[i] >= 70 && row[i] <= 79);
                            case 8 -> assertTrue(row[i] >= 80 && row[i] <= 90);
                        }
                    }
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("ticketStrips")
    public void columnsOrderedFromTopToBottom(TicketStrip strip) {
        for (var ticket : strip.tickets()) {
            for (int i = 0; i < 9; i++) {
                var columnValues = Stream.of(ticket.fields()[0][i], ticket.fields()[1][i], ticket.fields()[2][i])
                        .filter(v -> v != 0).toList();

                Assertions.assertIterableEquals(columnValues.stream().sorted().toList(), columnValues);
            }
        }
    }
}
package ad.bingo;

import ad.bingo.generator.TicketGenerator;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("Grab your tickets while they're hot!");
        var random = new Random();
        var ticketGenerator = new TicketGenerator();

        var template = ticketGenerator.ticketStrip(random);

        Prettifier.prettyPrint(template);

        var startTime = System.currentTimeMillis();
        var testCount = 10000;
        for (int i = 0; i < testCount; i++) {
            ticketGenerator.ticketStrip(random);
        }
        var endTime = System.currentTimeMillis();
        System.out.println("It took " + (endTime - startTime) + " milliseconds to generate " + testCount + " strips.");
    }
}
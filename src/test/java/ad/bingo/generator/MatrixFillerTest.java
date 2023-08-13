package ad.bingo.generator;

import ad.bingo.Config;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MatrixFillerTest {

    @Test
    public void distributionIsFair() {
        var random = new Random();

        var sums = new int[6][9];

        for (int i = 0; i < 100000; i++) {
            var matrix = MatrixFiller.produce(
                    random,
                    Config.NumbersPerStripColumn,
                    Config.NumbersPerStripTicket,
                    Config.MinimumNumbersPerColumn,
                    Config.MaximumNumbersPerColumn
            );

            for (int j = 0; j < matrix.length; j++) {
                for (int k = 0; k < matrix[j].length; k++) {
                    sums[j][k] += matrix[j][k];
                }
            }
        }

        var fields = new ArrayList<Double>();
        for (int[] sum : sums) {
            for (int i = 0; i < sum.length; i++) {
                fields.add((double) sum[i] / Config.NumbersPerStripColumn[i]);
            }
        }

        // We should do some fancier tests, at least a basic chi squared, but let's not bother with that right now,
        // delta from mean should be good enough
        var mean = fields.stream().mapToDouble(Double::doubleValue).sum() / fields.size();
        var percentDeltaFromMean = fields.stream().mapToDouble(i -> Math.abs((1 - i / mean)) * 100);

        var maxDeltaPercent = percentDeltaFromMean.max().orElseThrow();
        // Less than one percent gap is uniform enough
        assertTrue(maxDeltaPercent < 1);
    }

}
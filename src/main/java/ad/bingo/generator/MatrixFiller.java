package ad.bingo.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

public class MatrixFiller {


    /**
     * Wrapper for {@link #produceOpt(Random, int[], int[], int, int)}. Will recursively attempt to create a matrix,
     * this may lead to a stack overflow if we're unlucky or configuration is too complicated.
     */
    public static int[][] produce(Random random,
                                  int[] columnRequirements,
                                  int[] rowRequirements,
                                  int baseValue,
                                  int maximumValue) {
        return produceOpt(random, columnRequirements, rowRequirements, baseValue, maximumValue)
                .orElseGet(() -> produce(random, columnRequirements, rowRequirements, baseValue, maximumValue));
    }

    /**
     * Will produce a matrix where sum of all columns sum up to values in {@code columnRequirements} and all the rows
     * add up to {@code rowRequirements}.
     *
     * @param random             instance of randomness
     * @param columnRequirements Columns of the produced matrix will add up to this vector
     * @param rowRequirements    Rows of the produced matrix will add up to this vector
     * @param baseValue          Minimal possible value for the resulting matrix field
     * @param maximumValue       Maximum possible value for the resulting matrix field
     * @return Matrix if it was generated or none if it is not possible to build it given this random instance.
     * Since {@code random} is updated after every use it should be enough to simply retry using same random instance
     */
    public static Optional<int[][]> produceOpt(
            Random random,
            int[] columnRequirements,
            int[] rowRequirements,
            int baseValue,
            int maximumValue
    ) {
        var output = new int[rowRequirements.length][columnRequirements.length];
        for (int[] ints : output) {
            // Since we know the minimal value for every field we can eagerly populate it.
            Arrays.fill(ints, baseValue);
        }
        // We need to track current row/column sums to compare against the requirements
        var rowSums = new int[rowRequirements.length];
        Arrays.fill(rowSums, columnRequirements.length * baseValue);
        var columnSums = new int[columnRequirements.length];
        Arrays.fill(columnSums, rowRequirements.length * baseValue);

        // Total sum is also beneficial, just so we don't need to sum the in progress row/column arrays for each iteration
        var totalSum = columnRequirements.length * rowRequirements.length * baseValue;
        var requiredSum = IntStream.of(rowRequirements).sum();

        do {
            // As a first step we identify all the possible locations where a value can be incremented for this matrix
            // without going over the given requirements
            var potentialPositions = new ArrayList<Pair>();

            for (int i = 0; i < rowSums.length; i++) {
                for (int j = 0; j < columnSums.length; j++) {
                    if (rowSums[i] < rowRequirements[i] && columnSums[j] < columnRequirements[j] && output[i][j] < maximumValue) {
                        potentialPositions.add(new Pair(i, j));
                    }
                }
            }
            // It is possible that we got into an unfortunate situation where we cannot add anything to the matrix
            // without going over the limits, yet it is not complete yet. Returning an empty is a simple way to signal
            // that the caller should retry
            if (potentialPositions.isEmpty()) {
                return Optional.empty();
            }

            // Attempt to populate multiple fields on every iteration, so we don't need to traverse the whole matrix
            // every time, as computing the potential positions is quite expensive
            while (!potentialPositions.isEmpty() && totalSum != requiredSum) {
                var toUpdate = potentialPositions.get(random.nextInt(potentialPositions.size()));

                output[toUpdate.row()][toUpdate.column()]++;
                // Need to update our intermediate lookup tables, so we don't recompute them again.
                columnSums[toUpdate.column()]++;
                rowSums[toUpdate.row()]++;
                totalSum++;

                // We need to discard all the potential positions that are no longer valid
                potentialPositions.removeIf(p -> p.column == toUpdate.column() || p.row == toUpdate.row());
            }


        } while (totalSum != requiredSum);

        return Optional.of(output);
    }


    private record Pair(int row, int column) {
    }
}

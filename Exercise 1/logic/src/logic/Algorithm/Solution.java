package logic.Algorithm;

import java.util.Arrays;
import java.util.Random;

public class Solution {

    private static final Random rand = new Random();
    private static int[] maxValues;
    private final int[] gens;

    public static int[] getMaxValues() {
        return maxValues;
    }

    public static void setMaxValues(int[] maxValues) {
        Solution.maxValues = maxValues;
    }

    public Solution() {
        this.gens = new int[maxValues.length];

        for (int i = 0; i < maxValues.length; i++) {
            gens[i] = rand.nextInt(maxValues[i] + 1);
        }
    }

    public float getFitness() {
        return ((float) Arrays.stream(gens).sum()) / Arrays.stream(maxValues).sum();
    }

    public int getGen(int idx) {
        return gens[idx];
    }

    public void setGen(int idx, int value) {
        // TODO: Do I need to check this?
        if (value <= maxValues[idx]) {
            gens[idx] = value;
        }
    }

    @Override
    public String toString() {
        return "Solution{" +
                "gens=" + Arrays.toString(gens) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solution solution = (Solution) o;
        return Arrays.equals(gens, solution.gens);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(gens);
    }
}

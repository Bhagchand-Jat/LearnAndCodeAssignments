package Chapter_2.Assignment_2;

import java.util.Scanner;

class SubarrayCalculator {

    public static long[] computePrefixSums(long[] elements, int noOfElements) {
        long[] prefixSums = new long[noOfElements + 1];
        for (int prefixSumsIndex = 1; prefixSumsIndex <= noOfElements; prefixSumsIndex++) {
            prefixSums[prefixSumsIndex] = prefixSums[prefixSumsIndex - 1] + elements[prefixSumsIndex];
        }
        return prefixSums;
    }

    public static StringBuilder processRangeQueries(int totalQueries, long[] prefixSums, Scanner inputScanner) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < totalQueries; i++) {
            int leftIndex = inputScanner.nextInt();
            int rightIndex = inputScanner.nextInt();
            long sum = calculateSum(leftIndex, rightIndex, prefixSums);
            long mean = calculateMean(sum, leftIndex, rightIndex);
            result.append(mean).append("\t");
        }
        return result;
    }

    public static long calculateSum(int leftIndex, int rightIndex, long[] prefixSums) {
        return prefixSums[rightIndex] - prefixSums[leftIndex - 1];
    }

    public static long calculateMean(long sum, int leftIndex, int rightIndex) {
        return sum / (rightIndex - leftIndex + 1);
    }
}
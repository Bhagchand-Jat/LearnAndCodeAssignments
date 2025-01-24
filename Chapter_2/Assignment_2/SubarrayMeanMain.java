package Chapter_2.Assignment_2;

import java.util.Scanner;

public class SubarrayMeanMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int noOfElements = scanner.nextInt();
        int noOfQueries = scanner.nextInt();
        long[] elements = new long[noOfElements + 1];
        for (int elementIndex = 1; elementIndex <= noOfElements; elementIndex++) {
            elements[elementIndex] = scanner.nextLong();
        }

        long[] prefixSums = SubarrayCalculator.computePrefixSums(elements, noOfElements);
        StringBuilder result =SubarrayCalculator.processRangeQueries(noOfQueries, prefixSums, scanner );

        System.out.print(result);
        scanner.close();
    }
}




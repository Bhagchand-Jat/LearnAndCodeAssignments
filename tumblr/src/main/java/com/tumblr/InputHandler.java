package com.tumblr;

import java.util.Scanner;

public class InputHandler {
    public static String getInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int[] parseRange(String rangeInput) {
        String[] parts = rangeInput.split("-");
        if (parts.length != 2) {
            System.out.println("Invalid range format. Use 'start-end'.");
            return null;
        }

        try {
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);
            if (start < 1 || end < start) {
                System.out.println("Invalid range values. Start must be ≥1 and end ≥ start.");
                return null;
            }
            return new int[]{start, end};
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Enter numeric values.");
            return null;
        }
    }
}

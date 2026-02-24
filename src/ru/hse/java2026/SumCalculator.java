package ru.hse.java2026;


public final class SumCalculator {

    private SumCalculator() {
        // Prevent instantiation
    }

    public static void main(String[] args) {

        int sum = 0;

        for (String argument : args) {
            sum += sumIntegersInString(argument);
        }

        System.out.println(sum);
    }

    private static int sumIntegersInString(String text) {
        if (text == null || text.isBlank()) {
            return 0;
        }

        int localSum = 0;
        int i = 0;

        while (i < text.length()) {
            // 1) Skip any whitespace
            while (i < text.length() && Character.isWhitespace(text.charAt(i))) {
                i++;
            }
            if (i >= text.length()) {
                break;
            }

            // 2) Read optional sign
            int sign = 1;
            char ch = text.charAt(i);
            if (ch == '+' || ch == '-') {
                sign = (ch == '-') ? -1 : 1;
                i++;
            }

            // 3) Must have at least one digit to be a number
            if (i >= text.length() || !Character.isDigit(text.charAt(i))) {

                continue;
            }

            // 4) Read digits
            int value = 0;
            while (i < text.length() && Character.isDigit(text.charAt(i))) {
                int digit = text.charAt(i) - '0';
                value = value * 10 + digit;
                i++;
            }

            localSum += sign * value;
        }

        return localSum;
    }
}
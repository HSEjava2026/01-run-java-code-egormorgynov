package sum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public final class SumBigDecimalHex {

    private SumBigDecimalHex() {
    }

    public static void main(String[] args) {
        BigDecimal sum = BigDecimal.ZERO;
        boolean hasNumber = false;

        for (String argument : args) {
            ParseResult r = sumNumbers(argument);
            sum = sum.add(r.sum);
            hasNumber |= r.hasNumber;
        }

        if (!hasNumber) {
            System.out.println("0");
            return;
        }

        BigDecimal rounded = sum.setScale(3, RoundingMode.HALF_UP);

        if (rounded.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("0.000");
            return;
        }

        BigDecimal stripped = rounded.stripTrailingZeros();
        if (stripped.scale() <= 0) {
            System.out.println(stripped.toBigIntegerExact().toString());
        } else {
            System.out.println(rounded.toPlainString());
        }
    }

    private static ParseResult sumNumbers(String text) {
        if (text == null || text.isBlank()) {
            return new ParseResult(BigDecimal.ZERO, false);
        }

        BigDecimal localSum = BigDecimal.ZERO;
        boolean hasNumber = false;
        int i = 0;

        while (i < text.length()) {
            while (i < text.length() && Character.isWhitespace(text.charAt(i))) {
                i++;
            }
            if (i >= text.length()) {
                break;
            }

            int start = i;
            while (i < text.length() && !Character.isWhitespace(text.charAt(i))) {
                i++;
            }

            String token = text.substring(start, i);

            localSum = localSum.add(parseToken(token));
            hasNumber = true;
        }

        return new ParseResult(localSum, hasNumber);
    }

    private static BigDecimal parseToken(String token) {
        String t = token.trim();
        if (t.isEmpty()) {
            throw new NumberFormatException();
        }

        int sPos = indexOfS(t);
        if (sPos >= 0) {
            String left = t.substring(0, sPos);
            String right = t.substring(sPos + 1);

            int sign = 1;
            if (!left.isEmpty() && (left.charAt(0) == '+' || left.charAt(0) == '-')) {
                sign = left.charAt(0) == '-' ? -1 : 1;
                left = left.substring(1);
            }

            if (!(left.startsWith("0x") || left.startsWith("0X"))) {
                throw new NumberFormatException();
            }
            left = left.substring(2);

            if (left.isEmpty() || right.isEmpty()) {
                throw new NumberFormatException();
            }

            BigInteger mantissa = new BigInteger(left, 16);
            if (sign < 0) {
                mantissa = mantissa.negate();
            }

            int scale = parseSignedHexInt(right);
            return new BigDecimal(mantissa, scale);
        }

        if (isHexPrefixed(t)) {
            int sign = 1;
            if (t.charAt(0) == '+' || t.charAt(0) == '-') {
                sign = t.charAt(0) == '-' ? -1 : 1;
                t = t.substring(1);
            }

            t = t.substring(2);
            if (t.isEmpty()) {
                throw new NumberFormatException();
            }

            BigInteger mantissa = new BigInteger(t, 16);
            if (sign < 0) {
                mantissa = mantissa.negate();
            }
            return new BigDecimal(mantissa, 0);
        }

        return new BigDecimal(t);
    }

    private static boolean isHexPrefixed(String t) {
        if (t.length() >= 2 && (t.startsWith("0x") || t.startsWith("0X"))) {
            return true;
        }
        return t.length() >= 3 && (t.startsWith("+0x") || t.startsWith("+0X") || t.startsWith("-0x") || t.startsWith("-0X"));
    }

    private static int parseSignedHexInt(String s) {
        String t = s.trim();
        if (t.isEmpty()) {
            throw new NumberFormatException();
        }

        int sign = 1;
        if (t.charAt(0) == '+' || t.charAt(0) == '-') {
            sign = t.charAt(0) == '-' ? -1 : 1;
            t = t.substring(1);
        }
        if (t.isEmpty()) {
            throw new NumberFormatException();
        }

        int value = Integer.parseInt(t, 16);
        return sign * value;
    }

    private static int indexOfS(String t) {
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            if (c == 's' || c == 'S') {
                return i;
            }
        }
        return -1;
    }

    private static final class ParseResult {
        final BigDecimal sum;
        final boolean hasNumber;

        ParseResult(BigDecimal sum, boolean hasNumber) {
            this.sum = sum;
            this.hasNumber = hasNumber;
        }
    }
}
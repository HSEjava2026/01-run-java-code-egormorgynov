package sum;

public final class SumDoubleHex {

    private SumDoubleHex() {
    }

    public static void main(String[] args) {
        double sum = 0.0;

        for (String argument : args) {
            sum += sumNumbersInsideOneArgument(argument);
        }

        System.out.println(sum);
    }

    private static double sumNumbersInsideOneArgument(String text) {
        if (text == null || text.isBlank()) {
            return 0.0;
        }

        double localSum = 0.0;
        int i = 0;
        int n = text.length();

        while (i < n) {
            while (i < n && Character.isWhitespace(text.charAt(i))) {
                i++;
            }
            if (i >= n) break;

            int start = i;
            while (i < n && !Character.isWhitespace(text.charAt(i))) {
                i++;
            }

            String token = text.substring(start, i);

            Double value = parseNumberToken(token);
            if (value != null) {
                localSum += value;
            }
        }

        return localSum;
    }

    private static Double parseNumberToken(String token) {
        if (token == null) return null;
        token = token.trim();
        if (token.isEmpty()) return null;

        int sign = 1;
        char first = token.charAt(0);
        if (first == '+' || first == '-') {
            sign = (first == '-') ? -1 : 1;
            token = token.substring(1);
            if (token.isEmpty()) return null;
        }

        if (token.length() >= 2 && (token.startsWith("0x") || token.startsWith("0X"))) {
            Double v = parseHexFloat(token);
            return v == null ? null : sign * v;
        }

        try {
            return sign * Double.parseDouble(token);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    private static Double parseHexFloat(String token) {
        String s = token.substring(2);
        if (s.isEmpty()) return null;

        int pPos = indexOfP(s);
        String mantissaStr;
        int exp2 = 0;

        if (pPos >= 0) {
            mantissaStr = s.substring(0, pPos);
            String expStr = s.substring(pPos + 1);
            if (expStr.isEmpty()) return null;
            try {
                exp2 = Integer.parseInt(expStr);
            } catch (NumberFormatException e) {
                return null;
            }
        } else {
            mantissaStr = s;
        }

        if (mantissaStr.isEmpty()) return null;

        int dot = mantissaStr.indexOf('.');
        String intPartStr = (dot >= 0) ? mantissaStr.substring(0, dot) : mantissaStr;
        String fracPartStr = (dot >= 0) ? mantissaStr.substring(dot + 1) : "";

        double intPart = 0.0;
        if (!intPartStr.isEmpty()) {
            Long v = parseHexUnsignedToLong(intPartStr);
            if (v == null) return null;
            intPart = v.doubleValue();
        }

        double fracPart = 0.0;
        if (!fracPartStr.isEmpty()) {
            double denom = 16.0;
            for (int i = 0; i < fracPartStr.length(); i++) {
                int d = hexDigit(fracPartStr.charAt(i));
                if (d < 0) return null;
                fracPart += d / denom;
                denom *= 16.0;
            }
        }

        double mantissa = intPart + fracPart;

        // применяем 2^exp2
        double result = mantissa * Math.scalb(1.0, exp2); // 1.0 * 2^exp2
        return result;
    }

    private static int indexOfP(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == 'p' || c == 'P') return i;
        }
        return -1;
    }

    private static Long parseHexUnsignedToLong(String s) {
        if (s.isEmpty()) return null;
        long val = 0L;
        for (int i = 0; i < s.length(); i++) {
            int d = hexDigit(s.charAt(i));
            if (d < 0) return null;
            val = (val << 4) + d;
        }
        return val;
    }

    private static int hexDigit(char c) {
        if (c >= '0' && c <= '9') return c - '0';
        if (c >= 'a' && c <= 'f') return 10 + (c - 'a');
        if (c >= 'A' && c <= 'F') return 10 + (c - 'A');
        return -1;
    }
}
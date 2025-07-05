import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {

    public static float calculateAndRound(float a, float b, Operation op) {
        BigDecimal bdA = new BigDecimal(Float.toString(a));
        BigDecimal bdB = new BigDecimal(Float.toString(b));
        BigDecimal result = switch (op) {
            case ADD -> bdA.add(bdB);
            case SUBTRACT -> bdA.subtract(bdB);
            case MULTIPLY -> bdA.multiply(bdB);
            case DIVIDE -> {
                if (bdB.compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                yield bdA.divide(bdB, 10, RoundingMode.HALF_UP);
            }
            default -> throw new IllegalArgumentException("Unsupported operation");
        };

        return result.setScale(2, RoundingMode.HALF_UP).floatValue();
    }

    public enum Operation {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE
    }
}

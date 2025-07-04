import java.math.BigDecimal;
import java.math.RoundingMode;

public class Utils {

    public enum Operation {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE
    }

    public static float calculateAndRound(float a, float b, Operation op) {
        BigDecimal bdA = new BigDecimal(Float.toString(a));
        BigDecimal bdB = new BigDecimal(Float.toString(b));
        BigDecimal result;

        switch (op) {
            case ADD:
                result = bdA.add(bdB);
                break;
            case SUBTRACT:
                result = bdA.subtract(bdB);
                break;
            case MULTIPLY:
                result = bdA.multiply(bdB);
                break;
            case DIVIDE:
                if (bdB.compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result = bdA.divide(bdB, 10, RoundingMode.HALF_UP); // scale 10 for intermediate precision
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation");
        }

        return result.setScale(2, RoundingMode.HALF_UP).floatValue();
    }
}

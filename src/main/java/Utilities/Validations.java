package Utilities;

import errors.InvalidDataException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Validations {
    public static float assignNonNegative(float number, String attributeName) throws InvalidDataException {
        if (number >= 0) {
            return number;
        }
        throw new InvalidDataException(attributeName + " can't be negative");
    }

    public static int assignNonNegative(int number, String attributeName) throws InvalidDataException {
        if (number >= 0) {
            return number;
        }
        throw new InvalidDataException(attributeName + " can't be negative");
    }

    public static float assignPositive(float number, String attributeName) throws InvalidDataException {
        if (number > 0) {
            return number;
        }
        throw new InvalidDataException(attributeName + " can't be negative");
    }

    public static int assignPositive(int number, String attributeName) throws InvalidDataException {
        if (number > 0) {
            return number;
        }
        throw new InvalidDataException(attributeName + " can't be negative");
    }

    public static String assignNonEmptyString(String str) throws InvalidDataException {
        if (str != null && !str.isEmpty()) {
            return str;
        }
        throw new InvalidDataException("Name can't be empty");
    }

    public static Date validateNotPastDate(Date date, String attributeName) throws InvalidDataException {
        if (date == null) {
            throw new InvalidDataException(attributeName + " can't be null");
        }
        LocalDate today = LocalDate.now();
        Date todayDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        if (date.before(todayDate)) {
            throw new InvalidDataException(attributeName + " should not be a past date");
        }
        return date;
    }
}

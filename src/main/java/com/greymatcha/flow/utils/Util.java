package com.greymatcha.flow.utils;

import com.greymatcha.flow.Main;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.time.*;
import java.util.Arrays;
import java.util.Objects;

import static com.greymatcha.flow.utils.Constant.EMPTY_STRING;
import static java.time.temporal.ChronoUnit.DAYS;

public class Util {

    private static final String resourcePath =  "/com/greymatcha/flow/";

    public static URL getFXML(String fileName) {
        return Main.class.getResource(resourcePath + "fxml/" + fileName + ".fxml");
    }

    public static String getImage(String fileName) {
        return Objects.requireNonNull(Main.class.getResource(resourcePath + "media/" + fileName)).toString();
    }

    public static String getJSON(String fileName) {
        return Objects.requireNonNull(Main.class.getResource(resourcePath + "json/" + fileName)).toString();
    }

    public static boolean compareNoOrder(@NotNull String string1, @NotNull String string2) {
        int string1Sum = sum(string1);
        int string2Sum = sum(string2);
        return string1Sum == string2Sum;
    }

    public static String toProperCase(Object object) {
        if (object == null) return EMPTY_STRING;

//        System.out.printf("Before processing: %s\n", object);
        String string = object.toString();

        String firstLetter = string.substring(0, 1).toUpperCase();
        String restOfTheString = string.substring(1).toLowerCase();

//        System.out.printf("After processing: %s\n", firstLetter + restOfTheString);

        return firstLetter + restOfTheString;
    }

    public static boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.printf("[%s %s] Util.isNumber() exception: %s\n", LocalDate.now(), LocalTime.now(), e.getMessage());
            return false;
        }
    }

    public static String formatDeadline(ZonedDateTime deadline) {

        if (deadline == null) return EMPTY_STRING;
        int gapInDays = (int) (ZonedDateTime.now().until(deadline, DAYS));

        if (gapInDays == 0) {
            System.out.println(gapInDays);
            return "Today";
        }

        if (gapInDays == 1)
            return "Tomorrow";

        if (gapInDays < 7)
            return Util.toProperCase(deadline.getDayOfWeek());

        return String.format(
                "%s %s %s",
                Util.toCompactMonth(deadline.getMonth()),
                deadline.getDayOfMonth(),
                deadline.getYear()
        );
    }

    public static String extractString(String string, String subString) {
        int indexOfSubstring = string.indexOf(subString);

        if (indexOfSubstring == -1)
            return EMPTY_STRING;

        return string.substring(0, indexOfSubstring) +
               string.substring(indexOfSubstring + subString.length());
    }

    public static String toCompactMonth(Object month) {
        try {
            StringBuilder monthBuilder = new StringBuilder(month.toString());
            monthBuilder.delete(3, month.toString().length());

            return toProperCase(monthBuilder.toString());
        } catch (RuntimeException e) {
//            System.out.println("Util.toCompact() error: " + e.getMessage());
            return null;
        }
    }

    public static boolean isBetween(int number, int start, int end, boolean isInclusive) {
        if (isInclusive)
            return number >= start && number <= end;

        return number > start && number < end;
    }

    public static int parseMonthToInt(Month month) {
        return switch (month) {
            case JANUARY -> 1;
            case FEBRUARY -> 2;
            case MARCH -> 3;
            case APRIL -> 4;
            case MAY -> 5;
            case JUNE -> 6;
            case JULY -> 7;
            case AUGUST -> 8;
            case SEPTEMBER -> 9;
            case OCTOBER -> 10;
            case NOVEMBER -> 11;
            case DECEMBER -> 12;
            default -> throw new IllegalArgumentException("Invalid month: " + month);
        };

    }

    public static int sum(String string) {
        int total = 0;
        for (int i = 0; i < string.length(); ++i)
            total += string.charAt(i);
        return total;
    }

    public static String sort(String word) {
        char[] wordCharArray = word.toCharArray();
        Arrays.sort(wordCharArray);
        return new String(wordCharArray);
    }
}
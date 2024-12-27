package com.greymatcha.flow.models.smartdate;

import static com.greymatcha.flow.utils.Constant.*;
import com.greymatcha.flow.utils.Util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static java.time.DayOfWeek.*;
import static java.time.Month.*;

public class DateIdentifier {

    private static HashSet<String> TIME_WORDS;
    private static String latestExtractedString;
    private static String latestModifiedString;
    private static boolean isMoreThanOneWord;

    public static void initialize() {
        setTimeWords();
    }

    private static void setTimeWords() {
        String[] timeWordsArray = {
                "monday", "tuesday", "wednesday", "thursday", "friday",
                "saturday", "sunday", "today", "tomorrow", "yesterday",
                "now", "again", "january", "jan", "february", "feb",
                "march", "mar", "april", "apr", "may", "june", "jun",
                "july", "jul", "august", "aug", "september", "sep",
                "october", "oct", "november", "nov", "december", "dec"
        };
        TIME_WORDS = new HashSet<>();
        List.of(timeWordsArray).forEach(
            word -> TIME_WORDS.add(
                Util.sort(word)
            )
        );
    }

    public static ZonedDateTime extractDate(String string) {
        String[] tokens = tokenizeString(string);
        ZonedDateTime extractedDate = null;
        String extractedWord = EMPTY_STRING;
        StringBuilder builder = new StringBuilder(EMPTY_STRING);
        isMoreThanOneWord = false;

        for (int i = 0; i < tokens.length; ++i) {
            String sortedToken = Util.sort(tokens[i].toLowerCase());
            if (TIME_WORDS.contains(sortedToken)) {
                extractedDate = getMatchingDate(tokens, i);
                extractedWord = tokens[i];
                continue;
            }
            builder.append(tokens[i]).append(" ");
        }

        latestModifiedString = builder.toString().trim();

        if (extractedDate == null) return null;


        if (!extractedWord.equals(latestExtractedString)) {
            System.out.println("Extracted date: " + extractedDate);
            if (!isMoreThanOneWord)
                latestExtractedString = extractedWord;
        }

        return extractedDate;
    }

    public static String[] tokenizeString(String string) {
        return string.trim().split(REGEX_WHITESPACE_NONALNUM);
    }

    public static String getLatestExtractedString() {
        return latestExtractedString;
    }

    public static String getLatestModifiedString() {
        return latestModifiedString;
    }

    public static ZonedDateTime getMatchingDate(String[] tokenizedString, int indexOfCurrentWord) {
        ZonedDateTime matchingDate;
        LocalDate currentDate = LocalDate.now();

        try {
            int daysToAdd = getDaysToAdd(
                    tokenizedString,
                    indexOfCurrentWord
            );

            matchingDate = ZonedDateTime.of(
                    currentDate.plusDays(daysToAdd),
                    END_OF_DAY,
                    USER_ZONEID
            );
        } catch (IllegalArgumentException e) {
           matchingDate = getdfngDate(tokenizedString, indexOfCurrentWord);
        }

        return matchingDate;
    }

    public static int getDaysToAdd(String[] tokenizedString, int indexOfCurrentWord) {
        String currentWord = tokenizedString[indexOfCurrentWord].toLowerCase();

        if (Util.compareNoOrder(currentWord, "today"))
            return 0;

        else if (Util.compareNoOrder(currentWord, "tomorrow"))
            return 1;

        else if (Util.compareNoOrder(currentWord, "yesterday"))
            return -1;

        else if (Util.compareNoOrder(currentWord, "monday"))
            return daysUntilNextDayOfWeek(MONDAY);

        else if (Util.compareNoOrder(currentWord, "tuesday"))
            return daysUntilNextDayOfWeek(TUESDAY);

        else if (Util.compareNoOrder(currentWord, "wednesday"))
            return daysUntilNextDayOfWeek(WEDNESDAY);

        else if (Util.compareNoOrder(currentWord, "thursday"))
            return daysUntilNextDayOfWeek(THURSDAY);

        else if (Util.compareNoOrder(currentWord, "friday"))
            return daysUntilNextDayOfWeek(FRIDAY);

        else if (Util.compareNoOrder(currentWord, "saturday"))
            return daysUntilNextDayOfWeek(SATURDAY);

        else if (Util.compareNoOrder(currentWord, "sunday"))
            return daysUntilNextDayOfWeek(SUNDAY);

        throw new IllegalArgumentException();
    }

    public static ZonedDateTime getdfngDate(String[] tokenizedString, int indexOfCurrentWord) {

        String currentWord = tokenizedString[indexOfCurrentWord].toLowerCase();

        if (Util.compareNoOrder(currentWord, "january") ||
                Util.compareNoOrder(currentWord, "jan")) {
            return getDate(tokenizedString, indexOfCurrentWord, JANUARY);
        }

        else if (Util.compareNoOrder(currentWord, "february") ||
                Util.compareNoOrder(currentWord, "feb")) {
            return getDate(tokenizedString, indexOfCurrentWord, FEBRUARY);
        }

        else if (Util.compareNoOrder(currentWord, "march") ||
                Util.compareNoOrder(currentWord, "mar")) {
            return getDate(tokenizedString, indexOfCurrentWord, MARCH);
        }

        else if (Util.compareNoOrder(currentWord, "april") ||
                Util.compareNoOrder(currentWord, "apr")) {
            return getDate(tokenizedString, indexOfCurrentWord, APRIL);
        }

        else if (Util.compareNoOrder(currentWord, "may")) {
            return getDate(tokenizedString, indexOfCurrentWord, MAY);
        }

        else if (Util.compareNoOrder(currentWord, "june") ||
                Util.compareNoOrder(currentWord, "jun")) {
            return getDate(tokenizedString, indexOfCurrentWord, JUNE);
        }

        else if (Util.compareNoOrder(currentWord, "july") ||
                Util.compareNoOrder(currentWord, "jul")) {
            return getDate(tokenizedString, indexOfCurrentWord, JULY);
        }

        else if (Util.compareNoOrder(currentWord, "august") ||
                Util.compareNoOrder(currentWord, "aug")) {
            return getDate(tokenizedString, indexOfCurrentWord, AUGUST);
        }

        else if (Util.compareNoOrder(currentWord, "september") ||
                Util.compareNoOrder(currentWord, "sep")) {
            return getDate(tokenizedString, indexOfCurrentWord, SEPTEMBER);
        }

        else if (Util.compareNoOrder(currentWord, "october") ||
                Util.compareNoOrder(currentWord, "oct")) {
            return getDate(tokenizedString, indexOfCurrentWord, OCTOBER);
        }

        else if (Util.compareNoOrder(currentWord, "november") ||
                Util.compareNoOrder(currentWord, "nov")) {
            return getDate(tokenizedString, indexOfCurrentWord, NOVEMBER);
        }

        else if (Util.compareNoOrder(currentWord, "december") ||
                Util.compareNoOrder(currentWord, "dec")) {
            return getDate(tokenizedString, indexOfCurrentWord, DECEMBER);
        }

        throw new IllegalArgumentException();
    }

    public static ZonedDateTime getDate(String[] tokenizedString, int index, Month yearMonth) {
        ZonedDateTime dayToday = ZonedDateTime.now();
        String compactMonth = Util.toCompactMonth(tokenizedString[index]);

        assert compactMonth != null;
        StringBuilder possibleDate = new StringBuilder(compactMonth);
        StringBuilder newLatestExtractedString = new StringBuilder(tokenizedString[index]);

        if (index + 1 < tokenizedString.length && isValidDayMonth(tokenizedString[index + 1])) {
            String nextToken = tokenizedString[index + 1];
            possibleDate.append("-").append(nextToken);
            newLatestExtractedString.append(" ").append(nextToken);

            if (index + 2 < tokenizedString.length && Util.isNumber(tokenizedString[index + 2])) {
                String nextNextToken = tokenizedString[index + 2];
                possibleDate.append("-").append(nextNextToken);
                newLatestExtractedString.append(" ").append(nextNextToken);
            } else {
                int yearToAppend = (dayToday.getMonth() == DECEMBER)
                    ? dayToday.getYear() + 1
                    : dayToday.getYear();

                possibleDate.append("-").append(yearToAppend);
            }

        } else {
            int toMonth = yearMonth.getValue();
            int fromMonth = dayToday.getMonthValue();

            int nextMonth = (toMonth + 1 > 12)
                ? 1
                : toMonth + 1;

            int year = (fromMonth == 12 || toMonth < fromMonth)
                ? dayToday.getYear() + 1
                : dayToday.getYear();

            ZonedDateTime nextDateTime = ZonedDateTime.of(
                    year,
                    nextMonth,
                    1,
                    0,
                    0,
                    0,
                    0,
                    USER_ZONEID
            );

            return nextDateTime.plusDays(-1);
        }

        try {

            LocalDate tempUntilDate = LocalDate.parse(possibleDate, DateTimeFormatter.ofPattern("MMM-d-u"));
            ZonedDateTime untilDate = ZonedDateTime.of(tempUntilDate, END_OF_DAY, USER_ZONEID);

            latestExtractedString = newLatestExtractedString.toString();
            isMoreThanOneWord = true;
            return untilDate;
        } catch (DateTimeParseException e) {
            latestModifiedString = EMPTY_STRING;
            latestExtractedString = EMPTY_STRING;
            System.out.println("possibleDate in DateIdentifier.getDaysUntilDate didn't parse correctly.\npossibleDate value: " + possibleDate.toString().trim());
            System.out.println(e.getMessage());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private static boolean isValidDayMonth(String string) {
        try {
            int monthDay = Integer.parseInt(string);
            return monthDay >= 1 && monthDay <= 31;
        } catch (NumberFormatException e) {
           return false;
        }
    }

    public static int daysUntilNextDayOfWeek(DayOfWeek dayOfWeek) {
        ZonedDateTime dayToday = ZonedDateTime.now();
        int addedDays = 0;

        while (dayToday.plusDays(addedDays).getDayOfWeek() != dayOfWeek) {
            addedDays++;
        }

        return addedDays;
    }
}
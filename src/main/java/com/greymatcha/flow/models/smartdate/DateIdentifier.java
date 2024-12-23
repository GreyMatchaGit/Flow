package com.greymatcha.flow.models.smartdate;

import static com.greymatcha.flow.utils.Constant.*;
import com.greymatcha.flow.utils.Util;

import java.time.*;
import java.util.*;

import static java.time.DayOfWeek.*;

public class DateIdentifier {

    private static HashSet<String> TIME_WORDS;
    private static String latestExtractedWord;
    private static String latestModifiedString;

    public static void initialize() {
        setTimeWords();
    }

    private static void setTimeWords() {
        String[] temp = {
            "monday", "tuesday", "wednesday", "thursday", "friday",
            "saturday", "sunday", "today", "tomorrow", "yesterday",
            "now", "again"
        };
        TIME_WORDS = new HashSet<>();
        List.of(temp).forEach(
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

        for (String token : tokens) {
            String sortedToken = Util.sort(token.toLowerCase());
            if (TIME_WORDS.contains(sortedToken)) {
                extractedDate = getMatchingDate(sortedToken);
                extractedWord = token;
                continue;
            }
            builder.append(token).append(" ");
        }

        if (extractedDate == null) return null;

        if (!extractedWord.equals(latestExtractedWord)) {
            System.out.println("Extracted date: " + extractedDate);
            latestExtractedWord = extractedWord;
        }

        latestModifiedString = builder.toString().trim();

        return extractedDate;
    }

    public static String[] tokenizeString(String string) {
        return string.trim().split(REGEX_WHITESPACE_NONALNUM);
    }

    public static String getLatestExtractedWord() {
        return latestExtractedWord;
    }

    public static String getLatestModifiedString() {
        return latestModifiedString;
    }

    public static ZonedDateTime getMatchingDate(String word) {
        ZonedDateTime matchingDate = null;
        LocalDate currentDate = LocalDate.now();

        int daysToAdd = getDaysToAdd(word);

        matchingDate = ZonedDateTime.of(
                currentDate.plusDays(daysToAdd),
                END_OF_DAY,
                USER_ZONEID
        );

        return matchingDate;
    }

    public static int getDaysToAdd(String word) {
        if (Util.compareNoOrder(word, "today")) {
            return 0;
        } else if (Util.compareNoOrder(word, "tomorrow")) {
            return 1;
        } else if (Util.compareNoOrder(word, "yesterday")) {
            return -1;
        } else if (Util.compareNoOrder(word, "monday")) {
            return daysUntilNextDayOfWeek(MONDAY);
        } else if (Util.compareNoOrder(word, "tuesday")) {
            return daysUntilNextDayOfWeek(TUESDAY);
        } else if (Util.compareNoOrder(word, "wednesday")) {
            return daysUntilNextDayOfWeek(WEDNESDAY);
        } else if (Util.compareNoOrder(word, "thursday")) {
            return daysUntilNextDayOfWeek(THURSDAY);
        } else if (Util.compareNoOrder(word, "friday")) {
            return daysUntilNextDayOfWeek(FRIDAY);
        } else if (Util.compareNoOrder(word, "saturday")) {
            return daysUntilNextDayOfWeek(SATURDAY);
        } else if (Util.compareNoOrder(word, "sunday")) {
            return daysUntilNextDayOfWeek(SUNDAY);
        }

        return -1;
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
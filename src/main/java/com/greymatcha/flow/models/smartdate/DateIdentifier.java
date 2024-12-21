package com.greymatcha.flow.models.smartdate;

import com.greymatcha.flow.utils.Util;

import java.time.*;
import java.util.*;

public class DateIdentifier {

    private static HashSet<String> TIME_WORDS;
    private static final String REGEX_WHITESPACE = "\\s+";
    private static String USER_TIMEZONE = "Asia/Manila";
    private static final ZoneId USER_ZONEID = ZoneId.of(USER_TIMEZONE);
    private static final LocalTime END_OF_DAY = LocalTime.of(23, 59, 59, 999_999_999);

    private static String latestExtractedWord;

    public static void initialize() {
        USER_TIMEZONE = "Asia/Manila";
        setTimeWords();
    }

    private static void setTimeWords() {
        String[] temp = {"today", "tomorrow", "yesterday", "now", "again"};
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
        String extractedWord = "";

        for (String token : tokens) {
            String sortedToken = Util.sort(token);
            if (TIME_WORDS.contains(sortedToken)) {
                extractedDate = getMatchingDate(sortedToken);
                extractedWord = token;
                break;
            }
        }

        if (extractedDate == null) return null;

        if (!extractedWord.equals(latestExtractedWord)) {
            System.out.println("Extracted date: " + extractedDate);
            latestExtractedWord = extractedWord;
        }

        return extractedDate;
    }

    public static String[] tokenizeString(String string) {
        return string.toLowerCase().trim().split(REGEX_WHITESPACE);
    }

    public static String getLatestExtractedWord() {
        return latestExtractedWord;
    }

    public static ZonedDateTime getMatchingDate(String word) {
        ZonedDateTime matchingDate = null;
        LocalDate currentDate = LocalDate.now();

        if (Util.compareNoOrder(word, "today")) {
            matchingDate = ZonedDateTime.of(
                    currentDate,
                    END_OF_DAY,
                    USER_ZONEID
            );
        } else if (Util.compareNoOrder(word, "tomorrow"))  {
            matchingDate = ZonedDateTime.of(
                    currentDate.plusDays(1),
                    END_OF_DAY,
                    USER_ZONEID
            );
        }

        return matchingDate;
    }
}

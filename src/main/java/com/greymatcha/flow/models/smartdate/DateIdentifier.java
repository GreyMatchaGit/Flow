package com.greymatcha.flow.models.smartdate;

import com.greymatcha.flow.utils.StringUtil;

import java.time.*;
import java.util.*;

public class DateIdentifier {

    private static HashSet<String> TIME_WORDS;
    private static final String REGEX_WHITESPACE = "\\s+";
    private static String USER_TIMEZONE = "Asia/Manila";
    private static final ZoneId USER_ZONEID = ZoneId.of(USER_TIMEZONE);
    private static final LocalTime END_OF_DAY = LocalTime.of(23, 59, 59, 999_999_999);

    private static String latestExtractedWord;

    public static void intialize() {
        USER_TIMEZONE = "Asia/Manila";
        setTimeWords();
    }

    public static void setTimeWords() {
        String[] temp = {"today", "tomorrow", "yesterday", "now", "again"};
        TIME_WORDS = new HashSet<>();
        List.of(temp).forEach(word -> TIME_WORDS.add(StringUtil.sort(word)));
    }

    public static ZonedDateTime extractDate(String string) {
        String[] tokens = string.toLowerCase().trim().split(REGEX_WHITESPACE);
        StringBuilder stringBuilder = new StringBuilder(string);
        ZonedDateTime extractedDate = null;
        boolean foundTimeWord = false;
        String extractedWord = "";

        for (String token : tokens) {
            String sortedToken = StringUtil.sort(token);

            if (!foundTimeWord && TIME_WORDS.contains(sortedToken)) {
                extractedDate = getMatchingDate(sortedToken);
                extractedWord = token;
                foundTimeWord = true;
                continue;
            }

            stringBuilder.append(token).append(" ");
        }

        if (extractedDate != null) {
            string = stringBuilder.toString();
            if (!extractedWord.equals(latestExtractedWord)) {
                System.out.println("Extracted date: " + extractedDate);
                latestExtractedWord = extractedWord;
            }
            return extractedDate;
        }

        return null;
    }

    public static String getLatestExtractedWord() {
        return latestExtractedWord;
    }

    public static ZonedDateTime getMatchingDate(String word) {
        ZonedDateTime matchingDate = null;
        LocalDate currentDate = LocalDate.now();

        if (StringUtil.compareNoOrder(word, "today")) {
            matchingDate = ZonedDateTime.of(
                    currentDate,
                    END_OF_DAY,
                    USER_ZONEID
            );
        } else if (StringUtil.compareNoOrder(word, "tomorrow"))  {
            matchingDate = ZonedDateTime.of(
                    currentDate.plusDays(1),
                    END_OF_DAY,
                    USER_ZONEID
            );
        }

        return matchingDate;
    }

    private static ArrayList<String> extractWordsWithTime(String[] words) {
        ArrayList<String> wordsWithTime = new ArrayList<>();
        return wordsWithTime;
    }
}

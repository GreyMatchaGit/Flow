package com.greymatcha.flow.models.smartdate;

import java.util.*;

public class DateIdentifier {

    public static HashSet<String> TIME_WORDS;

    private DateIdentifier() {

    }

    public GregorianCalendar extractDate(String stream) {
        String[] tokens = stream.toLowerCase().trim().split("\\s+");
        ArrayList<String> wordsWithTime = extractWordsWithTime(tokens);
        return new GregorianCalendar();
    }

    private ArrayList<String> extractWordsWithTime(String[] words) {
        ArrayList<String> wordsWithTime = new ArrayList<>();
        return wordsWithTime;
    }
}

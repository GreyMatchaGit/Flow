package com.greymatcha.flow.utils;

import com.greymatcha.flow.Main;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Arrays;

public class StringUtil {

    private static String resourcePath =  "/com/greymatcha/flow/";

    public static URL getFXML(String fileName) {
        return Main.class.getResource(resourcePath + "fxml/" + fileName + ".fxml");
    }

    public static String getImage(String fileName) {
        return Main.class.getResource(resourcePath + "media/" + fileName).toString();
    }

    public static String getJSON(String fileName) {
        return Main.class.getResource(resourcePath + "json/" + fileName).toString();
    }

    public static boolean compareNoOrder(@NotNull String string1, @NotNull String string2) {
        String sortedString1 = sort(string1);
        String sortedString2 = sort(string2);
        return sortedString1.equals(sortedString2);
    }

    public static String sort(String word) {
        char[] wordCharArray = word.toCharArray();
        Arrays.sort(wordCharArray);
        return new String(wordCharArray);
    }
}
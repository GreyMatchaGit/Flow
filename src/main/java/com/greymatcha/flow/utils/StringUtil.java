package com.greymatcha.flow.utils;

import com.greymatcha.flow.Main;

import java.net.URL;

public class StringUtil {

    private static String resourcePath =  "/com/greymatcha/flow/";

    public static URL getFXML(String fileName) {
        return Main.class.getResource(resourcePath + "fxml/" + fileName + ".fxml");
    }

    public static String getImage(String fileName) {
        return Main.class.getResource(resourcePath + "media/" + fileName).toString();
    }
}
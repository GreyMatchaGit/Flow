package com.greymatcha.flow.utils;

import com.greymatcha.flow.Main;

import java.net.URL;

public class StringUtil {

    public static URL getFXML(String fileName) {

        String precedingPath = "/com/greymatcha/flow/fxml/";
        return Main.class.getResource(precedingPath + fileName + ".fxml");
    }
}
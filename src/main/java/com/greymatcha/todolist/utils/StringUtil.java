package com.greymatcha.todolist.utils;

import com.greymatcha.todolist.TodoList;

import java.net.URL;

public class StringUtil {

    public static URL getFXML(String fileName) {

        String precedingPath = "/com/greymatcha/todolist/fxml/";
        return TodoList.class.getResource(precedingPath + fileName + ".fxml");
    }
}

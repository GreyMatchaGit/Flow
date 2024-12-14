package com.greymatcha.todolist;

import com.greymatcha.todolist.utils.StringUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TodoList extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(StringUtil.getFXML("inbox-page"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("TodoList");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
package com.greymatcha.flow;

import com.greymatcha.flow.models.smartdate.DateIdentifier;
import com.greymatcha.flow.utils.Util;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {


        DateIdentifier.initialize();

        FXMLLoader fxmlLoader = new FXMLLoader(Util.getFXML("todolist-page"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Flow");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
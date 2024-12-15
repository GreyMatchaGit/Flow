package com.greymatcha.todolist.fxmlcontroller;

import com.greymatcha.todolist.utils.CustomAnimation;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class InboxPageController implements Initializable {


    @FXML
    Button addTaskButton;

    @FXML
    Circle taskPaneCloseButton;

    @FXML
    Pane taskPane, addTaskParentPane, contentPane;

    @FXML
    TextField taskNameField, taskDescriptionField;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        taskPane.setScaleX(0);
        taskPane.setScaleY(0);

        taskNameField.setBackground(null);
        taskDescriptionField.setBackground(null);

        CustomAnimation.buttonClickEffect(addTaskButton, addTaskParentPane);

        addTaskButton.setOnMouseClicked(_ -> {

            contentPane.setEffect(new GaussianBlur());
            taskNameField.clear();
            taskDescriptionField.clear();
            CustomAnimation.createScaleTransition(taskPane, 1.0, Duration.millis(100)).play();
        });

        taskPaneCloseButton.setOnMouseClicked(_ -> {

            contentPane.setEffect(null);
            CustomAnimation.createScaleTransition(taskPane, 0.0, Duration.millis(100)).play();
        });

        taskPaneCloseButton.setCursor(Cursor.HAND);
    }
}
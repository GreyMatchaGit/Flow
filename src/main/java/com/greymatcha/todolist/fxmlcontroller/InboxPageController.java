package com.greymatcha.todolist.fxmlcontroller;

import com.greymatcha.todolist.utils.CustomAnimation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class InboxPageController implements Initializable {


    @FXML
    Button addTaskButton;

    @FXML
    Pane addTaskParentPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CustomAnimation.buttonClickEffect(addTaskButton, addTaskParentPane);
    }
}
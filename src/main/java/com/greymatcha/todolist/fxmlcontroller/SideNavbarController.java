package com.greymatcha.todolist.fxmlcontroller;

import com.greymatcha.todolist.utils.CustomAnimation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class SideNavbarController implements Initializable {

    @FXML
    Button inboxButton, todayButton, userButton;

    @FXML
    Pane userPane;

    @FXML
    Rectangle inboxBackground, todayBackground, userBackground;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CustomAnimation.buttonHoverEffect(inboxButton, inboxBackground);
        CustomAnimation.buttonHoverEffect(todayButton, todayBackground);
        CustomAnimation.buttonHoverEffect(userButton, userBackground);
        CustomAnimation.buttonClickEffect(userButton, userPane);
    }
}

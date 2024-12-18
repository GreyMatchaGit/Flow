package com.greymatcha.flow.fxmlcontroller;

import com.greymatcha.flow.utils.CustomAnimation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class SideNavbarController implements Initializable {

    @FXML
    Button inboxButton, todayButton, userButton;

    @FXML
    Pane userPane, userStackPane;

    @FXML
    Text username;

    @FXML
    HBox userContainer;

    @FXML
    Rectangle inboxBackground, todayBackground, userBackground;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        username.setText("GreyMatcha");

        CustomAnimation.buttonHoverEffect(inboxButton, inboxBackground);
        CustomAnimation.buttonHoverEffect(todayButton, todayBackground);
        CustomAnimation.buttonHoverEffect(userPane, userBackground);
        CustomAnimation.buttonClickEffect(userButton, userPane);

        Platform.runLater(() -> {
            userBackground.setWidth(userPane.getWidth() + 5);
            userStackPane.setPrefWidth(userPane.getWidth() + 5);
        });

        userButton.setOnMouseClicked(_ -> {
            System.out.println("userButton is pressed.");
        });
    }

}

package com.greymatcha.flow.fxmlcontrollers;

import com.greymatcha.flow.utils.MyAnimation;
import com.greymatcha.flow.utils.Util;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    ImageView userProfileImage;

    @FXML
    Rectangle inboxBackground, todayBackground, userBackground;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        username.setText("GreyMatcha");
        userProfileImage.setImage(new Image(Util.getImage("frieren.jpg")));

        MyAnimation.buttonHoverEffect(inboxButton, inboxBackground);
        MyAnimation.buttonHoverEffect(todayButton, todayBackground);
        MyAnimation.buttonHoverEffect(userPane, userBackground);
        MyAnimation.buttonClickEffect(userButton, userPane);

        Platform.runLater(() -> {
            userBackground.setWidth(userPane.getWidth() + 5);
            userStackPane.setPrefWidth(userPane.getWidth() + 5);
            userButton.setPrefWidth(userPane.getWidth() + 5);
        });
    }

}

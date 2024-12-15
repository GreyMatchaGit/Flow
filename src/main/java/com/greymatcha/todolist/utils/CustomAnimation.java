package com.greymatcha.todolist.utils;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class CustomAnimation {

    public static void buttonHoverEffect(Button button, Rectangle background) {

        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(_ -> background.setFill(Color.web(ColorPalette.light)));
        button.setOnMouseExited(_ -> background.setFill(Color.web(ColorPalette.medium)));
    }

    public static void buttonClickEffect(Button button, Pane parentPane) {

        button.setCursor(Cursor.HAND);
        button.setOnMousePressed(_ -> createScaleTransition(parentPane, 0.95).play());
        button.setOnMouseReleased(_ -> createScaleTransition(parentPane, 1.0).play());
    }

    public static ScaleTransition createScaleTransition(Node node, Double scalingFactor) {

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(50), node);
        scaleTransition.setToX(scalingFactor);
        scaleTransition.setToY(scalingFactor);
        scaleTransition.setInterpolator(Interpolator.EASE_IN);

        return scaleTransition;
    }
}

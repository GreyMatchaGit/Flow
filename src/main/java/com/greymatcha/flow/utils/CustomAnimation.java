package com.greymatcha.flow.utils;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class CustomAnimation {

    public static void buttonHoverEffect(Node button, Rectangle background) {

        button.setOnMouseEntered(_ -> background.setFill(Color.web(Theme.light)));
        button.setOnMouseExited(_ -> background.setFill(Color.web(Theme.medium)));
    }

    public static void buttonClickEffect(Node button, Pane parentPane) {

        button.setOnMousePressed(_ -> createScaleTransition(parentPane, 0.95, Duration.millis(50)).play());
        button.setOnMouseReleased(_ -> createScaleTransition(parentPane, 1.0, Duration.millis(50)).play());
    }

    public static ScaleTransition createScaleTransition(Node node, Double scalingFactor, Duration duration) {

        ScaleTransition scaleTransition = new ScaleTransition(duration, node);
        scaleTransition.setToX(scalingFactor);
        scaleTransition.setToY(scalingFactor);
        scaleTransition.setInterpolator(Interpolator.EASE_IN);

        return scaleTransition;
    }
}

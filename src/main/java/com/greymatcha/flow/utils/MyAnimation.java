package com.greymatcha.flow.utils;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MyAnimation {

    public static final Duration CLICK_DURATION_MILLIS = Duration.millis(50);
    public static final double CLICK_SHRINK_FACTOR = 0.95;
    public static final double CLICK_DEFAULT_FACTOR = 1.0;
    public static final double CLICK_GROW_FACTOR = 1.05;

    public static void buttonHoverEffect(Node button, Rectangle background) {

        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(_ -> background.setFill(Color.web(Theme.light)));
        button.setOnMouseExited(_ -> background.setFill(Color.web(Theme.medium)));
    }

    public static void buttonClickEffect(Node button, Pane parentPane) {

        button.setCursor(Cursor.HAND);
        button.setOnMousePressed(_ -> createScaleTransition(parentPane, 0.95, Duration.millis(50)).play());
        button.setOnMouseReleased(_ -> createScaleTransition(parentPane, 1.0, Duration.millis(50)).play());
    }

    public static void shrinkButtonSize(Node parentPane) {
        createScaleTransition(parentPane, CLICK_SHRINK_FACTOR, CLICK_DURATION_MILLIS).play();
    }

    public static void resetButtonSize(Node parentPane) {
        createScaleTransition(parentPane, CLICK_DEFAULT_FACTOR, CLICK_DURATION_MILLIS).play();
    }

    public static ScaleTransition createScaleTransition(Node node, Double scalingFactor, Duration duration) {

        ScaleTransition scaleTransition = new ScaleTransition(duration, node);
        scaleTransition.setToX(scalingFactor);
        scaleTransition.setToY(scalingFactor);
        scaleTransition.setInterpolator(Interpolator.EASE_IN);

        return scaleTransition;
    }
}

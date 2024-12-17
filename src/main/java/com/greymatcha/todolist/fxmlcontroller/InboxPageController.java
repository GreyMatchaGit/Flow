package com.greymatcha.todolist.fxmlcontroller;

import com.greymatcha.todolist.models.Task;
import com.greymatcha.todolist.models.TaskBuilder;
import com.greymatcha.todolist.models.TaskList;
import com.greymatcha.todolist.utils.CustomAnimation;
import com.greymatcha.todolist.utils.Theme;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class InboxPageController implements Initializable {


    @FXML
    Button addTaskButton;

    @FXML
    Circle taskPaneCloseButton;

    @FXML
    Rectangle taskPanePrimaryButton;

    @FXML
    Pane taskPane, addTaskParentPane, contentPane, taskStackPane, taskPanePrimaryButtonParent;

    @FXML
    VBox taskBoxContainer;

    @FXML
    TextField taskNameField, taskDescriptionField;

    @FXML
    ScrollPane taskBoxScroll;

    TaskList taskList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        taskList = TaskList.getInstance();

        taskStackPane.setVisible(true); // temporary for frontend dev purposes

        setUpTaskPane();
        setUpAddTaskButton();
    }

    public void setUpTaskPane() {

        taskBoxScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        taskPane.setScaleX(0);
        taskPane.setScaleY(0);

        taskNameField.setBackground(null);
        taskDescriptionField.setBackground(null);

        taskPaneCloseButton.setCursor(Cursor.HAND);
        taskPaneCloseButton.setOnMouseClicked(_ -> closeTaskPane());

        CustomAnimation.buttonClickEffect(taskPanePrimaryButton, taskPanePrimaryButtonParent);
        taskPanePrimaryButton.setCursor(Cursor.HAND);
        taskPanePrimaryButton.setOnMouseClicked(_ -> {

            Task newTask = new TaskBuilder(UUID.randomUUID().toString())
                .setName(
                        (taskNameField.getText().isEmpty())
                            ? "New Task"
                            : taskNameField.getText()
                )
                .setDescription(taskDescriptionField.getText())
                .create();

            if (taskList.addTask(newTask)) {

                taskBoxContainer.getChildren().add(createTaskBox(newTask));
            }

            closeTaskPane();
        });
    }

    public void closeTaskPane() {

        contentPane.setEffect(null);
        ScaleTransition animation = CustomAnimation.createScaleTransition(taskPane, 0.0, Duration.millis(100));
        animation.setOnFinished(_ -> {
            taskStackPane.setVisible(false);
        });
        animation.play();
    }

    public void openTaskPane(int mode) {

    }

    public void setUpAddTaskButton() {

        CustomAnimation.buttonClickEffect(addTaskButton, addTaskParentPane);

        addTaskButton.setOnMouseClicked(_ -> {

            contentPane.setEffect(new GaussianBlur());
            taskNameField.clear();
            taskDescriptionField.clear();
            taskStackPane.setVisible(true);
            CustomAnimation.createScaleTransition(taskPane, 1.0, Duration.millis(100)).play();
        });
    }

    public void displayTaskBoxes() {

    }

    public AnchorPane createTaskBox(Task task) {

        AnchorPane container = new AnchorPane();
        container.setPrefHeight(51);
        container.setPrefWidth(taskBoxContainer.getWidth());

            Rectangle lineBreaker = new Rectangle();
            lineBreaker.setFill(Color.web("#495456"));
            lineBreaker.setHeight(container.getPrefHeight());
            lineBreaker.setWidth(container.getPrefWidth());

            Rectangle background = new Rectangle();
            background.setFill(Color.web(Theme.medium));
            background.setHeight(container.getPrefHeight() - 1);
            background.setWidth(container.getPrefWidth());

            Text taskName = new Text(task.getName());
            taskName.setFont(new Font("Product Sans", 24));
            taskName.setLayoutY(18);

            Text taskDescription = new Text(task.getDescription());
            taskName.setFont(new Font("Product Sans", 18));
            taskDescription.setLayoutY(36);

            Button editTask = new Button();
            editTask.setCursor(Cursor.HAND);
            editTask.setPrefWidth(container.getPrefWidth());
            editTask.setPrefHeight(container.getPrefHeight());
            editTask.setBackground(null);

            editTask.setOnMouseClicked(_ -> {
                System.out.println("Supposed to open the task pane here to edit the task. But will do that later.");
                System.out.println("Task UUID: " + task.getUniqueID());
            });

        container.getChildren().addAll(lineBreaker, background, taskName, taskDescription, editTask);

        return container;
    }
}
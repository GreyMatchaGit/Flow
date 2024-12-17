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


    @FXML Pane taskPane, addTaskParentPane, contentPane, taskStackPane, taskPaneAddButtonParent, taskPaneApplyButtonParent, taskPaneRemoveButtonParent;
    @FXML ScrollPane taskBoxScroll;
    @FXML VBox taskBoxContainer;
    @FXML TextField taskNameField, taskDescriptionField;
    @FXML Button addTaskButton, taskPaneApplyButton, taskPaneAddButton, taskPaneRemoveButton;
    @FXML Circle taskPaneCloseButton;

    TaskList taskList;

    private Pane selectedRoot;
    private Task selectedTask;
    private Text selectedTaskName;
    private Text selectedTaskDescription;

    enum Mode {
        ADD_TASK,
        EDIT_TASK
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        taskList = TaskList.getInstance();
        selectedTask = null;
        selectedTaskName = null;
        selectedTaskDescription = null;

        taskStackPane.setVisible(true); // temporary for frontend dev purposes

        setUpTaskPane();
        setUpAddTaskButton();
    }

    public void setUpAddTaskButton() {

        CustomAnimation.buttonClickEffect(addTaskButton, addTaskParentPane);
        addTaskButton.setCursor(Cursor.HAND);
        addTaskButton.setOnMouseClicked(_ -> openTaskPane(Mode.ADD_TASK));
    }

    public void setUpTaskPane() {

        taskBoxScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        taskPane.setScaleX(0);
        taskPane.setScaleY(0);

        taskNameField.setBackground(null);
        taskDescriptionField.setBackground(null);

        taskPaneCloseButton.setCursor(Cursor.HAND);
        taskPaneCloseButton.setOnMouseClicked(_ -> closeTaskPane());

        setupAddButton();
        setupApplyButton();
        setupRemoveButton();
    }

    public void setupAddButton() {

        CustomAnimation.buttonClickEffect(taskPaneAddButton, taskPaneAddButtonParent);
        taskPaneAddButton.setCursor(Cursor.HAND);

        taskPaneAddButton.setOnMouseClicked(_ -> {

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

    public void setupRemoveButton() {

        CustomAnimation.buttonClickEffect(taskPaneRemoveButton, taskPaneRemoveButtonParent);
        taskPaneRemoveButton.setCursor(Cursor.HAND);

        taskPaneRemoveButton.setOnMouseClicked(_ -> {

            if (!taskList.removeTask(selectedTask))
                throw new RuntimeException("Task doesn't exist in the list.");

            taskBoxContainer.getChildren().remove(selectedRoot);
            selectedTask = null;
            selectedRoot = null;
            selectedTaskName = null;
            selectedTaskDescription = null;

            closeTaskPane();
        });
    }

    public void setupApplyButton() {

        CustomAnimation.buttonClickEffect(taskPaneApplyButton, taskPaneApplyButtonParent);
        taskPaneApplyButton.setCursor(Cursor.HAND);

        taskPaneApplyButton.setOnMouseClicked(_ -> {

            selectedTask.setName(taskNameField.getText());
            selectedTask.setDescription(taskDescriptionField.getText());
            selectedTaskName.setText(taskNameField.getText());
            selectedTaskDescription.setText(taskDescriptionField.getText());

            closeTaskPane();
        });
    }

    public void closeTaskPane() {

        contentPane.setEffect(null);
        selectedTask = null;
        ScaleTransition animation = CustomAnimation.createScaleTransition(taskPane, 0.0, Duration.millis(100));
        animation.setOnFinished(_ -> {
            taskStackPane.setVisible(false);
        });
        animation.play();
    }

    public void openTaskPane(Mode mode, Task... task) {

        contentPane.setEffect(new GaussianBlur());
        taskStackPane.setVisible(true);
        CustomAnimation.createScaleTransition(taskPane, 1.0, Duration.millis(100)).play();

        if (mode.equals(Mode.ADD_TASK)) {
            taskNameField.clear();
            taskDescriptionField.clear();

            taskPaneApplyButtonParent.setVisible(false);
            taskPaneRemoveButtonParent.setVisible(false);
            taskPaneAddButtonParent.setVisible(true);
        } else {
            if (task == null)
                throw new IllegalArgumentException("Mode.EDIT_TASK must have a task passed.");

            taskPaneApplyButtonParent.setVisible(true);
            taskPaneRemoveButtonParent.setVisible(true);
            taskPaneAddButtonParent.setVisible(false);

            taskNameField.setText(task[0].getName());
            taskDescriptionField.setText(task[0].getDescription());
            selectedTask = task[0];
        }
    }

    public void displayTaskBoxes() {

    }

    public AnchorPane createTaskBox(Task task) {

        AnchorPane container = new AnchorPane();
        container.setPrefHeight(51);
        container.setPrefWidth(taskBoxContainer.getWidth());

            Rectangle lineBreaker = new Rectangle();
            lineBreaker.setFill(Color.web(Theme.dark));
            lineBreaker.setHeight(container.getPrefHeight());
            lineBreaker.setWidth(container.getPrefWidth());

            Rectangle background = new Rectangle();
            background.setFill(Color.web(Theme.medium));
            background.setHeight(container.getPrefHeight() - 1);
            background.setWidth(container.getPrefWidth());

            Text taskName = new Text(task.getName());
            taskName.setFont(new Font(Theme.FONT_FAMILY, 24));
            taskName.setLayoutY(18);

            Text taskDescription = new Text(task.getDescription());
            taskName.setFont(new Font(Theme.FONT_FAMILY, 18));
            taskDescription.setLayoutY(38);

            Button editTask = new Button();
            editTask.setCursor(Cursor.HAND);
            editTask.setPrefWidth(container.getPrefWidth());
            editTask.setPrefHeight(container.getPrefHeight());
            editTask.setBackground(null);

            editTask.setOnMouseClicked(_ -> {

                selectedRoot = container;
                selectedTaskName = taskName;
                selectedTaskDescription = taskDescription;
                openTaskPane(Mode.EDIT_TASK, task);
                System.out.println("Task UUID: " + task.getUniqueID());
            });

        container.getChildren().addAll(lineBreaker, background, taskName, taskDescription, editTask);

        return container;
    }
}
package com.greymatcha.flow.fxmlcontrollers;

import com.greymatcha.flow.models.tasklist.Task;
import com.greymatcha.flow.models.tasklist.TaskBuilder;
import com.greymatcha.flow.models.tasklist.TaskList;
import com.greymatcha.flow.utils.MyAnimation;
import com.greymatcha.flow.utils.StringUtil;
import com.greymatcha.flow.utils.Theme;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class TodolistController implements Initializable {

    @FXML
    public Pane taskPane, addTaskParentPane, contentPane, taskStackPane, taskPaneAddButtonParent, taskPaneApplyButtonParent, taskPaneRemoveButtonParent;
    @FXML
    public ScrollPane taskListScrollPane;
    @FXML
    public VBox taskListVBox;
    @FXML
    public TextField taskNameField, taskDescriptionField;
    @FXML
    public Button addTaskButton, taskPaneApplyButton, taskPaneAddButton, taskPaneRemoveButton;
    @FXML
    public Circle taskPaneCloseButton;

    private TaskList taskList;
    private Pane selectedRoot;
    private Task selectedTask;
    private Text selectedTaskName;
    private Text selectedTaskDescription;
    private AnchorPane selectedContainer;
    private Circle selectedCheckBox;
    private Rectangle selectedLineBreaker;
    private Rectangle selectedBackground;
    private Button selectedEditTask;

    private enum Mode {
        ADD_TASK,
        EDIT_TASK
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taskList = TaskList.getInstance();
        setupUI();
    }

    private void setupUI() {
        taskListScrollPane.setBackground(null);
        setupAddTaskButton();
        setupTaskPane();
    }

    private void setupAddTaskButton() {
        addTaskButton.setCursor(Cursor.HAND);
        addTaskButton.setOnMouseClicked(event -> {
            MyAnimation.shrinkButtonSize(addTaskParentPane);
            openTaskPane(Mode.ADD_TASK);
        });
        addTaskButton.setOnMouseReleased(event -> MyAnimation.resetButtonSize(addTaskParentPane));
    }

    private void setupTaskPane() {
        setTaskPaneProperties();
        setupTaskPaneCloseButton();
        setupAddButton();
        setupApplyButton();
        setupRemoveButton();
    }

    private void setupTaskPaneCloseButton() {
        taskPaneCloseButton.setOnMouseClicked(event -> closeTaskPane());
    }

    private void setTaskPaneProperties() {
        taskListScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        taskPane.setScaleX(0);
        taskPane.setScaleY(0);
        taskNameField.setBackground(null);
        taskDescriptionField.setBackground(null);
        taskPaneCloseButton.setCursor(Cursor.HAND);
    }

    private void setupAddButton() {
        taskPaneAddButton.setCursor(Cursor.HAND);
        taskPaneAddButton.setOnMouseClicked(event -> {
            MyAnimation.shrinkButtonSize(taskPaneAddButtonParent);
            addNewTask();
            closeTaskPane();
        });
        taskPaneAddButton.setOnMouseReleased(event -> MyAnimation.resetButtonSize(taskPaneAddButtonParent));
    }

    private void addNewTask() {
        String taskName = taskNameField.getText().isEmpty() ? "New Task" : taskNameField.getText();
        Task newTask = new TaskBuilder(UUID.randomUUID().toString())
                .setName(taskName)
                .setDescription(taskDescriptionField.getText())
                .create();

        if (taskList.addTask(newTask)) {
            taskListVBox.getChildren().add(createTaskBox(newTask));
        }
    }

    private void setupRemoveButton() {
        taskPaneRemoveButton.setCursor(Cursor.HAND);
        taskPaneRemoveButton.setOnMouseClicked(event -> {
            MyAnimation.shrinkButtonSize(taskPaneRemoveButtonParent);
            removeSelectedTask();
            closeTaskPane();
        });
        taskPaneRemoveButton.setOnMouseReleased(event -> MyAnimation .resetButtonSize(taskPaneRemoveButtonParent));
    }

    private void removeSelectedTask() {
        if (selectedTask == null || !taskList.removeTask(selectedTask)) {
            throw new RuntimeException("Task doesn't exist in the list.");
        }
        taskListVBox.getChildren().remove(selectedRoot);
        clearSelectedTask();
    }

    private void clearSelectedTask() {
        selectedTask = null;
        selectedRoot = null;
        selectedTaskName = null;
        selectedTaskDescription = null;
    }

    private void setupApplyButton() {
        taskPaneApplyButton.setCursor(Cursor.HAND);
        taskPaneApplyButton.setOnMouseClicked(event -> {
            MyAnimation.shrinkButtonSize(taskPaneApplyButtonParent);
            updateSelectedTask();
            closeTaskPane();
        });
        taskPaneApplyButton.setOnMouseReleased(event -> MyAnimation.resetButtonSize(taskPaneApplyButtonParent));
    }

    private void updateSelectedTask() {
        if (selectedTask != null) {
            selectedTask.setName(taskNameField.getText());
            selectedTask.setDescription(taskDescriptionField.getText());
            selectedTaskName.setText(taskNameField.getText());
            selectedTaskDescription.setText(taskDescriptionField.getText());
            selectedTaskDescription.setLayoutY(selectedTask.getDescription().isEmpty() ? 18 : 38);
            selectedContainer.setPrefHeight(selectedTask.getDescription().isEmpty() ? 31 : 51);
            selectedCheckBox.setLayoutY(12);
            selectedLineBreaker.setHeight(selectedRoot.getPrefHeight());
            selectedBackground.setHeight(selectedRoot.getPrefHeight() - 1);
            selectedEditTask.setPrefHeight(selectedRoot.getPrefHeight());
        }
    }

    private void closeTaskPane() {
        contentPane.setEffect(null);
        clearSelectedTask();
        ScaleTransition animation = MyAnimation.createScaleTransition(taskPane, 0.0, Duration.millis(100));
        animation.setOnFinished(event -> taskStackPane.setVisible(false));
        animation.play();
    }

    private void openTaskPane(Mode mode, Task... task) {
        contentPane.setEffect(new GaussianBlur());
        taskStackPane.setVisible(true);
        MyAnimation.createScaleTransition(taskPane, 1.0, Duration.millis(100)).play();

        if (mode == Mode.ADD_TASK) {
            taskNameField.clear();
            taskDescriptionField.clear();
            taskPaneApplyButtonParent.setVisible(false);
            taskPaneRemoveButtonParent.setVisible(false);
            taskPaneAddButtonParent.setVisible(true);
            return;
        }

        if (task.length == 0)
            throw new IllegalArgumentException("Mode.EDIT_TASK must have a task passed.");

        setupEditTaskPane(task[0]);
    }

    private void setupEditTaskPane(Task task) {
        taskPaneApplyButtonParent.setVisible(true);
        taskPaneRemoveButtonParent.setVisible(true);
        taskPaneAddButtonParent.setVisible(false);
        taskNameField.setText(task.getName());
        taskDescriptionField.setText(task.getDescription());
        selectedTask = task;
    }

    private AnchorPane createTaskBox(Task task) {
        AnchorPane container = new AnchorPane();
        container.setPrefHeight(task.getDescription().isEmpty() ? 31 : 51);
        container.setPrefWidth(taskListVBox.getWidth());

            Rectangle lineBreaker = new Rectangle();
            lineBreaker.setFill(Color.web(Theme.dark));
            lineBreaker.setHeight(container.getPrefHeight());
            lineBreaker.setWidth(container.getPrefWidth());

            Rectangle background = new Rectangle();
            background.setFill(Color.web(Theme.light));
            background.setHeight(container.getPrefHeight() - 1);
            background.setWidth(container.getPrefWidth());

            ImageView checkIcon = new ImageView(new Image(StringUtil.getImage("check.png")));
            checkIcon.setFitWidth(11);
            checkIcon.setFitHeight(11);
            checkIcon.setLayoutX(4);
            checkIcon.setLayoutY(7);
            checkIcon.setOpacity(0);

            Circle checkBox = new Circle(8);
            checkBox.setCursor(Cursor.HAND);
            checkBox.setFill(Color.web("transparent"));
            checkBox.setStrokeWidth(1);
            checkBox.setStroke(Color.web(Theme.dark));
            checkBox.setLayoutX(10);
            checkBox.setLayoutY(12);

            checkBox.setOnMouseEntered(_ -> checkIcon.setOpacity(0.50));
            checkBox.setOnMouseExited(_ -> checkIcon.setOpacity(0));

            Text taskName = new Text(task.getName());
            taskName.setFont(new Font(Theme.FONT_FAMILY, 18));
            taskName.setLayoutX(28);
            taskName.setLayoutY(18);

            Text taskDescription = new Text(task.getDescription());
            taskDescription.setFont(new Font(Theme.FONT_FAMILY, 12));
            taskDescription.setLayoutX(28);
            taskDescription.setLayoutY(task.getDescription().isEmpty() ? 18 : 38);

            Button editTask = new Button();
            editTask.setCursor(Cursor.HAND);
            editTask.setPrefWidth(container.getPrefWidth());
            editTask.setPrefHeight(container.getPrefHeight());
            editTask.setBackground(null);

            editTask.setOnMouseClicked(event -> {
                selectedRoot = container;
                selectedTaskName = taskName;
                selectedTaskDescription = taskDescription;
                selectedCheckBox = checkBox;
                selectedContainer = container;
                selectedLineBreaker = lineBreaker;
                selectedBackground = background;
                selectedEditTask = editTask;

                openTaskPane(Mode.EDIT_TASK, task);
                System.out.println("Task UUID: " + task.getUniqueID());
            });

            checkBox.setOnMouseClicked(_ -> {
                selectedRoot = container;
                selectedTaskName = taskName;
                selectedTaskDescription = taskDescription;
                selectedCheckBox = checkBox;
                selectedContainer = container;
                selectedLineBreaker = lineBreaker;
                selectedBackground = background;
                selectedEditTask = editTask;
                selectedTask = task;

                removeSelectedTask();
            });

        container.getChildren().addAll(lineBreaker, background, taskName, taskDescription, editTask, checkIcon, checkBox);
        return container;
    }
}
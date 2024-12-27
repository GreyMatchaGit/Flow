package com.greymatcha.flow.fxmlcontrollers;

import com.greymatcha.flow.models.smartdate.DateIdentifier;
import com.greymatcha.flow.models.tasklist.Task;
import com.greymatcha.flow.models.tasklist.TaskBuilder;
import com.greymatcha.flow.models.tasklist.TaskList;
import com.greymatcha.flow.utils.MyAnimation;
import com.greymatcha.flow.utils.Util;
import com.greymatcha.flow.utils.Theme;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.UUID;

import static com.greymatcha.flow.utils.Constant.*;

public class TodolistController implements Initializable {

    @FXML
    public Pane taskPane, addTaskParentPane, contentPane, taskStackPane, taskPaneAddButtonParent, taskPaneApplyButtonParent, taskPaneRemoveButtonParent;
    @FXML
    public ScrollPane taskListScrollPane;
    @FXML
    public VBox taskListVBox;
    @FXML
    public Text deadlineText;
    @FXML
    public TextField taskNameField, taskDeadlineField;
    @FXML
    public TextFlow taskNameTextFlow;
    @FXML
    public Button addTaskButton, taskPaneApplyButton, taskPaneAddButton, taskPaneRemoveButton;
    @FXML
    public Circle taskPaneCloseButton;
    @FXML
    public Rectangle backgroundDimmer;

    private TaskList taskList;
    private Pane selectedRoot;
    private Task selectedTask;
    private Text selectedTaskNameText;
    private Text selectedTaskDeadlineText;
    private AnchorPane selectedContainer;
    private AnchorPane selectedTaskDeadlineBackground;
    private Circle selectedCheckBox;
    private Rectangle selectedLineBreaker;
    private Rectangle selectedBackground;
    private Button selectedEditTask;
    private ZonedDateTime selectedTaskDeadline;

    private enum Mode {
        ADD_TASK,
        EDIT_TASK
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taskList = TaskList.getInstance();

        Platform.runLater(() -> {
            taskNameField.setFont(Theme.PARAGRAPH1);
            taskDeadlineField.setFont(Theme.PARAGRAPH1);
        });

        setupUI();
    }

    private void setupUI() {
        taskListScrollPane.setBackground(null);
        setupAddTaskButton();
        setupTaskPane();
    }

    private void setupAddTaskButton() {
        addTaskButton.setCursor(Cursor.HAND);
        addTaskButton.setOnMousePressed(event -> {
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

        new Thread(() -> taskNameField.setOnKeyReleased(_ -> {
            selectedTaskDeadline = highlightTextNameDate(taskNameField.getText());
            displayDeadline(selectedTaskDeadline);
        })).start();

        taskDeadlineField.setBackground(null);
        taskPaneCloseButton.setCursor(Cursor.HAND);
    }

    private ZonedDateTime highlightTextNameDate(String taskName) {
        ZonedDateTime extractedDate = DateIdentifier.extractDate(taskName);

        if (extractedDate == null) {
            resetTaskNameTextFlow();
            return null;
        }

        String wordWithDate = DateIdentifier.getLatestExtractedString();
        updateTaskNameTextFlow(wordWithDate, taskName);

        return extractedDate;
    }

    private void updateTaskNameTextFlow(String highlightedPortion, String entireText) {
        highlightedPortion = highlightedPortion.toLowerCase();
        int start = entireText.toLowerCase().indexOf(highlightedPortion);
        int end = start + highlightedPortion.length();

        AnchorPane leftText = createHighlightPane(entireText.substring(0, start), Theme.PARAGRAPH1, false);
        AnchorPane highlightedText = createHighlightPane(entireText.substring(start, end), Theme.PARAGRAPH1, true);
        AnchorPane rightText = createHighlightPane(entireText.substring(end), Theme.PARAGRAPH1, false);

        resetTaskNameTextFlow();
        taskNameTextFlow.getChildren().addAll(leftText, highlightedText, rightText);
    }

    public void resetTaskNameTextFlow() {
        taskNameTextFlow.getChildren().clear();
    }

    public AnchorPane createHighlightPane(String message, Font font, boolean isHighlighted) {
        Text text = new Text(message);
        text.setLayoutY(23);
        text.setLayoutX(5);
        text.setFont(font);
        text.setFill(Paint.valueOf("transparent"));

        AnchorPane background = new AnchorPane();
        background.getChildren().add(text);
        background.setPadding(new Insets(-2, 0, -2, 0));

        if (isHighlighted) {
            background.setStyle("-fx-background-radius: 4; -fx-background-color: " + Theme.SUNSET_ORANGE + ";");
        }

        return background;
    }

    private void setupAddButton() {
        taskPaneAddButton.setCursor(Cursor.HAND);
        taskPaneAddButton.setOnMousePressed(event -> {
            MyAnimation.shrinkButtonSize(taskPaneAddButtonParent);
            Task newlyAddedTask = addNewTask();
            newlyAddedTask.setDeadline(selectedTaskDeadline);
            closeTaskPane();
        });
        taskPaneAddButton.setOnMouseReleased(event -> MyAnimation.resetButtonSize(taskPaneAddButtonParent));
    }

    private Task addNewTask() {
        String taskName = taskNameField.getText().isEmpty() ? "New Task" : taskNameField.getText();
        Task newTask = new TaskBuilder(UUID.randomUUID().toString())
                .setName(taskName)
                .setDescription(taskDeadlineField.getText())
                .setDeadline(selectedTaskDeadline)
                .create();

        if (taskList.addTask(newTask)) {
            taskListVBox.getChildren().add(createTaskBox(newTask));
        }

        return newTask;
    }

    private void setupRemoveButton() {
        taskPaneRemoveButton.setCursor(Cursor.HAND);
        taskPaneRemoveButton.setOnMousePressed(event -> {
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
        selectedTaskNameText = null;
        selectedTaskDeadlineText = null;
    }

    private void setupApplyButton() {
        taskPaneApplyButton.setCursor(Cursor.HAND);
        taskPaneApplyButton.setOnMousePressed(event -> {
            MyAnimation.shrinkButtonSize(taskPaneApplyButtonParent);
            updateSelectedTask();
            selectedTask.setDeadline(selectedTaskDeadline);
            closeTaskPane();
        });
        taskPaneApplyButton.setOnMouseReleased(event -> MyAnimation.resetButtonSize(taskPaneApplyButtonParent));
    }

    private void updateSelectedTask() {
        if (selectedTask != null) {
            selectedTask.setName(taskNameField.getText());
            selectedTask.setDescription(taskDeadlineField.getText());
            selectedTaskNameText.setText(
                DateIdentifier.getLatestModifiedString().isEmpty()
                    ? "New Task"
                    : DateIdentifier.getLatestModifiedString()
            );
            selectedTaskDeadlineText.setText(
                    selectedTaskDeadline != null
                    ? " " + Util.formatDeadline(selectedTaskDeadline) + " "
                    : EMPTY_STRING
            );
            selectedTaskDeadlineBackground.setLayoutY(selectedTaskDeadline == null ? 6 : 26);
            selectedContainer.setPrefHeight(selectedTaskDeadline == null ? 31 : 51);
            selectedCheckBox.setLayoutY(12);
            selectedLineBreaker.setHeight(selectedRoot.getPrefHeight());
            selectedBackground.setHeight(selectedRoot.getPrefHeight() - 1);
            selectedEditTask.setPrefHeight(selectedRoot.getPrefHeight());
        }
    }

    private void closeTaskPane() {
        contentPane.setEffect(null);
        backgroundDimmer.setVisible(false);
        clearSelectedTask();
        ScaleTransition animation = MyAnimation.createScaleTransition(taskPane, 0.0, Duration.millis(100));
        animation.setOnFinished(event -> taskStackPane.setVisible(false));
        animation.play();
    }

    private void openTaskPane(Mode mode, Task... task) {
        contentPane.setEffect(new GaussianBlur());
        taskStackPane.setVisible(true);
        MyAnimation.createScaleTransition(taskPane, 1.0, Duration.millis(100)).play();
        backgroundDimmer.setVisible(true);

        if (mode == Mode.ADD_TASK) {
            taskNameField.clear();
            resetTaskNameTextFlow();
            taskDeadlineField.clear();
            deadlineText.setText(EMPTY_STRING);
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
        highlightTextNameDate(task.getName());
        displayDeadline(task.getDeadline());
        taskDeadlineField.setText(task.getDescription());

        selectedTask = task;
    }

    private void displayDeadline(ZonedDateTime deadline) {
        deadlineText.setFont(Theme.PARAGRAPH1);
        deadlineText.setFill(Color.web(Theme.OBSIDIAN));

        if (deadline == null) {
            deadlineText.setText(EMPTY_STRING);
            return;
        }

        String deadlineMonth = Util.toProperCase(deadline.getMonth());
        String deadlineWeek = Util.toProperCase(deadline.getDayOfWeek());

        String deadlineFormat = String.format(
                "%s %s %s\n%s",
                deadlineMonth,
                deadline.getDayOfMonth(),
                deadline.getYear(),
                deadlineWeek
        );
        deadlineText.setText(deadlineFormat);
    }

    private AnchorPane createTaskBox(Task task) {
        AnchorPane container = new AnchorPane();
        container.setPrefHeight(task.getDeadline() == null ? 31 : 51);
        container.setPrefWidth(taskListVBox.getWidth());

            Rectangle lineBreaker = new Rectangle();
            lineBreaker.setFill(Color.web(Theme.OBSIDIAN));
            lineBreaker.setHeight(container.getPrefHeight());
            lineBreaker.setWidth(container.getPrefWidth());

            Rectangle background = new Rectangle();
            background.setFill(Color.web(Theme.WHITE));
            background.setHeight(container.getPrefHeight() - 1);
            background.setWidth(container.getPrefWidth());

            ImageView checkIcon = new ImageView(new Image(Util.getImage("check.png")));
            checkIcon.setFitWidth(11);
            checkIcon.setFitHeight(11);
            checkIcon.setLayoutX(4);
            checkIcon.setLayoutY(7);
            checkIcon.setOpacity(0);

            Circle checkBox = new Circle(8);
            checkBox.setCursor(Cursor.HAND);
            checkBox.setFill(Color.web("transparent"));
            checkBox.setStrokeWidth(1);
            checkBox.setStroke(Color.web(Theme.OBSIDIAN));
            checkBox.setLayoutX(10);
            checkBox.setLayoutY(12);

            checkBox.setOnMouseEntered(_ -> checkIcon.setOpacity(0.50));
            checkBox.setOnMouseExited(_ -> checkIcon.setOpacity(0));

            Text taskName = new Text(
                DateIdentifier.getLatestModifiedString() == null || DateIdentifier.getLatestModifiedString().isEmpty()
                        ? "New Task"
                        : DateIdentifier.getLatestModifiedString()
            );
            taskName.setFont(Theme.HEADER5);
            taskName.setLayoutX(28);
            taskName.setLayoutY(18);

            if (task.getDeadline() != null) System.out.println(task.getDeadline());

            AnchorPane taskDeadlineBackground = new AnchorPane();
            taskDeadlineBackground.setStyle("-fx-background-radius: 4; -fx-background-color: " + Theme.MALACHITE + ";");
            taskDeadlineBackground.setPrefHeight(16);

                Text taskDeadline = new Text(
                    task.getDeadline() != null
                        ? " " + Util.formatDeadline(task.getDeadline()) + " "
                        : EMPTY_STRING
                );
                taskDeadline.setFont(Theme.PARAGRAPH2);
                taskDeadline.setLayoutY(12);

            taskDeadlineBackground.setLayoutX(28);
            taskDeadlineBackground.setLayoutY(task.getDeadline() == null ? 6: 26);

            taskDeadlineBackground.getChildren().add(taskDeadline);

            Button editTask = new Button();
            editTask.setCursor(Cursor.HAND);
            editTask.setPrefWidth(container.getPrefWidth());
            editTask.setPrefHeight(container.getPrefHeight());
            editTask.setBackground(null);

            editTask.setOnMouseClicked(event -> {
                selectedRoot = container;
                selectedTaskNameText = taskName;
                selectedTaskDeadlineText = taskDeadline;
                selectedCheckBox = checkBox;
                selectedContainer = container;
                selectedLineBreaker = lineBreaker;
                selectedBackground = background;
                selectedTaskDeadlineBackground = taskDeadlineBackground;
                selectedEditTask = editTask;
                selectedTask = task;

                openTaskPane(Mode.EDIT_TASK, task);
                System.out.println("Task UUID: " + task.getUniqueID());
            });

            checkBox.setOnMouseClicked(_ -> {
                selectedRoot = container;
                selectedTaskNameText = taskName;
                selectedTaskDeadlineText = taskDeadline;
                selectedCheckBox = checkBox;
                selectedContainer = container;
                selectedLineBreaker = lineBreaker;
                selectedBackground = background;
                selectedTaskDeadlineBackground = taskDeadlineBackground;
                selectedEditTask = editTask;
                selectedTask = task;

                removeSelectedTask();
            });

        container.getChildren().addAll(lineBreaker, background, taskName, taskDeadlineBackground, editTask, checkIcon, checkBox);
        return container;
    }
}
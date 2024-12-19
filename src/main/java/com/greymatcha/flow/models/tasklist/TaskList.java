package com.greymatcha.flow.models.tasklist;

import java.util.ArrayList;
import java.util.UUID;

public class TaskList {

    private static TaskList instance = null;
    private final ArrayList<Task> tasks;

    private TaskList() {
        tasks = new ArrayList<>();
    }

    public static TaskList getInstance() {
        if (instance == null)
            instance = new TaskList();

        return instance;
    }

    public boolean addTask(Task task) {
        return tasks.add(task);
    }

    public boolean addTask(String name, String description) {
        Task newTask = new TaskBuilder(UUID.randomUUID().toString())
                .setName(name)
                .setDescription(description)
                .create();

        return tasks.add(newTask);
    }

    public boolean removeTask(Task task) {
        return tasks.remove(task);
    }

    public boolean removeTask(String UUID) {
        Task taskToRemove = null;
        for (Task task : tasks) {
            if (task.getUniqueID().equals(UUID)) {
                taskToRemove = task;
                break;
            }
        }
        return tasks.remove(taskToRemove);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}

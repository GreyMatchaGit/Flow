package com.greymatcha.flow.models.tasklist;

public class TaskBuilder {

    private String name;
    private String description;
    private final String uniqueID;

    public TaskBuilder(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public TaskBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public TaskBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public Task create() {
        return new Task(name, description, uniqueID);
    }
}

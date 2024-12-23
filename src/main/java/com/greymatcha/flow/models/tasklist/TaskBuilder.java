package com.greymatcha.flow.models.tasklist;

import java.time.ZonedDateTime;

public class TaskBuilder {

    private String name;
    private String description;
    private ZonedDateTime deadline;
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

    public TaskBuilder setDeadline(ZonedDateTime deadline) {
        this.deadline = deadline;
        return this;
    }

    public Task create() {
        return new Task(name, description, deadline, uniqueID);
    }
}

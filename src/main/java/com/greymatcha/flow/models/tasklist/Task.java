package com.greymatcha.flow.models.tasklist;

import java.time.ZonedDateTime;

public class Task {

    private String name;
    private String description;
    private final String uniqueID;
    private ZonedDateTime deadline;

    public Task(String name, String description, ZonedDateTime deadline, String uniqueID) {

        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.uniqueID = uniqueID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setDeadline(ZonedDateTime deadline) {
        this.deadline = deadline;
    }

    public ZonedDateTime getDeadline() {
        return deadline;
    }
}

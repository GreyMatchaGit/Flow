package com.greymatcha.flow.models.tasklist;

public class Task {

    private String name;
    private String description;
    private String uniqueID;

    public Task(String name, String description, String uniqueID) {

        this.name = name;
        this.description = description;
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
}

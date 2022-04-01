package com.careerdevs.gorestv1.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ToDoModel {

    private int id;

    @JsonProperty ("user_id")
    private int userId;

    private String title;

    @JsonProperty ("due_on")
    private String dueOn;

    private String status;

    public ToDoModel() {
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDueOn() {
        return dueOn;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ToDoModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", dueOn='" + dueOn + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}

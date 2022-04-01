package com.careerdevs.gorestv1.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostModel {

    private int id;

    @JsonProperty ("post_id")
    private int postId;

    private String title;
    private String body;

    public PostModel() {
    }

    public int getId() {
        return id;
    }

    public int getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "PostModel{" +
                "id=" + id +
                ", postId=" + postId +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

}

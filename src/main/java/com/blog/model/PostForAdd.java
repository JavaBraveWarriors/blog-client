package com.blog.model;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class PostForAdd extends Post {

    private List<Long> tags;

    public PostForAdd() {
    }

    public PostForAdd(Long id, String title, String description, String text, String pathImage, Long authorId) {
        super(id, title, description, text, pathImage, authorId);
    }

    public List<Long> getTags() {
        return tags;
    }

    public void setTags(List<Long> tags) {
        this.tags = tags;
    }

    public String toString() {
        return "PostForAdd{" +
                "tags=" + tags +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", text='" + text + '\'' +
                ", timeOfCreation=" + timeOfCreation +
                ", pathImage='" + pathImage + '\'' +
                ", authorId=" + authorId +
                '}';
    }
}

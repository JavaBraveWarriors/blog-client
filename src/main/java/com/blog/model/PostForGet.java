package com.blog.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class PostForGet extends Post {

    private String authorName;

    private String authorLastName;

    private Long viewsCount;

    private Long commentsCount;

    private Long likesCount;

    private List<Comment> comments;

    @NotNull
    private List<Tag> tags;

    public PostForGet() {
    }

    public PostForGet(Long id, String title, String description, String text, String pathImage, Long authorId) {
        super(id, title, description, text, pathImage, authorId);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public Long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Long viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Long getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Long likesCount) {
        this.likesCount = likesCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "PostForGet{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", text='" + text + '\'' +
                ", timeOfCreation=" + timeOfCreation +
                ", pathImage='" + pathImage + '\'' +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", authorLastName='" + authorLastName + '\'' +
                ", viewsCount=" + viewsCount +
                ", commentsCount=" + commentsCount +
                ", likesCount=" + likesCount +
                ", comments=" + comments +
                ", tags=" + tags +
                '}';
    }
}


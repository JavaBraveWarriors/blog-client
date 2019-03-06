package com.blog.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Tag implements Serializable {

    static final long serialVersionUID = 42L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(min = 3, max = 100, message = "Title should be less than 100 characters.")
    private String title;

    private String pathImage;

    public Tag() {
    }

    public Tag(Long id) {
        this.id = id;
    }

    public Tag(Long id, String title, String pathImage) {
        this.id = id;
        this.title = title;
        this.pathImage = pathImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    // Override, because I use the "contains" method in the list.
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(getId(), tag.getId()) &&
                Objects.equals(getTitle(), tag.getTitle()) &&
                Objects.equals(getPathImage(), tag.getPathImage());
    }

    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hash(getId());
        return hash;
    }

    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", pathImage='" + pathImage + '\'' +
                '}';
    }
}

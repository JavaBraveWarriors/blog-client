package com.blog.dao;

import com.blog.model.Tag;

import java.util.List;

/**
 * This interface defines various operations management for the Tag object.
 *
 * @author Aliaksandr Yeutushenka
 */
public interface TagDao {

    /**
     * Gets the list of objects of the all tags.
     *
     * @return {List<Tag>} is a list of all tags from the database.
     */
    List<Tag> getAllTags();
}

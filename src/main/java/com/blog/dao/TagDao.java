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
     * Gets a {Tag} object where id is equal to argument parameter
     *
     * @param id {Long} value the ID of the tag you want to get.
     * @return {Tag} is a object which has this ID.
     */
    Tag getTagById(final Long id);

    /**
     * Gets the list of objects of the all tags.
     *
     * @return {List<Tag>} is a list of all tags from the database.
     */
    List<Tag> getAllTags();

    /**
     * Adds new tag.
     *
     * @param tag {Tag} to be added.
     */
    void addTag(Tag tag);

    /**
     * Update tag.
     *
     * @param tag {Tag} to be added.
     */
    void updateTag(final Tag tag);
}

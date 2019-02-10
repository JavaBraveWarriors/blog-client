package com.blog.model;

import com.blog.Tag;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class TagFormatter implements Formatter<Tag> {
    @Override
    public Tag parse(String text, Locale locale) throws ParseException {
        Tag tag = new Tag();
        tag.setId(Long.valueOf(text));
        return tag;
    }

    @Override
    public String print(Tag object, Locale locale) {
        return object.getId().toString();
    }
}

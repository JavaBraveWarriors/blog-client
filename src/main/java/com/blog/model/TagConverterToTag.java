package com.blog.model;

import com.blog.Tag;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TagConverterToTag implements Converter<String[], List<Tag>> {

    @Override
    public List<Tag> convert(String[] source) {
        List<Tag> tags = new ArrayList<>();
        for (String tagId : source) {
            Tag tag = new Tag();
            tag.setId(Long.valueOf(tagId));
            tags.add(tag);
        }
        return tags;
    }
}

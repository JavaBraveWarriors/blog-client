package com.blog.model;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TagConverterToString implements Converter<Tag, String> {
    @Override
    public String convert(Tag source) {
        return source.getId().toString();
    }
}
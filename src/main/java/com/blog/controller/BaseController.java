package com.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class BaseController {

    private MessageSource messageSource;

    @Autowired
    public BaseController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    protected String getLocaleMessage(String propertyName) {
        return messageSource.getMessage(propertyName, null, LocaleContextHolder.getLocale());
    }
}

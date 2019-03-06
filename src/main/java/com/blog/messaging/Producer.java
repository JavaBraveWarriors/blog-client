package com.blog.messaging;

import com.blog.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

@Component
public class Producer {

    private JmsTemplate jmsTemplate;

    @Qualifier(value = "tag")
    private Queue queue;

    @Autowired
    public Producer(JmsTemplate jmsTemplate, Queue queue) {
        this.jmsTemplate = jmsTemplate;
        this.queue = queue;
    }

    public void sendMessage(final Tag tag) {
        jmsTemplate.convertAndSend(queue, tag);
    }
}
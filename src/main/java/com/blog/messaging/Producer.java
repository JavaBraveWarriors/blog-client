package com.blog.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Message;
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

    public void sendMessage(final String text) {
        jmsTemplate.send(queue, session -> {
            Message message = session.createTextMessage(text);
            message.setJMSReplyTo(queue);
            return message;
        });
    }
}
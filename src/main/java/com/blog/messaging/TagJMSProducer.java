package com.blog.messaging;

import com.blog.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

@Component
public class TagJMSProducer {

    private JmsTemplate jmsTemplate;

    private Queue queue;

    @Autowired
    public TagJMSProducer(JmsTemplate jmsTemplate, @Qualifier(value = "tag") Queue queue) {
        this.jmsTemplate = jmsTemplate;
        this.queue = queue;
    }

    public void sendTagMessage(final Tag tag) {
        jmsTemplate.convertAndSend(queue, tag);
    }
}
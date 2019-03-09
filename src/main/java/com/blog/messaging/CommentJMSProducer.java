package com.blog.messaging;

import com.blog.model.Comment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class CommentJMSProducer {

    private static final Logger LOGGER = LogManager.getLogger();

    private final JmsTemplate jmsTemplate;

    private Queue commentQueue;
    private Queue responseCommentQueue;

    @Autowired
    public CommentJMSProducer(
            final JmsTemplate jmsTemplate,
            @Qualifier(value = "comment") Queue commentQueue,
            @Qualifier(value = "responseComment") Queue responseCommentQueue) {
        this.jmsTemplate = jmsTemplate;
        this.commentQueue = commentQueue;
        this.responseCommentQueue = responseCommentQueue;
    }

    public String sendOrder(Comment comment) throws JMSException {
        final AtomicReference<Message> message = new AtomicReference<>();

        jmsTemplate.convertAndSend(commentQueue, comment, messagePostProcessor -> {
            message.set(messagePostProcessor);
            return messagePostProcessor;
        });

        return message.get().getJMSMessageID();
    }

    public String receiveOrderStatus(String correlationId) {
        String status = (String) jmsTemplate.receiveSelectedAndConvert(
                responseCommentQueue,
                "JMSCorrelationID = '" + correlationId + "'");
        return status;
    }

}

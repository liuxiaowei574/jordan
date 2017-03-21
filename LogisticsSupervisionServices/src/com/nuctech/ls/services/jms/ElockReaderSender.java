package com.nuctech.ls.services.jms;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * MQ消息下发
 * 
 * @author
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class ElockReaderSender {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Resource(name = "elockReaderReqTopic")
    private Topic elockReaderReqTopic;

    @Resource(name = "elockReaderResTopic")
    private Topic elockReaderResTopic;

    @Resource(name = "jmsTemplate")
    private JmsTemplate jmsTemplate;

    public void sendMessage(final String sentText, final String clientid) {
        this.jmsTemplate.send(this.elockReaderReqTopic, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage returnMessage = null;
                returnMessage = session.createTextMessage(sentText);
                returnMessage.setStringProperty("clientid", clientid);
                logger.info("Send Event MQ msg#" + sentText);
                return returnMessage;
            }
        });
    }

}

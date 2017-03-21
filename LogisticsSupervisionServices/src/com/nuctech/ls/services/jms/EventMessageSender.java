package com.nuctech.ls.services.jms;

import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.nuctech.ls.model.bo.common.Event;
import com.nuctech.util.DateUtils;

/**
 * 事件发送者
 * */
@Component
public class EventMessageSender {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    static Session session = null;
    static MessageProducer producer = null;
    @Resource(name = "eventTopic")
    private Topic eventTopic;
    @Resource
    private JmsTemplate jmsTemplate;

    public void sendMessage(final Event event) throws JMSException {
        jmsTemplate.send(eventTopic, new MessageCreator() {

            @SuppressWarnings("rawtypes")
            @Override
            public Message createMessage(Session session) throws JMSException {
                final MapMessage message = session.createMapMessage();
                message.setStringProperty("site", (event.getSite() != null ? event.getSite() : ""));
                message.setObjectProperty("laneNumber", event.getLaneNumber());
                message.setStringProperty("device", event.getDevice());
                message.setObjectProperty("deviceNumber", event.getDeviceNumber());
                message.setStringProperty("time",
                        DateUtils.date2String(new Date(), DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS));
                message.setStringProperty("event", event.getEvent());

                Properties dataPropts = event.getDataPropts();
                Iterator keyIt = dataPropts.keySet().iterator();
                while (keyIt.hasNext()) {
                    String key = (String) keyIt.next();
                    message.setString(key, dataPropts.getProperty(key));
                }
                return message;
            }
        });
        logger.info(String.format("Event:%s(%s %s )sended", event.getEvent(), event.getSite(), event.getLaneNumber()));
    }
}

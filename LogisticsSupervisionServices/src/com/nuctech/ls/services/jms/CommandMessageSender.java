package com.nuctech.ls.services.jms;

import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.model.bo.common.Command;
import com.nuctech.util.DateUtils;

/**
 * 指令消息发送器
 * 
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class CommandMessageSender {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Resource(name = "commandTopic")
    private Topic commandTopic;
    @Resource
    private JmsTemplate jmsTemplate;

    /**
     * 发送指令
     * 
     * @param command
     *        指令对象
     */
    public void send(final Command command) {
        jmsTemplate.send(commandTopic, new MessageCreator() {

            @SuppressWarnings("rawtypes")
            @Override
            public Message createMessage(Session session) throws JMSException {
                final MapMessage message = session.createMapMessage();
                message.setStringProperty("site", (command.getSite() != null ? command.getSite() : ""));
                message.setObjectProperty("laneNumber", command.getLaneNumber());
                message.setStringProperty("device", command.getDevice());
                message.setObjectProperty("deviceNumber", command.getDeviceNumber());
                message.setStringProperty("time",
                        DateUtils.date2String(new Date(), DateUtils.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS_SSS));
                message.setStringProperty("command", command.getCommand());

                Properties dataPropts = command.getDataPropts();
                Iterator keyIt = dataPropts.keySet().iterator();
                while (keyIt.hasNext()) {
                    String key = (String) keyIt.next();
                    String value = dataPropts.getProperty(key);
                    if (!value.contains("\\u")) {
                        value = StringEscapeUtils.escapeJava(value);
                    }
                    message.setObject(key, value);
                }
                logger.info("Send Command MQ msg#" + message);
                return message;
            }
        });
    }
}

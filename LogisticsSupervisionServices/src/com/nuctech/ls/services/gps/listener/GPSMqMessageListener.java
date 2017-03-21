package com.nuctech.ls.services.gps.listener;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.services.gps.utils.GPSMeasureMessageHandler;

/**
 * @author sunming
 *
 */
@Component("gpsMqMessageListener")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class GPSMqMessageListener implements MessageListener {

    Logger logger = Logger.getLogger(this.getClass());
    // 线程池，处理消息
    @Resource
    private ThreadPoolTaskExecutor threadPool;
    @Resource
    private GPSMeasureMessageHandler handler;

    @Override
    public void onMessage(Message message) {
        String messageId = null;
        try {
            messageId = message.getJMSMessageID();
            if (messageId == null) {
                logger.warn("receive message# receive message ID failed");
            }
            if (!(message instanceof TextMessage)) {
                logger.warn(String.format("message#%s is not mapping type message ,ignore", messageId));
            } else if (message instanceof TextMessage) {
                final String msg = ((TextMessage) message).getText();
                logger.info("receive MQ message" + msg);
                final String clientId = message.getStringProperty("clientId");
                // 启用多线程处理MQ消息,此操作表示当前MQ消息已经接收完成，交予新线程处理GPS坐标存储逻辑
                threadPool.execute(new Runnable() {

                    @Override
                    public void run() {
                        handler.doHandle(msg, clientId);
                    }
                });
            }

        } catch (JMSException e) {
            logger.error("handle message failed：" + e.getLocalizedMessage());
        } finally {
            try {
                message.acknowledge();
                logger.info("feedback message success&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            } catch (JMSException e) {
                logger.error("feedback message fail:" + e.getLocalizedMessage());
            }
        }
    }

}

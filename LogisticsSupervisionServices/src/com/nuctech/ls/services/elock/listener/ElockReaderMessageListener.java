package com.nuctech.ls.services.elock.listener;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import net.sf.json.JSONObject;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nuctech.ls.services.elock.service.ElockService;
import com.nuctech.ls.services.elock.utils.ElockConstant;

/**
 * 关锁消息通信监听
 * 
 * @author 姜永权
 */
@Component("elockReaderMessageListener")
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class ElockReaderMessageListener implements MessageListener {

    private final Logger logger = Logger.getLogger(this.getClass().getName());
    @Resource
    private ElockService elockService;
    @Resource
    private JmsTemplate jmsTemplate;

    @Override
    public void onMessage(Message message) {
        String messageId = null;
        try {
            messageId = message.getJMSMessageID();
            if (messageId == null) {
                logger.warning("Receive msg# message ID is null");
            }

            if (!(message instanceof TextMessage)) {
                logger.warning(String.format("MQ Message#%s is not TextMessage,ignore", messageId));
            } else if (message instanceof TextMessage) {
                final String msg = ((TextMessage) message).getText();
                logger.info("Receive MQ message:" + msg);
                JSONObject jsonMsg = JSONObject.fromObject(msg);
                String commandId = jsonMsg.getString("Id");
                switch (commandId) {
                    case ElockConstant.ELOCK_AWAKE_125K:
                        // 处理125k
                        elockService.dealAwake(jsonMsg);
                        break;
                    default:
                        logger.info("commandId:" + commandId);
                }
            }
        } catch (JMSException e) {
            logger.severe("Deal message error" + e.getLocalizedMessage());
        } finally {
            try {
                message.acknowledge();
                logger.info("MQ message response complete&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            } catch (JMSException e) {
                logger.severe("MQ message response error：" + e.getLocalizedMessage());
            }
        }
    }

}

/**
 * 
 */
package com.nuctech.ls.services.gps.utils;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.nuctech.ls.model.bo.monitor.LsMonitorElockGpslogBO;
import com.nuctech.ls.model.service.MonitorElockGPSLogService;
import com.nuctech.ls.services.gps.models.MessageConstants;
import com.nuctech.ls.services.gps.models.RequestMessage;
import com.nuctech.util.NuctechUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

/**
 * GPS消息处理
 * 将GPS的json字符串保存，并转化成RequestMessage处理
 * 
 * @author sunming
 * @date 2015/04/08
 */
@Component
public class GPSMeasureMessageHandler {

    Logger logger = Logger.getLogger(this.getClass());
    @Resource(name = "GPSeventTopic")
    private Topic responseTopic;
    @Resource
    private JmsTemplate jmsTemplate;
    @Resource
    private MonitorElockGPSLogService elockGPSLogService;
    @Resource
    private MonitorVehicleGPSHandle handle;

    JSONArray result = null;

    // 接收到消息:[{"id":"0200","deviceType":"AT","phone_num":"869152021253454","seq_num":61,
    // "gps":[{"longitude":116325828,"latitude":39992446,"direction":54,"speed":0,"timestamp":"2016-06-08
    // 17:31:06"}]}]

    /**
     * 处理GPS信息,并返回相应消息 目前注册和鉴权已放置网关中处理，GPS坐标信息不需要返回MQ消息
     * 
     * @param message
     * @throws JMSException
     */
    public void doHandle(final String message, final String clientId) {

        result = new JSONArray();

        if (null != message && !"".equals(message)) {
            JSONArray array = JSONArray.fromObject(message);
            // 将mq消息中json数据转化
            for (Iterator<?> it = array.iterator(); it.hasNext();) {
                Object item = it.next();
                if (item == null || item instanceof JSONNull) {
                    continue;
                }
                JSONObject object = (JSONObject) item;
                RequestMessage requestMessage = (RequestMessage) JSONObject.toBean(object, RequestMessage.class);
                if (NuctechUtil.isNotNull(requestMessage)) {
                    // 存储原始数据
                    LsMonitorElockGpslogBO lsMonitorElockGpslogBO = new LsMonitorElockGpslogBO();
                    lsMonitorElockGpslogBO.setGpslogId(UUID.randomUUID().toString());
                    lsMonitorElockGpslogBO.setCreateTime(new Date());
                    if (NuctechUtil.isNotNull(object)) {
                        lsMonitorElockGpslogBO.setLogString(object.toString());
                    }
                    elockGPSLogService.save(lsMonitorElockGpslogBO);
                    logger.info("save raw GPS data:" + object.toString());
                    logger.info("terminal location information:" + requestMessage.getId());
                    // 如果是位置信息
                    if (MessageConstants.CODE_LOCATION.equals(requestMessage.getId())) {
                        // 判断指令下发返回值 set
                        if (null != requestMessage.getConsole()) {
                            logger.info("receive data and send back message:" + JSONObject.fromObject(requestMessage));
                        } else {
                            // 坐标信息上传，执行位置信息存储
                            logger.info(
                                    "receive data and start execute  storage:" + JSONObject.fromObject(requestMessage));
                            handle.dealRequestMessage(requestMessage);
                        }
                    } else if (MessageConstants.CODE_UNDERLINE.equals(requestMessage.getId())) {
                        // 判断指令下发返回值 set
                        if (null != requestMessage.getReason()) {
                            logger.info("receive offline message:" + JSONObject.fromObject(requestMessage));
                            handle.dealOffLineMessage(requestMessage);
                        }
                    }
                }
            }
            // 返回消息
            if (null != result && !"".equals(result.toString()) && result.size() > 0) {
                final JSONArray response = result;

                jmsTemplate.send(responseTopic, new MessageCreator() {

                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        TextMessage returnMessage = session.createTextMessage(response.toString());
                        returnMessage.setStringProperty("clientId", clientId);
                        logger.info(" feedback message#" + response.toString());
                        return returnMessage;
                    }

                });

            }

        }
    }

    public static void main(String[] args) {
        // String message =
        // "[{\"id\":\"0200\",\"deviceType\":\"AT\",\"phone_num\":\"869152021253454\",\"seq_num\":61,
        // \"gps\":[{\"longitude\":116325828,\"latitude\":39992446,
        // \"direction\":54,\"speed\":0,\"timestamp\":\"2016-06-08
        // 17:31:06\"}]}]";
        String message = "[{\"id\":\"0200\",\"phone_num\":\"000000000004\","
                + "\"seq_num\":\"3\",\"gps\":{\"state\":\"11000\",\"latitude\":41133403,"
                + "\"longitude\":43800059,\"altitude\":418,\"speed\":480,\"direction\":3,"
                + "\"timestamp\":\"2016-06-08 19:25:00\"},\"lock\":{\"status\":\"1\",\"sealStatus\":\"1\""
                + ",\"vvv\":\"418\",\"antidismantle\":\"0\",\"events\":\"0\",\"operationID\":\"66666\","
                + "\"seals\":[\"0\",\"1\",\"2\",\"3\",\"4\",\"5\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\"]}}]";
        JSONArray array = JSONArray.fromObject(message);
        JSONObject object = (JSONObject) array.get(0);
        Object gpsobj = object.get("gps");
        if (gpsobj instanceof JSONArray) {

        } else {
            object.put("gps", JSONArray.fromObject(gpsobj));
        }
        RequestMessage requestMessage = (RequestMessage) JSONObject.toBean(object, RequestMessage.class);
    }

}

/**
 * 
 */
package com.nuctech.ls.services.gps.models;

/**
 * MQ消息传递对象属性，用于json转换
 * 
 * @author sunming
 *
 */
public abstract class AbstractMQMessage {

    private String clientId;

    private AbstractMessageContent content;

}

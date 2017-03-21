package com.nuctech.ls.services.websokcet;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

import org.springframework.stereotype.Component;

@Component
public class WebsocketService {

    /**
     * 利用websocket 推送消息
     * 
     * @param message
     */
    public static void sendMessage(String message) {

        if (WebSocketServer.onLineUser.size() != 0) {

            List<Session> onLineUser = new ArrayList<Session>(WebSocketServer.onLineUser.values());

            SendMessageUtil.sendMsg(onLineUser, message);
        }

    }

}

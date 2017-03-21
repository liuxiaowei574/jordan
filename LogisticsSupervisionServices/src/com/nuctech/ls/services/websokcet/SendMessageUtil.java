package com.nuctech.ls.services.websokcet;

import java.util.List;

import javax.websocket.Session;

public class SendMessageUtil {

    /**
     * 给一个session推送消息
     * 
     * @param webSocketInfo
     * @param msg
     */
    public static void sendMsg(Session session, String msg) {
        try {
            if (session != null) {
                synchronized (session) {
                    session.getBasicRemote().sendText(msg);
                }
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 给多个session推送消息
     * 
     * @param sessionList
     * @param msg
     */
    public static void sendMsg(List<Session> sessionList, String msg) {
        for (Session session : sessionList) {
            try {
                if (session != null) {
                    synchronized (session) {
                        session.getBasicRemote().sendText(msg);
                    }
                }
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * 给多个session推送消息
     * 
     * @param sessionList
     * @param msg
     */
    // public static void sendMsg(List<Session> sessionList,Object msg){
    // for (Session session : sessionList) {
    // try {
    // if(session!=null){
    // session.getBasicRemote().sendObject(msg);
    // }
    // } catch (IOException ioe) {
    // ioe.printStackTrace();
    // } catch (EncodeException e) {
    // e.printStackTrace();
    // }
    // }
    // }

}

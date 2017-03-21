package com.nuctech.ls.center.websocket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
 
@ServerEndpoint(value = "/websocket")
public class WebSocketServer{
  
	public static Map<String,Session> onLineUser=new ConcurrentHashMap<String,Session>();
	
	@OnOpen
	public void onOpen(Session currSession) {
		
		onLineUser.put(currSession.getId(), currSession);
		
	}
	@OnClose
	public void onClose(Session currentSession) {
		if(onLineUser.containsKey(currentSession.getId())){
			
			onLineUser.remove(currentSession.getId());
		}
	}
	@OnMessage
	public void onMessage(String message, Session currentSession) {
		
	}
	
	@OnError
	public void onError(Throwable e, Session currentSession){
         if(onLineUser.containsKey(currentSession.getId())){
			//System.out.println(currentSession.getId()+"close websocket");
			onLineUser.remove(currentSession.getId());
		}
	}
	
}



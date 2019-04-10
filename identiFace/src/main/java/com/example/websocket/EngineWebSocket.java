package com.example.websocket;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/websocket/engine/{scenario}")
public class EngineWebSocket {
	
	// open websocket connection
    @OnOpen
    public void onOpen(Session session) {
    	System.out.println("websocket開啟");
    }
    
    // close websocket connection
    @OnClose
    public void onClose() {
    	
    }
    
    @OnMessage
    public void onMessage(Session session , String message) {
    	System.out.println("Get the message="+message);
    }
    
    @OnError
    public void onError() {
    	
    }
}

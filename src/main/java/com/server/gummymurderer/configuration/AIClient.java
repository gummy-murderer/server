package com.server.gummymurderer.configuration;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@ClientEndpoint
@RequiredArgsConstructor
public class AIClient {

    private final WebSocketUnityHandler unityHandler;

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    @OnMessage
    public void onMessage(String message) {
        unityHandler.sendMessageToUnity(message);
    }
}



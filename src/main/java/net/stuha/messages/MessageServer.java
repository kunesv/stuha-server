package net.stuha.messages;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/ws/message")
public class MessageServer {

    @OnMessage
    public void onMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText("Nazdar!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

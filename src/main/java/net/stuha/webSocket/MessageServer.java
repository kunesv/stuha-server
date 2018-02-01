package net.stuha.webSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.stuha.security.Token;
import net.stuha.security.TokenService;
import net.stuha.security.UnauthorizedRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//@ServerEndpoint(value = "/ws/message", configurator = SpringConfigurator.class)
public class MessageServer {

    private TokenService tokenService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServer.class);

    public static final Map<UUID, Session> peers = Collections.synchronizedMap(new HashMap<>());

    //    @OnOpen
    public void onOpen(Session session) throws UnauthorizedRequestException {
        final UUID userId = UUID.fromString(session.getRequestParameterMap().get("userId").get(0));

        final Token token = tokenService.findToken(session.getRequestParameterMap().get("token").get(0), userId);

        if (token == null) {
            throw new UnauthorizedRequestException();
        }

        try {
            peers.put(userId, session);

            session.getBasicRemote().sendText(new ObjectMapper().writeValueAsString(new Status("Connection Established")));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void sendText(UUID userId, String text) {
        peers.forEach((uid, session) -> {
            if (uid.equals(userId)) {
                try {
                    session.getBasicRemote().sendText(text);
                } catch (IOException e) {
                    LOGGER.error("Failed to send WebSocket message to user: " + userId, e);
                }
            }
        });
    }

    //    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

        System.out.println("Message from " + session.getId() + ": " + message);

//        for (Session peer : peers) {
//            peer.getBasicRemote().sendText(" Message from " + session.getId() + " : " + message);
//        }
    }


    //    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() + " has ended");
        peers.remove(session);
    }
}

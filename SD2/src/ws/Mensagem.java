package ws;

/**
 * Created by dannsguardado on 14/12/2016.
 */

import javax.websocket.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


public class Mensagem {
    private static final AtomicInteger sequence = new AtomicInteger(1);
    private final String username;
    private Session session;
    //private static final Set <ChatWebSocket> connections = new CopyOnWriteArraySet< >();

    public Mensagem() {
        username = "User" + sequence.getAndIncrement();
    }


    @OnOpen
    public void start(Session session) {
        this.session = session;
        String message = "*" + username + "* connected.";
        sendMessage(message);
    }

    @OnClose
    public void end() {
        // clean up once the WebSocket connection is closed
    }

    @OnMessage
    public void receiveMessage(String message) {
        // one should never trust the client, and sensitive HTML
        // characters should be replaced with &lt; &gt; &quot; &amp;
        String upperCaseMessage = message.toUpperCase();
        sendMessage("[" + username + "] " + upperCaseMessage);
    }

    @OnError
    public void handleError(Throwable t) {
        t.printStackTrace();
    }

    private void sendMessage(String text) {
        // uses *this* object's session to call sendText()
        try {
            this.session.getBasicRemote().sendText(text);
        } catch (IOException e) {
            // clean up once the WebSocket connection is closed
            try {
                this.session.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }        }
    }
}

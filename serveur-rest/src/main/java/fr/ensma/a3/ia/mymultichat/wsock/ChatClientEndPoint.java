package fr.ensma.a3.ia.mymultichat.wsock;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import fr.ensma.a3.ia.mymultichat.business.messages.ChatMessage;
import fr.ensma.a3.ia.mymultichat.wsock.coder.ChatMessageDecoder;
import fr.ensma.a3.ia.mymultichat.wsock.coder.ChatMessageEncoder;

/**
 * ChatClientEndPoint
 */
@ClientEndpoint(encoders = ChatMessageEncoder.class, decoders = ChatMessageDecoder.class)
public class ChatClientEndPoint {

    private Session session = null;

    private PrintStream out = new PrintStream(System.out, true, UTF_8);

    public ChatClientEndPoint(URI endpointURI) throws DeploymentException, IOException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, endpointURI);
    }

    public Session getSession() {
        return session;
    }

    @OnOpen
    public void onOpen(Session sess) {
        out.println("Connexion établie !!");
        this.session = sess;
    }

    @OnMessage
    public void onMessage(ChatMessage mess) {
        out.println(mess.getLePseudo() + " dit : " + mess.getLeContenu());
    }

    @OnError
    public void processError(Throwable t) {
        t.printStackTrace();
    }

    @OnClose
    public void onClose(Session sess, CloseReason reason) {
        try {
            session.close();
            sess.close();
            out.println("Bye !!!");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

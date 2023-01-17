package fr.ensma.a3.ia.mymultichat.restclient;
// TODO: Documentation

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.util.List;
import java.util.Scanner;

import javax.websocket.DeploymentException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import fr.ensma.a3.ia.mymultichat.business.canalchat.ChatCanalDesc;
import fr.ensma.a3.ia.mymultichat.business.messages.ChatMessage;
import fr.ensma.a3.ia.mymultichat.wsock.ChatClientEndPoint;

/**
 * Hello world!
 *
 */
public class App {
    // FIXME: utilisation d'un fichier properties
    private static final String REST_URI_HELLO = "http://localhost:8082/rest/hello";
    private static final String REST_URI_CHAT = "http://localhost:8082/rest/chatcanal";
    private static final String WS_URI = "ws://localhost:8082/ws/multichat";

    public static void main(String[] args) {
        PrintStream out = new PrintStream(System.out, true, UTF_8);
        out.println("Client REST");
        Client restclient = ClientBuilder.newClient();
        
        String rep = restclient.target(REST_URI_HELLO).request(MediaType.TEXT_PLAIN_TYPE).get(String.class);
        out.println(rep);
        
        ChatCanalDesc canal_res = restclient.target(REST_URI_CHAT).path(String.valueOf(1))
                .request(MediaType.APPLICATION_JSON).get(ChatCanalDesc.class);
        out.println(canal_res);
        
        List<ChatCanalDesc> list_canal = restclient.target(REST_URI_CHAT + "/all").request(MediaType.APPLICATION_JSON).get(new GenericType<List<ChatCanalDesc>>(){});
        out.println(list_canal);
        
        restclient.close();
        out.println("Fin connexion...");
        
        out.println("Connexion WebSocket :");
        Scanner scan = new Scanner(System.in);
        out.println("Bienvenu sur Multichat, choisi ton canal ");
		int num_canal = scan.nextInt();
		scan.nextLine();

		out.println("Bienvenue sur le canal" + num_canal + ". Donne ton pseudo : ");
		String pseudo = scan.nextLine();

        try {
            final ChatClientEndPoint clientendpoint = new ChatClientEndPoint(URI.create(WS_URI + "/" + num_canal + ":" + pseudo));
            String blabla;
            clientendpoint.getSession().getUserProperties().put("Pseudo", pseudo);
            do {
                out.print("Message : ");
                blabla = scan.nextLine();
                clientendpoint.getSession().getBasicRemote().sendText(formatMessage(pseudo, blabla, num_canal));
            } while (!blabla.equalsIgnoreCase("quit"));
            clientendpoint.getSession().close();
        } catch (DeploymentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            scan.close();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.exit(0);
    }

    private static Gson gson = new Gson();

    private static String formatMessage(String pseu, String bla, Integer numCanal) {
        ChatMessage m = new ChatMessage();
        m.setCanalId(numCanal);
        m.setLePseudo(pseu);
        m.setLeContenu(bla);
        return gson.toJson(m);
    }
}

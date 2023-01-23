package fr.ensma.a3.ia.mymultichat.wsock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import fr.ensma.a3.ia.mymultichat.business.canalchat.ChatCanalDesc;
import fr.ensma.a3.ia.mymultichat.business.canalchat.GestionCanaux;
import fr.ensma.a3.ia.mymultichat.business.messages.ChatMessage;
import fr.ensma.a3.ia.mymultichat.wsock.coder.ChatMessageDecoder;
import fr.ensma.a3.ia.mymultichat.wsock.coder.ChatMessageEncoder;

/**
 * ChatServerEndPoint
 */
@ServerEndpoint(value = "/ws/multichat/{canalandpseudo}", encoders = ChatMessageEncoder.class, decoders = ChatMessageDecoder.class)
public class ChatServerEndPoint {

	static List<Set<Session>> canaux = new ArrayList<Set<Session>>();

	// créer une liste qui fait le lien entre l'index de la liste canaux et les id
	// des canaux
	static List<Integer> idCanal = new ArrayList<>();

	public ChatServerEndPoint() {

	}

	@OnOpen
	public void onOpen(@PathParam("canalandpseudo") String canalandpseudo, Session sess,
			EndpointConfig endpointConfig) {
		
		GestionCanaux gestion_canaux = new GestionCanaux();
		List<ChatCanalDesc> listcanaux = gestion_canaux.getListCanal();
		List<Integer> listeidcanaux = new ArrayList<>();
		for (ChatCanalDesc chatCanalDesc : listcanaux) {
			listeidcanaux.add(chatCanalDesc.getCanalId());
		}
		
		String[] params = canalandpseudo.split(":");
		sess.getUserProperties().put("pseudo", params[1]);
		sess.getUserProperties().put("canal", params[0]);

		System.out.println(sess);
		System.out.println(params[1] + " vient de se connecter au canal " + params[0]);

		if (!listeidcanaux.contains(Integer.valueOf(params[0]))) {
			ChatMessage mess = new ChatMessage();

			try {
				mess.setLePseudo("LeServer");
				mess.setLeContenu("Ce canal n'existe pas");
				sess.getBasicRemote().sendObject(mess);
				sess.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (EncodeException e) {
				e.printStackTrace();
			}
		}
		else if (!idCanal.contains(Integer.valueOf(params[0]))) {
			canaux.add(Collections.synchronizedSet(new HashSet<Session>()));
			idCanal.add(Integer.valueOf(params[0]));
			canaux.get(idCanal.indexOf(Integer.valueOf(params[0]))).add(sess);
		}
		else {
			canaux.get(idCanal.indexOf(Integer.valueOf(params[0]))).add(sess);
		}
		
		

		

		System.out.println(canaux);
	}

	// Réaction du serveur à la réception d'un message.
	@OnMessage
	public void onMessage(ChatMessage mess, Session sess) {
		for (Session client : canaux.get(idCanal.indexOf(mess.getCanalId()))) {
			if (!sess.getId().equals(client.getId())) {
				try {
					client.getBasicRemote().sendObject(mess);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (EncodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@OnClose
	public void onClose(Session sess) {
		Integer idcanal = Integer.valueOf((String) sess.getUserProperties().get("canal"));
		String pseudo = (String) sess.getUserProperties().get("pseudo");
		
		System.out.println(pseudo + " vient de se déconnecter ...");
		
		try {
			sess.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		canaux.get(idCanal.indexOf(idcanal)).remove(sess);
		System.out.println(canaux);
		ChatMessage mess = new ChatMessage();
		for (Session client : canaux.get(idCanal.indexOf(idcanal))) {
			mess.setLePseudo("LeServer");
			mess.setLeContenu(pseudo + " nous a quitté ... (sniff)");
			try {
				client.getBasicRemote().sendObject(mess);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EncodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@OnError
	public void onError(Session session, Throwable t) {
		t.printStackTrace();
	}
}

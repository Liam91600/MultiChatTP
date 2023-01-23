package fr.ensma.a3.ia.mymultichat.wsock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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

import fr.ensma.a3.ia.mymultichat.business.chaudfroid.JeuChaudFroid;
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

	// on instancie une liste de jeu du chaud froid, contenant un jeu par canal
	static List<JeuChaudFroid> jeuxChaudFroid = new ArrayList<>();

	public ChatServerEndPoint() {

	}

	@OnOpen
	public void onOpen(@PathParam("canalandpseudo") String canalandpseudo, Session sess,
			EndpointConfig endpointConfig) {

		String[] params = canalandpseudo.split(":");
		sess.getUserProperties().put("pseudo", params[1]);
		sess.getUserProperties().put("canal", params[0]);

		System.out.println(params[1] + " vient de se connecter au canal " + params[0]);

		if (!idCanal.contains(Integer.valueOf(params[0]))) { // si le canal n'existe pas
			canaux.add(Collections.synchronizedSet(new HashSet<Session>())); // on créer un canal
			idCanal.add(Integer.valueOf(params[0])); // on ajoute son id dans la liste
			jeuxChaudFroid.add(new JeuChaudFroid(100, sess.getId())); // on créer un jeu chaud froid avec le joueur
																		// initial
			canaux.get(idCanal.indexOf(Integer.valueOf(params[0]))).add(sess); // on ajoute le joueur au canal

		} else {
			jeuxChaudFroid.get(idCanal.indexOf(Integer.valueOf(params[0]))).addJoueur(sess.getId()); // on ajoute le
																										// joueur a la
																										// liste du jeu
																										// chaud froid
			canaux.get(idCanal.indexOf(Integer.valueOf(params[0]))).add(sess); // on ajoute le joueur au canal existant
		}

		System.out.println(canaux);
	}

	// Réaction du serveur à la réception d'un message.
	@OnMessage
	public void onMessage(ChatMessage mess, Session sess) {

		int index_canal = idCanal.indexOf(mess.getCanalId());

		if (sess.getId().equals(jeuxChaudFroid.get(index_canal).getIdJoueurCourant())) { // si le message provient du
																							// joueur auquel c'est le
																							// tour de jouer

			String reponse_jeu = jeuxChaudFroid.get(index_canal).proposeNombre(Integer.valueOf(mess.getLeContenu()));
			try {
				ChatMessage reponse_jeu_chatmessage = new ChatMessage();
				reponse_jeu_chatmessage.setLeContenu(reponse_jeu);
				reponse_jeu_chatmessage.setLePseudo("LeServer");
				sess.getBasicRemote().sendObject(reponse_jeu_chatmessage);

			} catch (IOException e) {
				e.printStackTrace();
			} catch (EncodeException e) {
				e.printStackTrace();
			}

			for (Session client : canaux.get(index_canal)) { // la valeur est montrée aux autres joueurs
				if (!sess.getId().equals(client.getId())) {
					try {
						client.getBasicRemote().sendObject(mess);

					} catch (IOException e) {
						e.printStackTrace();
					} catch (EncodeException e) {
						e.printStackTrace();
					}
				}

				if (client.getId().equals(jeuxChaudFroid.get(index_canal).getIdJoueurCourant())) {

					ChatMessage message_tour_de_jeu = new ChatMessage();
					message_tour_de_jeu.setLePseudo("LeServer");
					message_tour_de_jeu.setLeContenu("C'est à vous de jouer, proposez un nombre entier de 0 à 100 : ");
					System.out.println("\n");

					try {
						client.getBasicRemote().sendObject(message_tour_de_jeu);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (EncodeException e) {
						e.printStackTrace();
					}
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
		if (canaux.get(idCanal.indexOf(idcanal)).size() == 0) {
			canaux.remove(idCanal.indexOf(idcanal));
			idCanal.remove(idCanal.indexOf(idcanal));
		}
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

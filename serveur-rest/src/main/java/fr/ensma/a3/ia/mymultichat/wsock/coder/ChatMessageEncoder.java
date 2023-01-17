package fr.ensma.a3.ia.mymultichat.wsock.coder;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import fr.ensma.a3.ia.mymultichat.business.messages.ChatMessage;

/**
 * ChatMessageEncoder
 */
public class ChatMessageEncoder implements Encoder.Text<ChatMessage> {

    private static Gson gson = new Gson();

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * Création d'un objet Json pour l'envoie
	 */
	@Override
	public String encode(ChatMessage mess) throws EncodeException {
		return gson.toJson(mess);
	}

}

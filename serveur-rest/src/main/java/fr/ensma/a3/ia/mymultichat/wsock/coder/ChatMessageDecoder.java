package fr.ensma.a3.ia.mymultichat.wsock.coder;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;

import fr.ensma.a3.ia.mymultichat.business.messages.ChatMessage;

/**
 * ChatMessageDecoder
 */
public class ChatMessageDecoder implements Decoder.Text<ChatMessage>{

	private static Gson gson = new Gson();

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public ChatMessage decode(String mess) throws DecodeException {
		return gson.fromJson(mess, ChatMessage.class);
	}

	@Override
	public boolean willDecode(String mess) {
		// TODO Auto-generated method stub
		return true;
	}

}

package com.takeaway.assignment.gameofthree.client;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import com.takeaway.assignment.gameofthree.util.ApplicationUtils;
import com.takeaway.assignment.gameofthree.util.Constants;

/**
 * 
 * @author vaibhav
 * Client class extending {@link WebSocketClient} .
 */
public class Client extends WebSocketClient {

	private static final Draft draft = new Draft_6455();
	
	
	public Client(URI serverUri, Draft draft) {
		super(serverUri, draft, null, 999);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println(Constants.CONNECTED);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
	}

	
	@Override
	public void onMessage(String message) {
		String next = ApplicationUtils.processNextNumber(message);
		
		if(Constants.DONE_KEYWORD.equals(next)){
			this.send(Constants.DONE_KEYWORD);
			shutDownClient(Constants.SUCCESS_STATUS);
		}
		
		if(Constants.SUCCESS_STR.equalsIgnoreCase(next)){
			shutDownClient(Constants.SUCCESS_STATUS);
		}
		
		if(Constants.FAILURE_STR.equalsIgnoreCase(next)){
			shutDownClient(Constants.FAILURE_STATUS);
		}

		this.send(next);
	}

	@Override
	public void onError(Exception ex) {
		if (!(ex instanceof java.net.ConnectException)) {
			this.close();
			System.exit(Constants.FAILURE_STATUS);
		}
	}

	/**
	 * Closes the connection and exits
	 * @param exitStatus
	 */
	private void shutDownClient(int exitStatus) {
		this.close();
		System.exit(exitStatus);
	}

	
	/**
	 * Main method to start a player(client)
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(Constants.PLAYER_JOINED);

		while (true) {
			WebSocketClient client = null;
			try {
				client = new Client(new URI(Constants.ADDRESS), draft);
				synchronized (client) {
					try {
						if (client.connectBlocking()) {
							break;
						} else {
							System.out.println(Constants.COULD_NOT_CONNECT + Constants.WAIT_TIME_IN_MILLISECONDS + " ms.");
							client.wait(Constants.WAIT_TIME_IN_MILLISECONDS);
						}
					} catch (InterruptedException e) {
						System.out.println(Constants.ERROR + e.getLocalizedMessage());
					}
				}
			} catch (URISyntaxException e) {
				System.out.println(Constants.ERROR + e.getLocalizedMessage());
			}

		}
	}
}
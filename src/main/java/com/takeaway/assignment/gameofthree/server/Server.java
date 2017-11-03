package com.takeaway.assignment.gameofthree.server;

import java.net.InetSocketAddress;
import java.util.Random;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.takeaway.assignment.gameofthree.util.ApplicationUtils;
import com.takeaway.assignment.gameofthree.util.Constants;

/**
 * 
 * @author vaibhav
 * Server class extending {@link WebSocketServer} .
 *
 */
public class Server extends WebSocketServer {
	private static final Random random = new Random();

	public Server(InetSocketAddress address) {
		super(address);
	}

	/**
	 * On server open, generates a random number and sends it to client
	 */
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println(Constants.CONNECTED);

		int n = random.nextInt(Constants.MAX) + 1;

		System.out.println(Constants.INPUT_NUMBER + n);

		if (n == 1) {
			System.out.println(Constants.WINNER);
			shutDownServer(conn, Constants.SUCCESS_STATUS);
		}

		conn.send(Integer.toString(n));
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		
		// If the client closed the connection,
		// we'll shutdown the server.
		System.exit(Constants.SUCCESS_STATUS);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		
		String next = ApplicationUtils.processNextNumber(message);
		
		if(Constants.DONE_KEYWORD.equals(next)){
			conn.send(Constants.DONE_KEYWORD);
			shutDownServer(conn, Constants.SUCCESS_STATUS);
		}
		
		if(Constants.SUCCESS_STR.equalsIgnoreCase(next)){
			shutDownServer(conn, Constants.SUCCESS_STATUS);
		}
		
		if(Constants.FAILURE_STR.equalsIgnoreCase(next)){
			shutDownServer(conn, Constants.FAILURE_STATUS);
		}
		
		conn.send(next);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		System.err.println(Constants.CONNECTION_ERROR + conn.getRemoteSocketAddress() + ":" + ex);
		shutDownServer(conn, Constants.FAILURE_STATUS);
	}

	@Override
	public void onStart() {
		System.out.println(Constants.PLAYER_JOINED);
	}

	private void shutDownServer(WebSocket conn, int exitStatus) {
		conn.close();
		System.exit(exitStatus);
	}

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		WebSocketServer server = new Server(new InetSocketAddress(Constants.HOST, Constants.PORT));
		server.run();
	}
}

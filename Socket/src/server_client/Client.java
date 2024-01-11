package server_client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static int post = 1234;
	public static String host = "localhost";

	private static void startClient() throws UnknownHostException,IOException {
		Socket socket = new Socket(host, post);
		ClientProccess clientProccess = new ClientProccess(socket);
		new Thread(clientProccess).start();;
		
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		new Client().startClient();
	}
}

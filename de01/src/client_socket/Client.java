package client_socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
	public static int port = 2000;
	public static String host = "127.0.0.1";

	private static void startClient() throws UnknownHostException, IOException {
		Socket socket = new Socket(host, port);
		ClientProcess proccess = new ClientProcess(socket);
		new Thread(proccess).start();
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		new Client().startClient();
	}
}

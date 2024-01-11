package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static int PORT = 6969;
	public static String HOST = "127.0.0.1";

	private void startClient() throws UnknownHostException, IOException {
		Socket socket = new Socket(HOST, PORT);
		ClientProccess proccess = new ClientProccess(socket);
		new Thread(proccess).start();
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		new Client().startClient();
	}
}

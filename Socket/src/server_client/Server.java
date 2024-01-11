package server_client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static int port = 1234;

	private void startServer() throws IOException {
		ServerSocket serverSocket = new ServerSocket(port);

		System.out.println("Server listening...");
		while (true) {
			Socket socket = serverSocket.accept();

			ServerProccess serverProccess = new ServerProccess(socket);
			new Thread(serverProccess).start();
		}

	}

	public static void main(String[] args) throws IOException {
		new Server().startServer();
	}
}

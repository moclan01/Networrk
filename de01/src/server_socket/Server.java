package server_socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static int port = 2000;
	private static void startServer() throws IOException {
		System.out.println("Server running ...");
		ServerSocket serverSocket = new ServerSocket(port);
		
		while(true) {
			Socket socket = serverSocket.accept();
			System.out.println("Client " + socket.getInetAddress() + " has connected");
			ServerProcess proccess = new ServerProcess(socket);
			new Thread(proccess).start();
		}
	}
	public static void main(String[] args) throws IOException {
		new Server().startServer();
	}
}

package student;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static int PORT = 1234;

	private static void startServer() throws IOException {
		System.out.println("Server running ...");
		ServerSocket serverSocket = new ServerSocket(PORT);
		while(true) {
			Socket socket = serverSocket.accept();
			System.out.println("Client " + socket.getInetAddress() + " has connected");
			ServerProccess proccess = new ServerProccess(socket);
			new Thread(proccess).start(); 
		}
	}

	public static void main(String[] args) throws IOException {
		new Server().startServer();
	}
}

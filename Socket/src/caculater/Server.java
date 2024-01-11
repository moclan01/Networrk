package caculater;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static int POST = 1234;

	public static void startServer() throws IOException {
		System.out.println("Server run ...");
		ServerSocket serverSocket = new ServerSocket(POST);
		while (true) {
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

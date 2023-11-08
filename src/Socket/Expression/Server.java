package Socket.Expression;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    public static final int PORT = 4444;

    public void startServer() {
        try {
            this.serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port: " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client " + clientSocket.getInetAddress() + " has connected");
                ServerProcess serverProcess = new ServerProcess(clientSocket);
                new Thread(serverProcess).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server().startServer();
    }
}

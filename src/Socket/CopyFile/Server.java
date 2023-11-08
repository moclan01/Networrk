package Socket.CopyFile;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static int PORT = 5555;

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port: " + PORT);
            while(true) {
                Socket clientSocket = serverSocket.accept();
                ServerProcess serverProcess = new ServerProcess(clientSocket);
                new Thread(serverProcess).start();
                System.out.println("Client " + clientSocket.getInetAddress() + " connected");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server().startServer();
    }
}

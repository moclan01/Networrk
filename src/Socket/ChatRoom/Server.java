package Socket.ChatRoom;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static int PORT = 5555;
    private List<ServerProcess> processes = new ArrayList<>();
    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Listening on port " + PORT);
            while(true) {
                Socket clientSocket = serverSocket.accept();
                ServerProcess serverProcess = new ServerProcess(this, clientSocket);
                processes.add(serverProcess);
                new Thread(serverProcess).start();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendAll(String id, String message) {
        for (ServerProcess serverProcess : processes) {
            if(!id.equals(serverProcess.getId()))
                serverProcess.sendMessage(message);
        }
    }

    public static void main(String[] args) {
        new Server().startServer();
    }
}

package Socket.CopyFile;

import java.net.Socket;

public class Client {
    public static int PORT = 5555;
    public static String HOST = "localhost";

    public void startClient() {
        try {
            Socket socket = new Socket(HOST, PORT);
            ClientProcess clientProcess = new ClientProcess(socket);
            new Thread(clientProcess).start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client().startClient();
    }
}

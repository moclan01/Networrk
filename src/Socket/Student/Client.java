package Socket.Student;

import java.net.Socket;

public class Client {
    private Socket socket;
    public static final String HOST = "localhost";
    public static final int PORT = 3333;

    public void startClient() {
        try {
            this.socket = new Socket(HOST, PORT);
            ClientProcess clientProcess = new ClientProcess(socket);
            new Thread( clientProcess).start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new Client().startClient();
    }

}

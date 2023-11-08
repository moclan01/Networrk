package Socket.ChatRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static int PORT = 5555;
    public static String HOST = "localhost";

    public void startClient() {
        try {
            Socket socket = new Socket(HOST, PORT);
            ClientProcess clientProcess = new ClientProcess(socket);
            new Thread(clientProcess).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

            while (true) {
                String message = reader.readLine();
                OutputStream out =  socket.getOutputStream();
                out.write(message.getBytes("UTF-8"));
                out.flush();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new Client().startClient();
    }

}

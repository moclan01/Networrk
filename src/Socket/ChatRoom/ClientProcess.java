package Socket.ChatRoom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientProcess implements Runnable {
    private Socket socket;
    private InputStream netIn;
    private OutputStream netOut;

    public ClientProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = socket.getInputStream();
        this.netOut = socket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024];
            int offset;
            while ((offset = netIn.read(buffer)) != -1) {
                String message = new String(buffer, 0, offset);
                System.out.println(message);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}

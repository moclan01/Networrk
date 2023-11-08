package Socket.ChatRoom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerProcess implements Runnable {
    private String id;
    private Server server;
    private Socket socket;
    private InputStream netIn;
    private OutputStream netOut;

    public ServerProcess(Server server, Socket socket) throws IOException {
        this.id = System.currentTimeMillis() + "";
        this.server = server;
        this.socket = socket;
        this.netIn = socket.getInputStream();
        this.netOut = socket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024];
            int offset;
            while((offset = netIn.read(buffer)) != -1) {
                String message = new String(buffer, 0, offset);
                server.sendAll(id, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            String fullMessage = "User " + id + ": " + message;
            netOut.write(fullMessage.getBytes("UTF-8"));
            netOut.flush();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return this.id;
    }

}

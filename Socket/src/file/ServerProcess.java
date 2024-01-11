package file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerProcess implements Runnable{
	private ServerSocket fileServer;
    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;

    private boolean exit;

    public ServerProcess(ServerSocket fileServer, Socket socket) throws IOException {
        this.fileServer = fileServer;
        this.socket = socket;
        this.exit = false;
        netIn = new DataInputStream(socket.getInputStream());
        netOut = new DataOutputStream(socket.getOutputStream());
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}

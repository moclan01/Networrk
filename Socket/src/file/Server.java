package file;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static final int MAIN_PORT = 2000;
    public static final int FILE_PORT = 2001;

    private ServerSocket mainServer;
    private ServerSocket fileServer;

    private void startServer() throws IOException {
        this.mainServer = new ServerSocket(MAIN_PORT);
        this.fileServer = new ServerSocket(FILE_PORT);
        while (true) {
            Socket clienSocket = mainServer.accept();
            System.out.println("CLIENT " + clienSocket.getInetAddress() + " has connected");
            ServerProcess process = new ServerProcess(fileServer, clienSocket);
            new Thread(process).start();
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().startServer();
    }
}

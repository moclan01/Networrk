package file;

import java.io.IOException;
import java.net.Socket;

public class Client {
	public static int MAIN_PORT = 2000;
    public static int FILE_PORT = 2001;
    public static String HOST = "localhost";

    private Socket socket;

    private void startClient() throws IOException {
        this.socket = new Socket(HOST, MAIN_PORT);
        ClientProcess process = new ClientProcess(socket);
        new Thread(process).start();
    }

    public static void main(String[] args) throws IOException {
        new Client().startClient();
    }
}

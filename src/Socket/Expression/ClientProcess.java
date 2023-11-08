package Socket.Expression;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientProcess implements Runnable {
    private Socket socket;
    private BufferedReader netIn;
    private PrintWriter netOut;
    private boolean exit = false;

    public ClientProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.netOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (!exit) {
                System.out.print("Enter your command: ");
                String command = reader.readLine();

                if (command.equals("quit"))
                    this.exit = true;

                netOut.println(command);
                netOut.flush();

                if (!this.exit) {
                    String serverResponse = netIn.readLine();
                    System.out.println(serverResponse);
                }
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ClientProcess [socket=" + socket + ", netIn=" + netIn + ", netOut=" + netOut + ", exit=" + exit + "]";
    }

}

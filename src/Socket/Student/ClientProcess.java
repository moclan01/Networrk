package Socket.Student;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientProcess implements Runnable {

    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;

    public ClientProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new DataInputStream(socket.getInputStream());
        this.netOut = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        boolean exit = false;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (!exit) {
                System.out.print("Enter username: ");
                String username = reader.readLine();
                System.out.print("Enter password: ");
                String password = reader.readLine();

                netOut.writeUTF(username);
                netOut.flush();
                netOut.writeUTF(password);
                netOut.flush();

                String message = netIn.readUTF();

                if (message.equalsIgnoreCase("Đăng nhập thành công")) {
                    while (true) {
                        System.out.print("Enter your command: ");
                        String command = reader.readLine();
                        if (command.equals("quit")) {
                            exit = true;
                            netOut.writeUTF(command);
                            netOut.flush();
                            break;
                        } else {
                            String[] commandSplit = command.split(" ", 2);
                            String commandName = commandSplit[0];
                            String commandValue = commandSplit[1];

                            netOut.writeUTF(commandName);
                            netOut.flush();
                            netOut.writeUTF(commandValue);
                            netOut.flush();

                            printStudentData();

                        }
                    }
                } else {
                    System.out.println(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printStudentData() throws IOException {
        String message = netIn.readUTF();
        if(message.equals("valid")) {
            int size = netIn.readInt();
            for (int i = 0; i < size; i++) {
                System.out.println(
                        "Name = " + netIn.readUTF() + " Age = " + netIn.readInt() + " Score = " + netIn.readDouble());
            }
        } else {
            System.out.println(message);
        }
    }

}

package Socket.Expression;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerProcess implements Runnable {

    private Socket socket;
    private BufferedReader netIn;
    private PrintWriter netOut;

    public ServerProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.netOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String command = netIn.readLine();
                if(command.equals("quit")) {
                    System.out.println("Client" + socket.getInetAddress() + " has disconnected");
                    break;
                }
                commandHandler(command);
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void commandHandler(String command) {
        String[] commandSplit = command.split(" ");
        String expression = commandSplit[1];
        String sendToClient = "";
        switch (expression) {
            case "+":
                caculate(command, "+");
                break;

            case "-":
                caculate(command, "-");
                break;
            case "*":
                caculate(command, "*");
                break;
            case "/":
                caculate(command, "/");
                break;

            default:
                sendToClient = "command khong hop le";
                netOut.println(sendToClient);
                netOut.flush();
                break;
        }
    }

    public void caculate(String command, String expression) {
        String sendToClient = "";
        String[] commandSplit = command.split(" ");
        int number1 = Integer.parseInt(commandSplit[0]);
        int number2 = Integer.parseInt(commandSplit[2]);

        int result;
        if(expression.equals("+"))
            result = number1 + number2;
        else if(expression.equals("-"))
            result = number1 - number2;
        else if(expression.equals("*"))
            result = number1 * number2;
        else result = number1 / number2;
        sendToClient = command + " = " + result;
        netOut.println(sendToClient);
        netOut.flush();
    }

}

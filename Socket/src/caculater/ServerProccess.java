package caculater;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerProccess implements Runnable {
	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private boolean exit;

	public ServerProccess(Socket socket) throws IOException {
		this.socket = socket;
		netIn = new DataInputStream(socket.getInputStream());
		netOut = new DataOutputStream(socket.getOutputStream());
		exit = false;
	}

	private String commandHandler(String command) {
		String result = command;
		result += " = ";
		String[] commandSplit = command.split(" ");
		int firstNumber = Integer.parseInt(commandSplit[0]);
		String subtend = commandSplit[1];
		int secondNumber = Integer.parseInt(commandSplit[2]);

		switch (subtend) {
		case "+": {
			result += (firstNumber + secondNumber);
			break;
		}
		case "-": {
			result += (firstNumber - secondNumber);
			break;
		}
		case "*": {
			result += (firstNumber * secondNumber);
			break;
		}
		case "/": {
			if (secondNumber == 0) {
				System.out.println("phep tinh khong hop le");
			} else {
				result += (firstNumber / secondNumber);
			}
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + subtend);
		}
		return result;
	}

	@Override
	public void run() {
		try {
			while (!exit) {

				String command = netIn.readUTF();
				if (command.equalsIgnoreCase("exit")) {
					exit = true;
					System.out.println("Client " + socket.getInetAddress() + " has disconnected");
				} else {
					String result = commandHandler(command);
					netOut.writeUTF(result);
					netOut.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

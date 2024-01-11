package caculater;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class ClientProccess implements Runnable {
	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private boolean exit;

	public ClientProccess(Socket socket) throws IOException {
		this.socket = socket;
		netIn = new DataInputStream(socket.getInputStream());
		netOut = new DataOutputStream(socket.getOutputStream());
		exit = false;
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (!exit) {
				System.out.print("enter your command: ");
				String command = reader.readLine();

				if (command.equalsIgnoreCase("exit")) {
					exit = true;
				} else {
					netOut.writeUTF(command);
					netOut.flush();

					String respone = netIn.readUTF();
					System.out.println(respone);
				}
			}
			reader.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

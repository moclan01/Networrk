package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ClientProccess implements Runnable {
	private static String commandFalse = "command khong hop le";

	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private boolean exit;
	private boolean isLogin;

	public ClientProccess(Socket socket) throws IOException {
		this.socket = socket;
		this.netIn = new DataInputStream(socket.getInputStream());
		this.netOut = new DataOutputStream(socket.getOutputStream());
		this.exit = false;
		this.isLogin = false;
	}

	@Override
	public void run() {
		try {
			String welcome = netIn.readUTF();
			System.out.println(welcome);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (!exit) {
				if (!isLogin) {
					String userCommand = reader.readLine();
					StringTokenizer userToken = new StringTokenizer(userCommand, "\t");
					String userCommandName = userToken.nextToken();
					if (userCommandName.startsWith("USER")) {
						netOut.writeUTF(userCommand);
						netOut.flush();

						String response = netIn.readUTF();
						System.out.println(response);
						if (response.equals("OK")) {
							String passwordCommand = reader.readLine();
							if (passwordCommand.startsWith("PASS")) {
								netOut.writeUTF(passwordCommand);
								netOut.flush();

								String loginResponse = netIn.readUTF();
								System.out.println(loginResponse);
								if (loginResponse.equals("OK")) {
									isLogin = true;
									System.out.println("login successfuly");
								}
							} else {
								System.out.println("Command khong hop le");
							}
						}
					} else if (userCommand.equals("EXIT")) {
						netOut.writeUTF(userCommand);
						netOut.flush();

						exit = true;
					}
				} else {
					// xu ly truy van
					String command = reader.readLine();
					StringTokenizer token = new StringTokenizer(command, "\t");
					int countTokens = token.countTokens();
					String commandName = token.nextToken();
					switch (commandName) {
					case "ADD": {
						if (countTokens == 5) {
							netOut.writeUTF(command);
							netOut.flush();

							String response = netIn.readUTF();
							System.out.println(response);
						} else {
							System.out.println(commandFalse);
						}
						break;
					}
					case "REMOVE": {
						if (countTokens > 1) {
							netOut.writeUTF(command);
							netOut.flush();

							String response = netIn.readUTF();
							System.out.println(response);
						} else {
							System.out.println(commandFalse);
						}
						break;
					}
					case "EDIT": {
						if (countTokens == 5) {
							netOut.writeUTF(command);
							netOut.flush();

							String response = netIn.readUTF();
							System.out.println(response);
						} else {
							System.out.println(commandFalse);
						}
						break;
					}
					case "VIEW": {
						if (countTokens == 2) {
							netOut.writeUTF(command);
							netOut.flush();

							int length = netIn.readInt();
							for(int i = 0; i < length; i++) {
                                String line = netIn.readUTF();
                                System.out.println(line);
                            }
						} else {
							System.out.println(commandFalse);
						}
						break;
					}
					case "QUIT": {
						if (countTokens == 1) {
							netOut.writeUTF(command);
							netOut.flush();

							isLogin = false;
						} else {
							System.out.println(commandFalse);
						}
						break;
					}
					default:
						System.out.println(commandFalse);
					}
				}
			}
			reader.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class ServerProccess implements Runnable {
	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private boolean exit;
	private boolean clientIsLogin;
	private Service service;

	public ServerProccess(Socket socket) throws IOException {
		this.socket = socket;
		this.netIn = new DataInputStream(socket.getInputStream());
		this.netOut = new DataOutputStream(socket.getOutputStream());
		this.exit = false;
		this.clientIsLogin = false;
		service = new Service();
	}

	@Override
	public void run() {
		try {
			netOut.writeUTF("WELCOME TO MANAGE PRODUCT SYSTEM");
			netOut.flush();
			while (!exit) {
				if (!clientIsLogin) {
					// xu ly dang nhap
					String userCommand = netIn.readUTF();
					StringTokenizer userToken = new StringTokenizer(userCommand, "\t");
					userToken.nextToken();
					if (userCommand.startsWith("USER")) {
						String username = userToken.nextToken();
						try {
							boolean isExistUsername = service.isExistUsername(username);
							if (isExistUsername) {
								netOut.writeUTF("OK");
								netOut.flush();

								String passCommand = netIn.readUTF();
								StringTokenizer passToken = new StringTokenizer(passCommand, "\t");
								passToken.nextToken();
								try {
									String password = passToken.nextToken();
									boolean isValidUser = service.isValidUser(username, password);
									if (isValidUser) {
										netOut.writeUTF("OK");
										netOut.flush();

										clientIsLogin = true;
									} else {
										netOut.writeUTF("FALSE");
										netOut.flush();
									}
								} catch (ClassNotFoundException | SQLException e) {
									e.printStackTrace();
								}
							} else {
								netOut.writeUTF("FALSE");
								netOut.flush();
							}
						} catch (ClassNotFoundException | SQLException e) {
							e.printStackTrace();
						}
					} else {
						exit = true;
						System.out.println("Client " + socket.getInetAddress() + " has disconnected");
					}

				} else {
					// xu ly truy van
					String command = netIn.readUTF();
					StringTokenizer token = new StringTokenizer(command, "\t");
					String commandName = token.nextToken();
					switch (commandName) {
					case "ADD": {
						String addId = token.nextToken();
						String addName = token.nextToken();
						int addAmount = Integer.parseInt(token.nextToken());
						double addPrice = Double.parseDouble(token.nextToken());
						
						Product product = new Product(addId, addName, addAmount, addPrice);
						try {
							service.insert(product);
							netOut.writeUTF("OK");
							netOut.flush();							
						} catch (ClassNotFoundException | SQLException e) {
							netOut.writeUTF("FAIL");
							netOut.flush();
							e.printStackTrace();
						}
						break;
					}
					case "REMOVE": {
						List<String> removeIds = new ArrayList<String>();
						while (token.countTokens() > 0) {
							String removeId = token.nextToken();
							removeIds.add(removeId);
						}
						try {
							service.delete(removeIds);
							netOut.writeUTF("OK");
							netOut.flush();
						} catch (ClassNotFoundException | SQLException e) {
							netOut.writeUTF("FAIL");
							netOut.flush();
							e.printStackTrace();
						}
						break;
					}
					case "EDIT": {
						String editId = token.nextToken();
						String editName = token.nextToken();
						int editAmount = Integer.parseInt(token.nextToken());
						double editPrice = Double.parseDouble(token.nextToken());
						
						Product product = new Product(editId, editName, editAmount, editPrice);
						try {
							service.update(product);;
							netOut.writeUTF("OK");
							netOut.flush();
						} catch (ClassNotFoundException | SQLException e) {
							netOut.writeUTF("FAIL");
							netOut.flush();
							e.printStackTrace();
						}
						break;
					}
					case "VIEW": {
						String param = token.nextToken();
						try {
							String data = service.search(param);
							StringTokenizer	dataTokens = new StringTokenizer(data, "\n");
							int length = dataTokens.countTokens();
							netOut.writeInt(length);
							netOut.flush();
							
							for(int i = 0; i< length ; i++) {
								String line = dataTokens.nextToken();
								netOut.writeUTF(line);
								netOut.flush();
							}
						} catch (ClassNotFoundException | SQLException e) {
							e.printStackTrace();
						}
						break;
					}
					case "QUIT": {
						clientIsLogin = false;
						break;
					}
					default:
						throw new IllegalArgumentException("Unexpected value: " + commandName);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String name = "Nguyen Binh";
		StringTokenizer token = new StringTokenizer(name, " ");
		String result = token.nextToken();
		System.out.println(result);
	}
}

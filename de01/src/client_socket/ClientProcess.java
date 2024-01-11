package client_socket;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

import server_socket.Candidate;

public class ClientProcess implements Runnable {

	private static String COMMAND_FALSE = "Command khong hop le";

	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private boolean isExit;
	private boolean isRegister;
	private boolean isUpload;
	private int id;

	public ClientProcess(Socket socket) throws IOException {
		this.socket = socket;
		netIn = new DataInputStream(socket.getInputStream());
		netOut = new DataOutputStream(socket.getOutputStream());
		this.isExit = false;
		this.isRegister = false;
		this.isUpload = false;
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (!isExit) {
				String command = reader.readLine();

				StringTokenizer token = new StringTokenizer(command, "\t");
				int countTokens = token.countTokens();
				String commandName = token.nextToken();

				switch (commandName) {
				case "REGISTER": {
					if (countTokens == 4) {
						if (!isRegister) {
							// send command to server
							netOut.writeUTF(command);
							netOut.flush();

							// receive response from server
							String response = netIn.readUTF();
							System.out.println(response);
							if (response.equals("success")) {
								int id = netIn.readInt();
								System.out.println("ma so du thi: " + id);
								isRegister = true;
							}
						} else {
							System.out.println("da dang ky roi");
						}
					} else {
						System.out.println(COMMAND_FALSE);
					}
					break;
				}
				case "FOTO": {
					if (countTokens == 2) {
						if (isRegister) {
							if (!isUpload) {
								// send command to server
								netOut.writeUTF(command);
								netOut.flush();

								// upload
								String path = token.nextToken();
								File file = new File(path);
								long size = file.length();

								netOut.writeLong(size);
								netOut.flush();

								// receive response from server
								String canUploadFile = netIn.readUTF();
								System.out.println(canUploadFile);
								if (canUploadFile.equals("success")) {
									BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
									byte[] buffer = new byte[100000];
									bis.read(buffer);
									netOut.write(buffer);
									netOut.flush();
									bis.close();

									netOut.writeInt(this.id);
									netOut.flush();

									String response = netIn.readUTF();
									System.out.println(response);
									if (response.endsWith("OK")) {
										isUpload = true;
									} else {
										System.out.println("file co kich thuoc qua lon");
									}
								}
							} else {
								System.out.println("khong duoc upload lai");
							}
						} else {
							System.out.println("ban can dang nhap");
						}
					} else {
						System.out.println(COMMAND_FALSE);
					}
				}
				case "VIEW": {
					if (countTokens == 2) {
						netOut.writeUTF(command);
						netOut.flush();

						String response = netIn.readUTF();
						System.out.println(response);
						if (response.equals("success")) {
							String infoCandidate = netIn.readUTF();
							System.out.println(infoCandidate);
						}
					} else {
						System.out.println(COMMAND_FALSE);
					}
					break;
				}
				case "UPDATE": {
					if (countTokens == 3) {
						netOut.writeUTF(command);
						netOut.flush();

						String response = netIn.readUTF();
						System.out.println(response);
					} else {
						System.out.println(COMMAND_FALSE);
					}
					break;
				}
				case "QUIT": {
					netOut.writeUTF(command);
					netOut.flush();

					isExit = true;
					break;
				}
				default:
					System.out.println(COMMAND_FALSE);
				}
			}
			reader.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

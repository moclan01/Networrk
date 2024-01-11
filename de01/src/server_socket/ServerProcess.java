package server_socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerProcess implements Runnable {

	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private boolean isExit;
	private Service service;

	public ServerProcess(Socket socket) throws IOException {
		this.socket = socket;
		netIn = new DataInputStream(socket.getInputStream());
		netOut = new DataOutputStream(socket.getOutputStream());
		isExit = false;
		service = Service.getInstance();
	}

	@Override
	public void run() {
		try {
			while (!isExit) {
				String command = netIn.readUTF();
				StringTokenizer token = new StringTokenizer(command, "\t");
				String commandName = token.nextToken();
				System.out.println(command);
				switch (commandName) {
				case "REGISTER": {
					String registerName = token.nextToken();
					String registerBirthday = token.nextToken();
					String registerAddress = token.nextToken();

					Candidate candidate = new Candidate(registerName, registerBirthday, registerAddress);
					if (service.checkBirthday(candidate)) {
						service.writeCandidate(candidate);

						// send response to client
						netOut.writeUTF("success");
						netOut.flush();
						netOut.writeInt(candidate.getId());
						netOut.flush();
					} else {
						netOut.writeUTF("tuoi khong phu hop");
						netOut.flush();
					}
					break;
				}
				case "FOTO": {
					long size = netIn.readLong();
					if (size <= 100000) {
						netOut.writeUTF("success");
						netOut.flush();

						byte[] buffer = new byte[100000];
						netIn.read(buffer);
						int id = netIn.readInt();
						service.writeFile(id, buffer);
						netOut.writeUTF("OK");
						netOut.flush();
					} else {
						netOut.writeUTF("error");
						netOut.flush();
					}
				}
				case "VIEW": {
					int viewId = Integer.parseInt(token.nextToken());
					if (service.isExist(viewId)) {
						netOut.writeUTF("success");
						netOut.flush();

						netOut.writeUTF(service.getUserData(viewId));
						netOut.flush();
					} else {
						netOut.writeUTF("thi sinh khong ton tai");
						netOut.flush();
					}
					break;
				}
				case "UPDATE": {
					int upId = Integer.parseInt(token.nextToken());
					String upAddress = token.nextToken();
					if (service.isExist(upId)) {
						service.update(upId, upAddress);

						netOut.writeUTF("success");
						netOut.flush();
					} else {
						netOut.writeUTF("thi sinh khong ton tai");
						netOut.flush();
					}
					break;
				}
				case "QUIT": {
					System.out.println("Client " + socket.getInetAddress() + " has disconnected");
					this.isExit = true;
					break;

				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + commandName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

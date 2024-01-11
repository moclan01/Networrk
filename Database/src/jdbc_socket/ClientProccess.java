package jdbc_socket;

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

	private void queryStudent() throws IOException {
		List<Student> reuslt = new ArrayList<Student>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter your command: ");
		String command = reader.readLine();
		StringTokenizer token = new StringTokenizer(command, " ");
		String commandName = token.nextToken();
		String commandValue = token.nextToken();

		switch (commandName) {
		case "findByAge": {
			int age = Integer.parseInt(commandValue);
			findByAge(age);
			break;
		}
		case "findByName": {
			String name = splitName(command);
			findByName(name);
			break;
		}
		case "findByScore": {
			double score = Double.parseDouble(commandValue);
			findByScore(score);
			break;
		}
		default:
			break;
		}

//		reader.close();
	}

	private void findByScore(double score) throws IOException {
		String commnadName = "findByScore";
		netOut.writeUTF(commnadName);
		netOut.flush();
		netOut.writeDouble(score);
		netOut.flush();

		reciveFromServer();

	}

	private void findByAge(int age) throws IOException {
		// giao tiep voi server
		String commandName = "findByAge";
		netOut.writeUTF(commandName);
		netOut.flush();
		netOut.writeInt(age);
		netOut.flush();

		// doi server xu ly

		// xu ly ket qua tu server
//		String respone = netIn.readUTF();
		reciveFromServer();
	}

	private void findByName(String name) throws IOException {
		String commandName = "findByName";
		netOut.writeUTF(commandName);
		netOut.flush();
		netOut.writeUTF(name);
		netOut.flush();

		reciveFromServer();
	}

	private static String splitName(String command) {
		String[] commandSplit = command.split(" ");
		String result = "";
		for (int i = 1; i < commandSplit.length; i++)
			result += commandSplit[i] + " ";
		return result.trim();
	}

	private void reciveFromServer() throws IOException {
		int size = netIn.readInt();
		for (int i = 0; i < size; i++) {
			int id = netIn.readInt();
			String name = netIn.readUTF();
			int _age = netIn.readInt();
			double score = netIn.readDouble();
			Student student = new Student(id, name, _age, score);
			System.out.println(student);
		}
	}

	private void insert() throws IOException {
		sendAndReciveServer("insert");
	}

	private void exit() throws IOException {
		netOut.writeUTF("exit");
		netOut.flush();
		this.exit = true;
	}

	private void delete() throws IOException {
		sendAndReciveServer("delete");
	}

	private void update() throws IOException {
		sendAndReciveServer("update");
	}

	private void sendAndReciveServer(String commandName) throws IOException {
		// send to server
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		netOut.writeUTF(commandName);
		netOut.flush();

		String command = reader.readLine();
		netOut.writeUTF(command);
		netOut.flush();

		// recive response from server
		String response = netIn.readUTF();
		System.out.println(response);
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (!exit) {
				System.out.println("Chon chuc nang");
				System.out.println("1. Tra cuu");
				System.out.println("2. Them sinh vien");
				System.out.println("3. Sua sinh vien");
				System.out.println("4. Xoa sinh vien");
				System.out.println("exit");

				String command = reader.readLine();
				switch (command) {
				case "1": {
					queryStudent();
					break;
				}
				case "2": {
					insert();
					break;
				}
				case "3": {
					update();
					break;
				}
				case "4": {
					delete();
					break;
				}
				case "exit": {
					exit();
					break;
				}
				default:
					System.out.println("command khong hop le");
				}
			}
			reader.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

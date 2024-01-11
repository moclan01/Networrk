package jdbc_socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ServerProccess implements Runnable {
	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private boolean exit;
	private StudentService studentService;

	public ServerProccess(Socket socket) throws IOException {
		this.socket = socket;
		netIn = new DataInputStream(socket.getInputStream());
		netOut = new DataOutputStream(socket.getOutputStream());
		exit = false;
		this.studentService = new StudentService();
	}

	private void findByAge() throws IOException, ClassNotFoundException, SQLException {
		int age = netIn.readInt();
		List<Student> students = studentService.findByAge(age);

		// gui response toi client
		sendResponeToClient(students);
	}

	private void findByName() throws IOException, ClassNotFoundException, SQLException {
		String name = netIn.readUTF();
		List<Student> students = studentService.findByName(name);

		// gui response toi client
		sendResponeToClient(students);
	}

	private void findByScore() throws IOException, ClassNotFoundException, SQLException {
		double score = netIn.readDouble();
		List<Student> students = studentService.findByScore(score);

		// gui response toi client
		sendResponeToClient(students);
	}

	private void sendResponeToClient(List<Student> students) throws IOException {
		netOut.writeInt(students.size());
		netOut.flush();
		for (Student student : students) {
			netOut.writeInt(student.getId());
			netOut.flush();
			netOut.writeUTF(student.getName());
			netOut.flush();
			netOut.writeInt(student.getAge());
			netOut.flush();
			netOut.writeDouble(student.getScore());
			netOut.flush();
		}
	}

	private void insert() throws IOException, ClassNotFoundException, SQLException {
		String command = netIn.readUTF();

		String[] commandInsert = command.split(" ");
		String name = "";
		for (int i = 0; i < commandInsert.length - 2; i++) {
			name += commandInsert[i] + " ";
		}
		name = name.trim();
		int age = Integer.parseInt(commandInsert[commandInsert.length - 2]);
		double score = Double.parseDouble(commandInsert[commandInsert.length - 1]);

		Student student = new Student(name, age, score);
		studentService.insert(student);

		String response = "Them sinh vien moi thanh cong";
		netOut.writeUTF(response);
		netOut.flush();
	}

	private void exit() throws IOException {
		this.exit = true;
		System.out.println("Client " + socket.getInetAddress() + " has disconnected");
	}

	private void delete() throws IOException, ClassNotFoundException, SQLException {
		String command = netIn.readUTF();
		int id = Integer.parseInt(command.trim());
		
		studentService.delete(id);
		
		String response = "Xoa sinh vien thanh cong";
		netOut.writeUTF(response);
		netOut.flush();
	}

	private void update() throws IOException, ClassNotFoundException, SQLException {
		String command = netIn.readUTF();
		String[] commandUpdate = command.split(" ");

		int id = Integer.parseInt(commandUpdate[0]);
		String name = "";
		for (int i = 1; i < commandUpdate.length - 2; i++) {
			name += commandUpdate[i] + " ";
		}
		name = name.trim();
		int age = Integer.parseInt(commandUpdate[commandUpdate.length - 2]);
		double score = Double.parseDouble(commandUpdate[commandUpdate.length - 1]);

		Student student = new Student(name, age, score);
		studentService.update(id, student);

		String response = "Sua sinh vien thanh cong";
		netOut.writeUTF(response);
		netOut.flush();
	}

	@Override
	public void run() {
		try {
			while (!exit) {
				String command = netIn.readUTF();
				switch (command) {
				case "findByAge": {
					findByAge();
					break;
				}
				case "findByName": {
					findByName();
					;
					break;
				}
				case "findByScore": {
					findByScore();
					;
					break;
				}
				case "insert": {
					insert();
					break;
				}
				case "update": {
					update();
					break;
				}
				case "delete": {
					delete();
					break;
				}
				case "exit": {
					exit();
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + command);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

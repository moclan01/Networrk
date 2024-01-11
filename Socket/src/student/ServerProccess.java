package student;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerProccess implements Runnable {
	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private boolean exit;
	private boolean clientIsLogin;

	List<Student> students = new ArrayList<Student>(
			Arrays.asList(
					new Student(1, "Nguyen Binh", 20, 7.0), 
					new Student(2, "Nguyen Tan Khoa", 20, 10.0),
					new Student(3, "Nguyen Phuong Nha", 21, 8.0), 
					new Student(4, "Bui Thi Thuy Trang", 22, 9.0)));
	List<User> users = new ArrayList<User>(Arrays.asList(new User("admin", "admin"), new User("moclan01", "12345")));

	public ServerProccess(Socket socket) throws IOException {
		this.socket = socket;
		netIn = new DataInputStream(socket.getInputStream());
		netOut = new DataOutputStream(socket.getOutputStream());
		exit = false;
		clientIsLogin = false;
	}

	private boolean login(String username, String password) {
		for (User user : users) {
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				return true;
			}
		}
		return false;
	}

	private static String splitName(String command) {
		String[] commandSplit = command.split(" ");
        String result = "";
        for(int i = 1; i < commandSplit.length; i++)
            result += commandSplit[i] + " ";
        return result.trim();
	}

	private List<Student> findByScore(double score) {
		List<Student> result = new ArrayList<Student>();
		for (Student student : students) {
			if (student.getAvg() == score) {
				result.add(student);
			}
		}
		return result;

	}

	private List<Student> findByAge(int age) {
		List<Student> result = new ArrayList<Student>();
		for (Student student : students) {
			if (student.getYear() == age) {
				result.add(student);
			}
		}
		return result;
	}

	private List<Student> findByName(String name) {
		List<Student> result = new ArrayList<Student>();
		for (Student student : students) {
			if (student.getName().equals(name)) {
				result.add(student);
			}
		}
		return result;
	}

	private List<Student> commandHandler(String command) {
		List<Student> result = new ArrayList<Student>();
		String[] commandSplit = command.split(" ");
		String commandName = commandSplit[0];
		String commandValue = commandSplit[1];

		switch (commandName) {
		case "findByName": {
			String name = splitName(command);
//			return findByName(name);
			result = findByName(name);
			break;
		}
		case "findByAge": {
			int age = Integer.parseInt(commandValue);
//			return findByAge(age);
			result = findByAge(age);
			break;
		}
		case "findByScore": {
			double score = Double.parseDouble(commandValue);
//			return findByScore(score);
			result = findByScore(score);
			break;
		}
		default:
			System.out.println("cu phap khong hop le");
		}
		return result;
	}

	private void sendStudentToClient(List<Student> students) throws IOException {
		netOut.writeInt(students.size());
		netOut.flush();
		for (Student student : students) {
			netOut.writeInt(student.getId());
			netOut.flush();
			netOut.writeUTF(student.getName());
			netOut.flush();
			netOut.writeInt(student.getYear());
			netOut.flush();
			netOut.writeDouble(student.getAvg());
			netOut.flush();
		}
	}

	@Override
	public void run() {
		try {
			while (!exit) {
				if (!clientIsLogin) {
					String username = netIn.readUTF();
					String password = netIn.readUTF();
					if (login(username, password)) {
						clientIsLogin = true;

						netOut.writeUTF("success");
						netOut.flush();
					} else {
						netOut.writeUTF("error");
						netOut.flush();
					}
				} else {
					String command = netIn.readUTF();
					if (command.equalsIgnoreCase("exit")) {
//						exit = true;
						clientIsLogin = false;
//						System.out.println("Client" + socket.getInetAddress() + " has disconnected");
					}else {
						List<Student> foundStudents = commandHandler(command);
						
						// send list student to client
						sendStudentToClient(foundStudents);						
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//public static void main(String[] args) throws IOException {
//	System.out.println(splitName("find Nguyen Binh"));
//	List<Student> result = findByName(splitName("findByName Nguyen Binh"));
//	for (Student student : result) {
//		System.out.println(student);
//	}
//}
}

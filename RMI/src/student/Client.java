package student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

public class Client {
	public static void main(String[] args) throws NotBoundException, IOException, ClassNotFoundException, SQLException {
		Registry registry = LocateRegistry.getRegistry("localhost", 1234);
		IServer auth = (IServer) registry.lookup("server");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String sessionId = "";
		while(true) {
			String command = reader.readLine();
			if(command.startsWith("user")) {
				//xu ly dang nhap
				StringTokenizer userToken = new StringTokenizer(command, " ");
				userToken.nextToken();
				String username = userToken.nextToken();
				String passCommand = reader.readLine();
				if(passCommand.startsWith("pass")) {
					StringTokenizer passToken = new StringTokenizer(passCommand, " ");
					passToken.nextToken();
					String password = passToken.nextToken();
					sessionId = auth.login(username, password);
					if(!sessionId.equals(""))
						System.out.println("login successfully");
				}else {
					System.out.println("command khong hop le");
				}
			}else {
				//xu ly truy van
				if(sessionId.equals("") ) {
					System.out.println("Chua dang nhap, khong duoc truy van");
				}else {
					StringTokenizer token = new StringTokenizer(command, " ");
					String commandName = token.nextToken();
					switch (commandName) {
					case "findByAge": {
						int age = Integer.parseInt(token.nextToken());
						List<Student> students = auth.findByAge(age, sessionId);
						if(students.size() > 0) {
							System.out.println(students);
						}else {
							System.out.println("khong tim thay");
						}
						break;
					}
					default:
						System.out.println("command khong hop le");
					}
				}
			}
		}
	}
}

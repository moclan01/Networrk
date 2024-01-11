package student;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientProccess implements Runnable {
	private Socket socket;
	private DataInputStream netIn;
	private DataOutputStream netOut;
	private boolean exit;
	private boolean isLogin;

	public ClientProccess(Socket socket) throws IOException { 
		this.socket = socket;
		netIn = new DataInputStream(socket.getInputStream());
		netOut = new DataOutputStream(socket.getOutputStream());
		exit = false;
		isLogin = false;
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while(!exit) {
				if(!isLogin) {
					System.out.print("user: ");
					String username =  reader.readLine();
					System.out.print("pass: ");
					String password = reader.readLine();
					
					netOut.writeUTF(username);
					netOut.flush();
					netOut.writeUTF(password);
					netOut.flush();
					
					String loginRespone = netIn.readUTF();
					if(loginRespone.equalsIgnoreCase("success")) {
						isLogin = true;
						System.out.println("login successfully");
					}
					
				}else {
					//xu ly tra cuu
					System.out.print("Enter your command: ");
					String commnad = reader.readLine();
					//send command to server
					if(commnad.equalsIgnoreCase("exit")) {
//						exit = true;
						isLogin = false;
						System.out.println("sign out");
					}else {						
						netOut.writeUTF(commnad);
						netOut.flush();	
						
						List<Student> students = reciveServerRespone();
						printStudent(students);
					}
				}
				
				
			}
			reader.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private List<Student> reciveServerRespone() throws IOException {
		List<Student> result = new ArrayList<Student>();
		int size = netIn.readInt();
		for(int i = 0; i< size; i++) {
			int id = netIn.readInt();
			String name = netIn.readUTF();
			int year = netIn.readInt();
			double score = netIn.readDouble();
			Student student = new Student(id, name, year, score);
			result.add(student);
		}
		return result;
	}
	
	private void printStudent(List<Student> students) {
		for (Student student : students) {
			System.out.println(student);
		}
	}

}

package student;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServerImpl extends UnicastRemoteObject implements IServer {
	Map<String, User> session = new HashMap<String, User>();
	StudentService studentService = new StudentService();
	UserService userService = new UserService();

	protected ServerImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String login(String username, String password) throws RemoteException, ClassNotFoundException, SQLException {
		String sessionId = "";
		User loginUser = userService.login(username, password);
		if (loginUser != null) {
			sessionId = UUID.randomUUID().toString();
			session.put(sessionId, loginUser);
			return sessionId;
		}
		return null;
	}

	@Override
	public List<Student> findByAge(int age, String sessionId) throws RemoteException, ClassNotFoundException, SQLException {
		if(session.containsKey(sessionId)) {
			List<Student> students = studentService.findByAge(age);
			return students;
		}
		return null;
	}

	@Override
	public String logout(String sessionId) throws RemoteException, ClassNotFoundException, SQLException {
		if(session.containsKey(sessionId)) {
			session.remove(sessionId);
			return "dang xuat thanh cong";
		}
		return "SessionId khong hop le";
	}

}

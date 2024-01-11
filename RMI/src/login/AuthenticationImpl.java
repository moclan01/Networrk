package login;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AuthenticationImpl extends UnicastRemoteObject implements IAuthentication {
	private Map<String, User> session = new HashMap<String, User>();

	protected AuthenticationImpl() throws RemoteException {
		super();
	}

	private List<User> users = new ArrayList<User>(
			Arrays.asList(new User("1", "admin", "admin123"), new User("2", "abc", "abc")));

	@Override
	public String login(String username, String password) throws RemoteException {
		String sessionId = "";
		for (User user : users) {
			if(username.equals(user.getUsername()) && password.equals(user.getPassword())) {
				sessionId = UUID.randomUUID().toString();
				session.put(sessionId, user);
				break;
			}
		}
		return sessionId;
	}

	@Override
	public User getUserData(String sessionId) throws RemoteException {
		if(session.containsKey(sessionId)) {
			return session.get(sessionId);
		}
		return null;
	}

}

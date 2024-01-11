package student;

import java.sql.SQLException;

public class UserService {
	Database database = new Database();
	
	public User login(String username, String password) throws ClassNotFoundException, SQLException {
		return database.login(username, password);
	}
}

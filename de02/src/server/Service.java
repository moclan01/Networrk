package server;

import java.sql.SQLException;
import java.util.List;

public class Service {
	private DAO database = new DAO();

	public boolean isExistUsername(String username) throws ClassNotFoundException, SQLException {
		return database.isExistUsername(username);
	}

	public boolean isValidUser(String username, String password) throws ClassNotFoundException, SQLException {
		return database.isValidUser(username, password);
	}

	public void insert(Product product) throws ClassNotFoundException, SQLException {
		database.insert(product);
	}

	public int delete(List<String> ids) throws ClassNotFoundException, SQLException {
		int sum = 0;
		for (String id : ids) {
			sum += database.delete(id);
		}
		return sum;
	}

	public void update(Product product) throws ClassNotFoundException, SQLException {
		database.update(product);
	}

	public String search(String search) throws ClassNotFoundException, SQLException {
		return database.search(search);
	}
}

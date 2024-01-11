package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Database {
	private static String DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
	private static String URL = "jdbc:ucanaccess://resources/SinhVien.accdb";
	Connection connection;
	
	public void createConnection() {
		try {
			//setting accept
			Class.forName(DRIVER);
			this.connection = DriverManager.getConnection(URL);
			
			System.out.println("accept successfully");
			List<User> users = new ArrayList<User>();
			
			//query
			String sql = "SELECT * FROM USER";
			PreparedStatement stmt = connection.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("ID");
				String username = rs.getString("username");
				String password = rs.getString("password");
				
				User user = new User(id, username, password);
				users.add(user);
			}
			
			rs.close();
			stmt.close();
			connection.close();
			
			System.out.println(users);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

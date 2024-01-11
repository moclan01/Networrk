package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ConnectionJDBC {
	public static void connection() {
		try {
			//setting accept
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			String url = "jdbc:ucanaccess://resources/SinhVien.accdb";
			Connection conn = DriverManager.getConnection(url);
			
			System.out.println("accept successfully");
			List<User> users = new ArrayList<User>();
			
			//query
			String sql = "SELECT * FROM USER";
			PreparedStatement stmt = conn.prepareStatement(sql);
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
			conn.close();
			
			System.out.println(users);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new ConnectionJDBC().connection();
	}
}

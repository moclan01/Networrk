package databaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Database {
	public Connection createConection() {
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			Connection connect = DriverManager.getConnection("jdbc:ucanaccess://D:/Projects/Java/Network/SinhVien.accdb");
			return connect;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Student> findAll(){
		String sql = "select * from Student";
		Connection conn = createConection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Student> result = new ArrayList<Student>();
		try {
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("Name");
				int age = rs.getInt("Age");
				int score = rs.getInt("Score");
				result.add(new Student(id, name, age, score));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
				stmt.close();
				rs.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return result.size() > 0 ? result : null;
	}
	public Student findByName(String ten) {
		String sql = "select * from Student where Name = ?";
		Connection conn = createConection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Student st = null;
		try {
			stmt = conn.prepareStatement(sql);

			stmt.setString(1, ten);
			rs = stmt.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("Name");
				int age = rs.getInt("Age");
				int score = rs.getInt("Score");
				st = new Student(id, name, age, score);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
				stmt.close();
				rs.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		return st;
	}
	
}

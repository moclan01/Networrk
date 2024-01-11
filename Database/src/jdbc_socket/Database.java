package jdbc_socket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
	private Connection conection;
	private static String DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
	private static String URL = "jdbc:ucanaccess://resources/SinhVien.accdb";

	public void OpenConnection() throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER);
		this.conection = DriverManager.getConnection(URL);
	}

	public List<Student> findBy(String column, Object value) throws ClassNotFoundException, SQLException {
		OpenConnection();
		List<Student> students = new ArrayList<Student>();

		String sql = "SELECT * FROM STUDENT WHERE " + column + " = ?";
		PreparedStatement stmt = conection.prepareStatement(sql);
		stmt.setObject(1, value);

		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			int id = rs.getInt("ID");
			String name = rs.getString("Name");
			int age = rs.getInt("Age");
			double score = rs.getDouble("Score");

			Student student = new Student(id, name, age, score);
			students.add(student);
		}
		rs.close();
		stmt.close();
		conection.close();
		return students;
	}

	public List<Student> findByAge(int age) throws ClassNotFoundException, SQLException {
		return findBy("Age", age);
	}

	public List<Student> findByName(String name) throws ClassNotFoundException, SQLException {
		return findBy("Name", name);
	}

	public List<Student> findByScore(double score) throws ClassNotFoundException, SQLException {
		return findBy("Score", score);
	}
	
	public void insert(Student student) throws ClassNotFoundException, SQLException {
		OpenConnection();
		String sql = "INSERT INTO STUDENT(NAME, AGE, SCORE) VALUES(?,?,?)";
		PreparedStatement stmt = conection.prepareStatement(sql);
		stmt.setObject(1, student.getName());
		stmt.setObject(2, student.getAge());
		stmt.setObject(3, student.getScore());
		
		stmt.executeUpdate();
		
		stmt.close();
		conection.close();
	}
	
	public void delete(int id) throws ClassNotFoundException, SQLException {
		OpenConnection();
		String sql = "DELETE FROM STUDENT WHERE ID = ?";
		PreparedStatement stmt = conection.prepareStatement(sql);
		stmt.setObject(1, id);
		stmt.executeUpdate();
		
		stmt.close();
		conection.close();
	}
	
	public void update(int id, Student student) throws ClassNotFoundException, SQLException {
		OpenConnection();
		String sql = "UPDATE STUDENT SET Name = ?, Age = ?, Score = ? WHERE ID = ?";
		PreparedStatement stmt = conection.prepareStatement(sql);
		stmt.setObject(1, student.getName());
		stmt.setObject(2, student.getAge());
		stmt.setObject(3, student.getScore());
		stmt.setObject(4, id);
		
		stmt.executeUpdate();
		
		stmt.close();
		conection.close();
	}
}

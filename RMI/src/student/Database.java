package student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
	private static String DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
	private static String URL = "jdbc:ucanaccess://resources/SinhVien.accdb";
	private Connection connection;

	private Connection openConnection() throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER);
		connection = DriverManager.getConnection(URL);
		return connection;
	}
	
	public List<Student> findBy(String column, Object value) throws SQLException, ClassNotFoundException {
        this.connection = openConnection();
        List<Student> students = new ArrayList<Student>();
        String sql = "SELECT * FROM STUDENTS WHERE " + column + "= ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, value);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("ID");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            double score = rs.getDouble("score");

            Student student = new Student(id, name, age, score);
            students.add(student);
        }

        rs.close();
        stmt.close();
        connection.close();
        return students;
    }

    public List<Student> findByName(String name) throws SQLException, ClassNotFoundException {
        return findBy("NAME", name);
    }

    public List<Student> findByAge(int age) throws SQLException, ClassNotFoundException {
        return findBy("AGE", age);
    }

    public List<Student> findByScore(double score) throws SQLException, ClassNotFoundException {
        return findBy("SCORE", score);
    }
    public User login(String username, String password) throws ClassNotFoundException, SQLException {
        this.connection = openConnection();
        String sql = "SELECT * FROM USERS WHERE username = ? AND password = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setObject(1, username);
        stmt.setObject(2, password);
        ResultSet rs = stmt.executeQuery();
        User user = null;
        while (rs.next()) {
            int id = rs.getInt("ID");
            String _username = rs.getString("username");
            String _password = rs.getString("password");
            user = new User(id, _username, _password);
        }

        rs.close();
        stmt.close();
        connection.close();
        return user;
    }
}

package jdbc_socket;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentService {
	private Database database = new Database();

	public List<Student> findByAge(int age) throws ClassNotFoundException, SQLException {
		return database.findByAge(age);
	}

	public List<Student> findByName(String name) throws ClassNotFoundException, SQLException {
		return database.findByName(name);
	}

	public List<Student> findByScore(double score) throws ClassNotFoundException, SQLException {
		return database.findByScore(score);
	}

	public void insert(Student student) throws ClassNotFoundException, SQLException {
		database.insert(student);
	}

	public void delete(int id) throws ClassNotFoundException, SQLException {
		database.delete(id);
	}

	public void update(int id, Student student) throws ClassNotFoundException, SQLException {
		database.update(id, student);
	}
}

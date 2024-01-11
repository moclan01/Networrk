package IO;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentToBinary {
	static List<Student> students = new ArrayList<Student>(Arrays.asList(
			new Student("Nguyen Binh", 20, 9.0),
			new Student("Pham Thi Phuong Loan", 20, 10.0),
			new Student("Nguyen Phuong Nha", 20, 9.0)
			));
	
	private static void writeStudents(String sourceFile) throws IOException {
		File file = new File(sourceFile);
		FileOutputStream fos = new FileOutputStream(file);
		DataOutputStream dos = new DataOutputStream(fos);
		
		if(!file.exists()) {
			file.createNewFile();
		}
		
		//write student
		for(Student student : students) {
			dos.writeUTF(student.getName());
			dos.writeInt(student.getAge());
			dos.writeDouble(student.getScore());
		}
		dos.close();
	}
	public static void main(String[] args) throws IOException {
		writeStudents("resources/IO/students.txt");
	}
}

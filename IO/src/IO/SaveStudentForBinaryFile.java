package IO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaveStudentForBinaryFile {
	private static List<Student> students = new ArrayList<Student>(Arrays.asList(
			new Student("Nguyễn Bính", 20, 9.0),
			new Student("Trần Thắng Lợi", 20, 10.0),
			new Student("Nguyễn Phương Nhã", 20, 10.0)
			));
	
	public static void writeStudent (String source) throws IOException {
		File file = new File(source);
		FileOutputStream fis = new FileOutputStream(file);
		if(!file.exists()) {
			file.createNewFile();
		}
		
		DataOutputStream dos = new DataOutputStream(fis);
		for(Student student : students) {
			dos.writeUTF(student.getName());
			dos.writeInt(student.getAge());
			dos.writeDouble(student.getScore());
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		writeStudent("src/IO/student.txt");
	}
}

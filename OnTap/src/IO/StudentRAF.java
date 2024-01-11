package IO;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentRAF {
	static List<Student> students = new ArrayList<Student>(Arrays.asList(new Student("Nguyen Binh", 20, 9.0),
			new Student("Pham Thi Phuong Loan", 20, 10.0), new Student("Nguyen Phuong Nha", 20, 9.0)));

	private static void writeStudents(String source) throws IOException {
		File file = new File(source);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");

		// write header
		raf.writeInt(students.size());
		List<Long> pointers = new ArrayList<Long>();
		for (Student student : students) {
			long curentPosition = raf.getFilePointer();
			pointers.add(curentPosition);
			raf.writeLong(0);
		}

		// write body
		for (int i = 0; i < students.size(); i++) {
			long currentPosition = raf.getFilePointer();
			Student student = students.get(i);
			raf.seek(pointers.get(i));
			raf.writeLong(currentPosition);
			raf.seek(currentPosition);

			// write student
			raf.writeUTF(student.getName());
			raf.writeInt(student.getAge());
			raf.writeDouble(student.getScore());
		}

		raf.close();
	}

	private static List<Student> readStudents(String source) throws IOException {
		List<Student> result = new ArrayList<Student>();
		File file = new File(source);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");

		// ?????
		int size = raf.readInt();
		List<Long> pointers = new ArrayList<Long>();
		for (int i = 0; i < size; i++) {
			pointers.add(raf.readLong());
		}

		// read students
		for (int i = 0; i < size; i++) {
			long pointer = pointers.get(i);
			raf.seek(pointer);

			String name = raf.readUTF();
			int age = raf.readInt();
			double score = raf.readDouble();
			Student student = new Student(name, age, score);
			result.add(student);
		}
		
		raf.close();
		return result;
	}

	public static void main(String[] args) throws IOException {
//		writeStudents("resources/IO/studentRAF.txt");
		List<Student> students = readStudents("resources/IO/studentRAF.txt");
		for (Student student : students) {
			System.out.println(student);
		}
		
	}
}

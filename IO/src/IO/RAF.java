package IO;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RAF {

	private static List<Student> students = new ArrayList<Student>(Arrays.asList(new Student("Nguyễn Bính", 20, 9.0),
			new Student("Trần Thắng Lợi", 20, 10.0), new Student("Nguyễn Phương Nhã", 20, 10.0)));

	public static void writeStudentRAF(String sourceFile) throws IOException {
		File file = new File(sourceFile);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		List<Long> pointers = new ArrayList<Long>();

		// write header
		raf.writeInt(students.size());
		for (Student student : students) {
			long currentPointer = raf.getFilePointer();
			pointers.add(currentPointer);
			raf.writeLong(0);
		}

		// write body
		for (int i = 0; i < students.size(); i++) {
			long currentPosition = raf.getFilePointer();
			Student student = students.get(i);
			raf.seek(pointers.get(i));
			raf.writeLong(currentPosition);
			raf.seek(currentPosition);

			raf.writeUTF(student.getName());
			raf.writeInt(student.getAge());
			raf.writeDouble(student.getScore());
		}
		raf.close();
	}

	public static Student readStudentRAF(String sourceFile, int index) throws IOException {
		File file = new File(sourceFile);
		RandomAccessFile raf = new RandomAccessFile(file, "rw");

		int size = raf.readInt();
		List<Long> pointers = new ArrayList<Long>();
		for (int i = 0; i < size; i++) {
			pointers.add(raf.readLong());
		}

		Long pointer = pointers.get(index);
		raf.seek(pointer);

		String name = raf.readUTF();
		int age = raf.readInt();
		double score = raf.readDouble();

		Student student = new Student(name, age, score);
		raf.close();
		return student;
	}

	public static void main(String[] args) throws IOException {
//		writeStudentRAF("src/IO/student.txt");
		Student student  = readStudentRAF("src/IO/student.txt", 1);
		System.out.println(student);
	}
}

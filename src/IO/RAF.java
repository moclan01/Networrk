package IO;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RAF {

    public static void writeRAF(List<Student> students) throws IOException {
        File file = new File("resources/raf.txt");
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.writeInt(students.size());

            List<Long> pointers = new ArrayList<Long>();
            for (int i = 0; i < students.size(); i++) {
                long position = raf.getFilePointer();
                pointers.add(position);
                raf.writeLong(0);
            }

            for (int i = 0; i < students.size(); i++) {
                long currentPosition = raf.getFilePointer();
                Student student = students.get(i);
                raf.seek(pointers.get(i));
                raf.writeLong(currentPosition);
                raf.seek(currentPosition);
                raf.writeInt(student.getId());
                raf.writeUTF(student.getFullName());
                raf.writeDouble(student.getGpa());
            }
        }

    }

    public static List<Student> readRAF() throws IOException {
        File file = new File("resources/raf.txt");
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            List<Student> students = new ArrayList<Student>();
            int length = raf.readInt();
            List<Long> pointers = new ArrayList<Long>();
            for(int i = 0; i < length; i++)
                pointers.add(raf.readLong());
            
            for(Long pointer : pointers) {
                raf.seek(pointer);
                students.add(new Student(raf.readInt(), raf.readUTF(), raf.readDouble()));
            }

            return students;
            
        }
    }

    public static Student getStudentByPointer(long pointer) throws IOException {
        File file = new File("resources/raf.txt");
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            List<Long> pointers = new ArrayList<Long>();
            int length = raf.readInt();
            for(int i = 0; i < length; i++)
                pointers.add(raf.readLong());
            
            if(pointers.contains(pointer)) {
                raf.seek(pointer);
                return new Student(raf.readInt(), raf.readUTF(), raf.readDouble());
            }
            else return null;
        }
    }

    public static Student getStudentBySerial(int serial) throws IOException {
        File file = new File("resources/raf.txt");
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        int length = raf.readInt();

        //Check if the serial is larger than the size of the student list
        if(serial >= length) {
            raf.close();
            return null;
        }

        List<Long> pointers = new ArrayList<Long>();
        for(int i = 0; i < length; i++)
            pointers.add(raf.readLong());
        raf.seek(pointers.get(serial));

        Student student = new Student(raf.readInt(), raf.readUTF(), raf.readDouble());
        raf.close();
        return student;

    }
    public static void main(String[] args) throws IOException {
        List<Student> students = new ArrayList<>(Arrays.asList(
            new Student(1, "Trần Thắng Lợi", 7.47),
            new Student(2, "Nguyễn Phương Nhã", 9.2),
            new Student(3, "Nguyễn Tấn Khoa", 7.5)
        ));

        // writeRAF(students);
        // System.out.println(readRAF());
        // System.out.println(getStudentByPointer(98));
        System.out.println(getStudentBySerial(1));
    }
}

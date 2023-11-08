package Socket.Student;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerProcess implements Runnable {

    private Server server;
    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;
    private boolean exit = false;

    public ServerProcess(Server server, Socket socket) throws IOException {
        this.server = server;
        this.socket = socket;
        this.netIn = new DataInputStream(socket.getInputStream());
        this.netOut = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (!exit) {
                String username = netIn.readUTF();
                String password = netIn.readUTF();

                if (isValid(username, password)) {
                    netOut.writeUTF("Đăng nhập thành công");
                    netOut.flush();

                    while (true) {
                        String commandName = netIn.readUTF();
                        if (commandName.equalsIgnoreCase("quit")) {
                            exit = true;
                            break;
                        }
                        String commandValue = netIn.readUTF();
                        commandHandler(commandName, commandValue);
                    }

                } else {
                    netOut.writeUTF("Sai tên đăng nhập hoặc mật khẩu");
                    netOut.flush();
                }
            }

            System.out.println("Client " + socket.getInetAddress() + " has disconnected");
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isValid(String username, String password) {
        return username.equals(Server.USERNAME) && password.equals(Server.PASSWORD);
    }

    public void commandHandler(String commandName, String commandValue) throws IOException {
        List<Student> students = new ArrayList<>();

        if (isValidCommand(commandName, commandValue)) {
            netOut.writeUTF("valid");
            switch (commandName) {
                case "findByName":
                    String studentName = commandValue;
                    students = findByName(studentName);
                    writeStudentData(students);
                    break;

                case "findByAge":
                    int studentAge = Integer.parseInt(commandValue);
                    students = findByAge(studentAge);
                    writeStudentData(students);
                    break;
                case "findByScore":
                    double studentScore = Double.parseDouble(commandValue);
                    students = findByScore(studentScore);
                    writeStudentData(students);
                    break;
            }
        } else {
            netOut.writeUTF("Command is invalid");
        }

    }

    public List<Student> findByName(String name) {
        List<Student> result = new ArrayList<Student>();
        List<Student> students = server.getStudents();
        for (Student student : students)
            if (student.getName().equalsIgnoreCase(name))
                result.add(student);
        return result;
    }

    public List<Student> findByAge(int age) {
        List<Student> result = new ArrayList<Student>();
        List<Student> students = server.getStudents();
        for (Student student : students)
            if (student.getAge() == age)
                result.add(student);
        return result;
    }

    public List<Student> findByScore(double score) {
        List<Student> result = new ArrayList<Student>();
        List<Student> students = server.getStudents();
        for (Student student : students)
            if (student.getScore() == score)
                result.add(student);
        return result;
    }

    public void writeStudentData(List<Student> students) throws IOException {
        netOut.writeInt(students.size());
        netOut.flush();
        for (Student student : students) {
            netOut.writeUTF(student.getName());
            netOut.flush();

            netOut.writeInt(student.getAge());
            netOut.flush();

            netOut.writeDouble(student.getScore());
            netOut.flush();
        }
    }

    public boolean isValidCommand(String commandName, String commandValue) {

        switch (commandName) {
            case "findByName":
                return Utils.isValidNonNumbericString(commandValue);
            case "findByAge":
                return Utils.isInteger(commandValue);
            case "findByScore":
                return Utils.isDouble(commandValue);

            default:
                return false;
        }
    }

}

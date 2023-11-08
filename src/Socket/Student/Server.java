package Socket.Student;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {

    public static final String USERNAME = "root";
    public static final String PASSWORD = "123";
    public static final int PORT = 3333;

    private List<Student> students = new ArrayList<Student>(Arrays.asList(
            new Student("Tran Thang Loi", 20, 7.47),
            new Student("Nguyen Phuong Nha", 20, 8.28),
            new Student("Hoang Hai Van", 20, 9.23)));

    private ServerSocket serverSocket;

    public void startServer() {
        try {
            this.serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client " + clientSocket.getInetAddress() + " connected");
                ServerProcess serverProcess = new ServerProcess(this, clientSocket);
                new Thread(serverProcess).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Student> getStudents() {
        return students;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public static void main(String[] args) {
        new Server().startServer();
    }

}

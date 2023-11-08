package Socket.CopyFile;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientProcess implements Runnable {
    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;

    public static final String COPY_COMMAND = "copy";
    public static final String EXIT_COMMAND = "exit";

    public ClientProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new DataInputStream(socket.getInputStream());
        this.netOut = new DataOutputStream(socket.getOutputStream());
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getNetIn() {
        return netIn;
    }

    public void setNetIn(DataInputStream netIn) {
        this.netIn = netIn;
    }

    public DataOutputStream getNetOut() {
        return netOut;
    }

    public void setNetOut(DataOutputStream netOut) {
        this.netOut = netOut;
    }

    public void printGreetingFromServer() {
        try {
            System.out.println(netIn.readUTF());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        printGreetingFromServer();
        boolean exit = false;
        Scanner sc = new Scanner(System.in, "UTF-8");    
        try {
            while(!exit) {
                System.out.print("Nhập lệnh: ");
                String command = sc.nextLine();
                String[] commandSplit = command.split(" ");
                String commandName = commandSplit[0];
                if(commandName == null) throw new Exception("Chưa nhập lệnh");
                else {
                    switch (commandName) {
                        case COPY_COMMAND:
                            String sourceFile = commandSplit[1];
                            String destFile = commandSplit[2];

                            netOut.writeUTF(COPY_COMMAND);
                            netOut.flush();
                            upload(sourceFile, destFile);

                            String message = netIn.readUTF();
                            System.out.println(message);
                            break;
                        case EXIT_COMMAND:
                            exit = true;
                            break;
                        default:
                            break;
                    }
                }
            }

            sc.close();
            socket.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void upload(String sourceFile, String destFile) {
        try {
            netOut.writeUTF(destFile);
            netOut.flush();

            File file = new File(sourceFile);
            if(!file.exists())
                throw new Exception("File not found");
            if(file.isDirectory())
                throw new Exception("Cannot upload folder");
            
            netOut.writeLong(file.length());
            netOut.flush();
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            int offset;
            while((offset = bis.read(buffer)) != -1) {
                netOut.write(buffer, 0, offset); 
                netOut.flush();
            }

            bis.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}

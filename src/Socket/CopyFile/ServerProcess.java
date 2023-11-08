package Socket.CopyFile;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerProcess implements Runnable {
    private Socket socket;
    private DataInputStream netIn;
    private DataOutputStream netOut;

    public static final String COPY_COMMAND = "copy";
    public static final String EXIT_COMMAND = "exit";

    public ServerProcess(Socket socket) throws IOException {
        this.socket = socket;
        this.netIn = new DataInputStream(socket.getInputStream());
        this.netOut = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        greeting();
        try {
            while (!socket.isClosed()) {
                String command = netIn.readUTF();
                switch (command) {
                    case COPY_COMMAND:
                        String destFile = netIn.readUTF();
                        download(destFile);
                        break;
                
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void greeting() {
        try {
            netOut.writeUTF("Kết nối thành công đến Server");
            netOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void download(String destFile) {
        try {
            long size = netIn.readLong();
            File file = new File(destFile);
            if(!file.exists())
                file.createNewFile();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[1024];
            int offset;
            int byteReaded = 0;
            while(byteReaded < size) {
                offset = netIn.read(buffer);
                bos.write(buffer, 0, offset);
                byteReaded += offset;
            }
            bos.close();
            netOut.writeUTF("Upload file thành công");
            netOut.flush();
        } catch(Exception e) {
            e.printStackTrace();
        }
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

}

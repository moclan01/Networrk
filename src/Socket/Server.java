package Socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2222);
        System.out.println("Listening ----");
        Socket socket = serverSocket.accept();
        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        int length = bis.read();
        byte[] fileNameBytes = new byte[length];
        for(int i = 0; i < length; i++) {
            fileNameBytes[i] = (byte)bis.read();
        }
        String fileName = new String(fileNameBytes, Charset.forName("UTF-8"));
        System.out.println("File = " + fileName);
        File file = new File("resources/server/" + fileName);
        file.createNewFile();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));

        int ch;
        while ((ch = bis.read()) != -1) {
            bos.write(ch);
        }

        bis.close();
        bos.close();
        serverSocket.close();
    }
}

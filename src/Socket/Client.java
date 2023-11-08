package Socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void upload(String path) throws IOException {
        Socket socket = new Socket("localhost", 2222);
        File file = new File(path);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = bis.readAllBytes();
        bis.close();
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        bos.write(file.getName().getBytes().length);
        bos.write(file.getName().getBytes());
        bos.write(bytes);
        bos.flush();
        bos.close();
        socket.close();
    }
    public static void main(String[] args) throws IOException {
        upload("resources/docs/Net-Program-OutCames-Exercise.docx");
    }
}

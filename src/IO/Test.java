package IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class Test {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("resources/docs/Net-Program-OutCames-Exercise.docx")), Charset.forName("UTF-8")));
        PrintWriter printWriter = new PrintWriter(System.out);
        String line;
        while ((line = reader.readLine()) != null) {
            printWriter.println(line);
        }
    }
}

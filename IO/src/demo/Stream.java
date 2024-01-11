package demo;

import java.io.File;
import java.io.FileInputStream;

public class Stream {
	public static void InputStream(String path) {
		try {
			File file = new File(path);
			FileInputStream fileInput = new FileInputStream(file);
			System.out.println("dữ liệu trong tệp");
			
			int data = fileInput.read();
			while(data != -1) {
				System.out.print((char)data);
				data = fileInput.read();
			}
			
			fileInput.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public static void OutputStream(String path) {
		
	}
	
	public static void main(String[] args) {
			InputStream("test.txt");
	}
}

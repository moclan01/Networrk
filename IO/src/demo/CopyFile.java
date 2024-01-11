package demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyFile {
	public static void copyFile(String inPath, String outPath) throws IOException {
		BufferedInputStream inputFile = new BufferedInputStream(new FileInputStream(inPath));
		BufferedOutputStream outputFile = new BufferedOutputStream(new FileOutputStream(outPath));
		
		int c;
		long beginTime = System.currentTimeMillis();
		while ((c=inputFile.read()) > -1) {
			outputFile.write(c);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("copy file "+ (endTime - beginTime) + "ms");
		inputFile.close();
		outputFile.close();
	}
	public static void main(String[] args) throws IOException {
		copyFile("D:\\Projects\\Java\\Network\\IO\\wallpaperflare.com_wallpaper.jpg", "D:\\Projects\\Java\\Network\\IO\\image.jpg");
	}
}

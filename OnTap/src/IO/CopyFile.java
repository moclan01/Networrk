package IO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class CopyFile {
	private static boolean copyFile(String sFile, String destFile, boolean moved) throws IOException {
		File sourcefile = new File(sFile);
		File destinationFile = new File(destFile);
		destinationFile.createNewFile();
		FileInputStream fis = new FileInputStream(sourcefile);
		FileOutputStream dos = new FileOutputStream(destinationFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		BufferedOutputStream bos = new BufferedOutputStream(dos);
		int data;
		byte[] buffer = new byte[1024];
		while ((data = bis.read(buffer)) != -1) {
			System.out.println(Arrays.toString(buffer));
			bos.write(buffer, 0, data);
		}
		bis.close();
		bos.close();
		if (moved == true) {
			sourcefile.delete();
		}
		return false;
	}

	public static void main(String[] args) throws IOException {
		copyFile("resources/IO/fileCopy1.txt", "resources/IO/fileCopy2.txt", false);
	}
}

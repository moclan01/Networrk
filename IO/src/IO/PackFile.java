package IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class PackFile {
	public static void packFolder(String sourceDir, String packFileName) throws IOException {
		File folder = new File(sourceDir);
		File fileName = new File(packFileName);
		fileName.createNewFile();
		RandomAccessFile raf = new RandomAccessFile(sourceDir, "rw");
		List<Long> pointers = new ArrayList<Long>();
		int size = folder.listFiles().length;

		// write header
		raf.writeInt(size);
		for (int i = 0; i < size; i++) {
			long currentPosition = raf.getFilePointer();
			pointers.add(currentPosition);
			raf.writeLong(0);
		}

		// write body
		for (int i = 0; i < size; i++) {
			long currentPosition = raf.getFilePointer();
			File file = folder.listFiles()[i]; 
			FileInputStream fis = new FileInputStream(file);
			
			raf.seek(pointers.get(i));
			raf.writeLong(currentPosition);
			raf.seek(currentPosition);
			
			raf.writeUTF(file.getName());
			raf.writeLong(file.length());
			raf.write(fis.readAllBytes());
			fis.close();
			
		}
		raf.close();
	}
	

	 
	public static void main(String[] args) throws IOException {
		packFolder("./IO/test/demo", "/IO/test.txt");
	}
}

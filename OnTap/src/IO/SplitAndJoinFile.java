package IO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SplitAndJoinFile {
	public static void splitFile(String sFile, String destFolder, int size) throws IOException {
		File file = new File(sFile);
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		long totalSize = file.length();
		int countFile = (int) (totalSize / size);
		long remainderFile = totalSize % size;
		File newFolder = new File(destFolder);
		newFolder.mkdir();
		// write files with size
		byte[] buffer = new byte[1024];
		for (int i = 0; i < countFile; i++) {
//			fos = new FileOutputStream(newFolder + File.separator + "split" + i);
//			for (int j = 0; j < size; j++) {
//				fos.write(j);
//			}
//			fos.close();
			String splitFileName = destFolder + File.separator + "split" + i;
			FileOutputStream fos = new FileOutputStream(splitFileName);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			int bytesRead;
			int totalBytesRead = 0;
			while (totalBytesRead < size
					&& (bytesRead = bis.read(buffer, 0, Math.min(buffer.length, size - totalBytesRead))) != -1) {
				bos.write(buffer, 0, bytesRead);
				totalBytesRead += bytesRead;
			}
			bos.close();
		}
		// write remainder file
		if (remainderFile > 0) {
			FileOutputStream fos = new FileOutputStream(newFolder + File.separator + "split end");
			for (int i = 0; i < remainderFile; i++) {
				fos.write(i);
			}
			fos.close();
		}

		System.out.println(size + " " + totalSize + " " + countFile);
	}

	public static void joinFIle(String sFolder, String destFile) throws IOException {
		File file = new File(sFolder);
		File desFile = new File(destFile);
		desFile.createNewFile();
		File[] listFiles = file.listFiles();
		FileInputStream fis;
		FileOutputStream fos = new FileOutputStream(desFile);
		for (int i = 0; i < listFiles.length; i++) {
			if(i == listFiles.length - 1) {
				fis = new FileInputStream(sFolder + File.separator + "split end");
			}else {
				fis = new FileInputStream(sFolder + File.separator + "split" + i);
			}
			int data;
			while((data = fis.read()) != -1) {
				fos.write(data);
			}
			fis.close();
		}
		fos.close();
	}

	public static void main(String[] args) throws IOException {
//		splitFile("resources/IO/video.mp4", "resources/IO/split2", 1024 * 1000);
		joinFIle("resources/IO/split", "resources/IO/fileJoin.mp4");
	}
}

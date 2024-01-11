package IO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Copy {
	public static boolean fileCopy(String sFile, String destFile, boolean moved) {
		boolean result = false;
		File file = new File(sFile);
		try {
			if (!file.exists()) {
				return false;
			} else {
				if (file.isFile()) {
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sFile));
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
					int c;
					while ((c = bis.read()) != -1) {
						bos.write((char) c);
					}
					bis.close();
					bos.close();

					if (moved == true) {
						file.delete();
					}

					result = true;

				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return result;
	}

	public static boolean folderCopy(String sFolder, String destFolder,boolean moved) {
		return moved;
		
	}
	public static void main(String[] args) throws FileNotFoundException {
		fileCopy("D:\\Projects\\Java\\Network\\IO\\image1.jpg", "D:\\Projects\\Java\\Network\\IO\\image2.jpg", true);
	}
}

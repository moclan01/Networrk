package fileClass;

import java.io.File;

public class Delete {
	public static boolean delete(String path) {
		boolean result = false;
		File file = new File(path);
		if (!file.exists()) {
			return false;
		} else {
			file.delete();
			result = true;
		}

		return result;
	}

	public static boolean deleteFiles(String path) {
		boolean result = false;
		File file = new File(path);
		if (file.exists()) {
			if(file.isFile()) {
				file.delete();
			}else {
				for(File child : file.listFiles()) {
					deleteFiles(child.getAbsolutePath());
				}
			}
			
		}
		return result;
	}

	public static void main(String[] args) {
		deleteFiles("D:\\Projects\\Java\\Network\\IO\\test1");
	}
}

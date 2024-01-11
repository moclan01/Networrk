package fileClass;

import java.io.File;

public class FindPath {
	public static boolean findFirst(String path, String pattern) {
		boolean result = false;
		File file = new File(path);
		
		if (file.isFile()) {
			if (file.getAbsolutePath().endsWith(pattern)) {
				System.out.println(file.getAbsolutePath());
				return true;
			}
		} else {
			for (File subFile: file.listFiles()) {
				result = findFirst(subFile.getAbsolutePath(), pattern);
				if (result) break;
			}
		}
		return result;

	}

	public static void main(String[] args) {
		System.out.println(findFirst("D:\\Projects\\Java\\Network\\IO\\test", "docx"));
	}
}

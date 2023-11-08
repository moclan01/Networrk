package IO;
import java.io.File;
import java.io.IOException;

public class MyFile {

    public boolean createNewFile(String pathFile) {
        try {
            File file = new File(pathFile);
            String pathFileArr[] = pathFile.split("/");
            String newPath = "";
            for(int i = 0; i < pathFileArr.length - 1; i++) {
                newPath += pathFileArr[i] + "/";
                File dirFile = new File(newPath);
                if(!dirFile.exists())
                    dirFile.mkdirs();
            }
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAll(String pathFile) {
        File file = new File(pathFile);
        if(!file.exists()) return false;

        File[] files = file.listFiles();
        for (File checkFile : files) {
            if(checkFile.isFile())
                checkFile.delete();
            else {
                deleteAll(checkFile.getPath());
                checkFile.delete();
            }
        }
        return file.delete();
    }

    public boolean deleteFileOnly(String pathFile) {
        File file = new File(pathFile);
        if(!file.exists()) return false;

        File[] files = file.listFiles();
        for (File checkFile : files) {
            if(checkFile.isFile())
                checkFile.delete();
            else {
                deleteFileOnly(checkFile.getPath());
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        MyFile myFile = new MyFile();
        for(int i = 0; i < 10; i++) {
            myFile.createNewFile("resources/games/mygame/testfolder/test" + (i+1) + ".txt");
            myFile.createNewFile("resources/games/mygame/game" + (i+1) + ".txt");
            myFile.createNewFile("resources/games/bestgame" + (i+1) + ".txt");
        }
        // myFile.deleteAll("resources");
        myFile.deleteFileOnly("resources");
    }
}

package IO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackUnPack {
    public static void pack(String pathDirectory, String packedPathFile) throws IOException {
        File rawDir = new File(pathDirectory);
        File packedFile = new File(packedPathFile);

        try (RandomAccessFile raf = new RandomAccessFile(packedFile, "rw")) {
            List<Long> pointers = new ArrayList<Long>();
            List<Long> fileBytes = new ArrayList<Long>();

            if (rawDir.isDirectory()) {
                File[] files = rawDir.listFiles();
                raf.writeInt(files.length);
                for (int i = 0; i < files.length; i++) {
                    pointers.add(raf.getFilePointer());
                    raf.writeLong(0);
                    raf.writeUTF(files[i].getName());
                    fileBytes.add(raf.getFilePointer());
                    raf.writeInt(0);
                }

                for (int i = 0; i < files.length; i++) {
                    long currentPosition = raf.getFilePointer();
                    raf.seek(pointers.get(i));
                    raf.writeLong(currentPosition);
                    raf.seek(currentPosition);
                    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(files[i]))) {
                        byte[] bytes = bis.readAllBytes();
                        raf.write(bytes);
                        currentPosition = raf.getFilePointer();
                        raf.seek(fileBytes.get(i));
                        raf.writeInt(bytes.length);
                        raf.seek(currentPosition);
                    }
                }
            } else
                System.out.println("Phải là Folder");
        }
    }

    public static void unpack(String packedPathfile, String unpackPathLocation) throws IOException {
        File packedFile = new File(packedPathfile);
        File unpackFile = new File(unpackPathLocation);

        if (!unpackFile.exists()) {
            unpackFile.mkdirs();
        }

        try (RandomAccessFile raf = new RandomAccessFile(packedFile, "r")) {
            int length = raf.readInt();
            List<Long> pointers = new ArrayList<Long>();
            List<String> fileNames = new ArrayList<String>();
            List<Integer> byteLengths = new ArrayList<Integer>();
            for (int i = 0; i < length; i++) {
                pointers.add(raf.readLong());
                fileNames.add(raf.readUTF());
                byteLengths.add(raf.readInt());
            }

            for (int i = 0; i < length; i++) {
                raf.seek(pointers.get(i));
                File file = new File(unpackPathLocation + File.separator + fileNames.get(i));
                file.createNewFile();
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
                    int byteLength = byteLengths.get(i);
                    for (int j = 0; j < byteLength; j++)
                        bos.write(raf.read());
                }
            }
        }
    }

    public static void writeHeaderSub(RandomAccessFile raf, File directory, List<Long> pointers, String parentFolder, Map<File, Integer> map)
            throws IOException {
        List<String> test = new ArrayList<String>();
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            int temp = 0;
            while(map.containsValue(temp))
                temp++;
            map.put(file, temp);
            pointers.add(raf.getFilePointer());
            raf.writeLong(0);
            raf.writeLong(file.length());
            raf.writeUTF(parentFolder + File.separator + file.getName());
            if (file.isFile()) {
                test.add("file");
                raf.writeUTF("file");
            } else {
                test.add("folder");
                raf.writeUTF("folder");
                writeHeaderSub(raf, file, pointers, parentFolder + File.separator + file.getName(), map);
            }

        }

    }

    public static void writeDataSub(RandomAccessFile raf, File directory, List<Long> pointers, Map<File, Integer> map)
            throws IOException {
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            long currentPosition = raf.getFilePointer();
            raf.seek(pointers.get(map.get(file)));
            raf.writeLong(currentPosition);
            raf.seek(currentPosition);
            if (file.isFile()) {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer = new byte[1024];
                int offset;
                while ((offset = bis.read(buffer)) != -1) {
                    raf.write(buffer, 0, offset);
                }
                bis.close();
            }

            else {
                writeDataSub(raf, file, pointers, map);
            }
        }
    }

    public static void packWithFolder(String pathname, String packFilePath) throws IOException {
        File pathFile = new File(pathname);
        File packFile = new File(packFilePath);
        RandomAccessFile raf = new RandomAccessFile(packFile, "rw");
        raf.writeInt(0);
        List<Long> pointers = new ArrayList<Long>();
        Map<File, Integer> map = new HashMap<File, Integer>();
        writeHeaderSub(raf, pathFile, pointers, "", map);

        long currentPosition = raf.getFilePointer();
        raf.seek(0);
        raf.writeInt(pointers.size());
        raf.seek(currentPosition);

        writeDataSub(raf, pathFile, pointers, map);
        raf.close();

    }

    public static void unpackWithFolder(String packedFilePath, String unpackLocation) throws IOException {
        File packedFile = new File(packedFilePath);
        File unpackDir = new File(unpackLocation);

        if (!unpackDir.exists())
            unpackDir.mkdirs();

        RandomAccessFile raf = new RandomAccessFile(packedFile, "r");
        List<Long> pointers = new ArrayList<Long>();
        List<String> paths = new ArrayList<String>();
        List<String> fileTypes = new ArrayList<String>();
        List<Long> fileSizes = new ArrayList<Long>();
        int length = raf.readInt();
        for (int i = 0; i < length; i++) {
            pointers.add(raf.readLong());
            fileSizes.add(raf.readLong());
            paths.add(raf.readUTF());
            fileTypes.add(raf.readUTF());
        }

        for (int i = 0; i < paths.size(); i++) {
            File file = new File(unpackLocation + paths.get(i));
            if (fileTypes.get(i).equals("file")) {
                file.createNewFile();
                raf.seek(pointers.get(i));
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                byte[] buffer = new byte[1024];
                int offset;
                int readedBytes = 0;
                long size = fileSizes.get(i);
                while (readedBytes < size) {
                    offset = raf.read(buffer);
                    readedBytes += offset;
                    if(readedBytes < size)
                        bos.write(buffer, 0, offset);
                    else 
                        bos.write(buffer, 0, offset - (int)(readedBytes - size));
                }

                bos.close();
            } else
                file.mkdirs();
        }

        raf.close();
    }

    public static void main(String[] args) throws IOException {
        // pack("resources/pack", "resources/pack.dat");
        // unpack("resources/pack.dat", "resources/unpack");
        // packWithFolder("resources/packwithfolder", "resources/packwithfolder.dat");
        unpackWithFolder("resources/packwithfolder.dat", "resources/unpackwithfolder");
    }
}

package server_socket;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

public class Service {
	private static String pathname = "DATA/thisinh.dat";
	private static Service instance;

	private Map<Integer, Long> map;

	private RandomAccessFile raf;

	private Service() throws IOException {
		File file = new File(pathname);
		this.map = new HashMap<Integer, Long>();
		if (!file.exists()) {
			file.createNewFile();
			raf = new RandomAccessFile(file, "rw");
		} else
			raf = new RandomAccessFile(file, "rw");
	}

	public static Service getInstance() throws IOException {
		if (instance == null)
			instance = new Service();
		return instance;
	}

	private char[] toCharArray(String str, int length) {
		char[] strChars = new char[length];
		str.getChars(0, str.length(), strChars, 0);
		return strChars;
	}

	private byte[] charToByte(char[] chars) {
		byte[] bytes = new byte[chars.length];
		for (int i = 0; i < bytes.length; i++)
			bytes[i] = (byte) chars[i];
		return bytes;
	}

	private String bytesToString(byte[] bytes, int length) {
//    	String result=bytes.toString();
//    	for(int i = 0; i < bytes.length; i++) {
//    		if(bytes[i] ==0) {
//    			result += " ";
//    		}
//    	}
		return new String(bytes);
	}

	public void writeFile(int id, byte[] buffer) throws IOException {
		long pointer = map.get(id);
		pointer += 4 + 25 + 10 + 25;
		raf.seek(pointer);
		raf.write(buffer);
	}

	public boolean checkBirthday(Candidate candidate) {
		return candidate.isValidAge();
	}

	public void writeCandidate(Candidate candidate) throws IOException {
		byte[] bytes = new byte[100000];
		// write header
		if (raf.length() == 0) {
			raf.seek(0);
			raf.writeInt(0);
		}
		raf.seek(0);
		int size = raf.readInt();
		size++;
		raf.seek(0);
		raf.writeInt(size);

		// write body
		raf.seek(raf.length());
		candidate.setId(size);

		// Dịch string ra byte[]
		byte[] nameBytes = charToByte(toCharArray(candidate.getName(), 25));
		byte[] birthdayBytes = charToByte(toCharArray(candidate.getBirthday(), 10));
		byte[] addressBytes = charToByte(toCharArray(candidate.getAddress(), 25));

		long pointer = raf.getFilePointer();
		raf.writeInt(candidate.getId());
		raf.write(nameBytes);
		raf.write(birthdayBytes);
		raf.write(addressBytes);
		raf.write(bytes);

		map.put(candidate.getId(), pointer);
	}

	public void update(int upId, String upAddress) throws IOException {
		long pointer = map.get(upId);
		pointer += 4 + 25 + 10;
		raf.seek(pointer);
		byte[] addressBytes = charToByte(toCharArray(upAddress, 25));
		raf.write(addressBytes);
	}

	public boolean isExist(int id) {
		return map.containsKey(id);
	}

	public String getUserData(int candidateId) throws IOException {
		String result = "";
		byte[] bytes = new byte[100000];
		long pointer = map.get(candidateId);
		raf.seek(pointer);

		byte[] bytesName = new byte[25];
		byte[] bytesBirthday = new byte[10];
		byte[] bytesAddress = new byte[25];

		int id = raf.readInt();
		raf.read(bytesName);
		raf.read(bytesBirthday);
		raf.read(bytesAddress);
		raf.read(bytes);

		int size = 0;
		for (int i = 0; i < bytes.length; i++) {
			if (bytes[i] != 0) {
				size++;
			} else
				break;
		}

		String name = bytesToString(bytesName, 25);
		String birthday = bytesToString(bytesBirthday, 10);
		String address = bytesToString(bytesAddress, 25);

		result = "Id= " + id + " ,Name = " + name + " ,birthday = " + birthday + " ,address = " + address
				+ " ,file size = " + size;
		return result;
	}
}

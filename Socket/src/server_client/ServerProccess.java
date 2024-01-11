package server_client;

import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerProccess implements Runnable {
	private Socket socket;

	public ServerProccess(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			OutputStream os = socket.getOutputStream();
			os.write(3);
			os.flush();
			
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			String message = dis.readUTF();
			System.out.println(message);
			
			//socketstream k close, cac stream khac thi close
			dis.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}

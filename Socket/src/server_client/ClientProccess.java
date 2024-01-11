package server_client;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class ClientProccess implements Runnable{
	private Socket socket;
	
	
	public ClientProccess(Socket socket) {
		this.socket = socket;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			InputStream is = socket.getInputStream();
			System.out.println(is.read());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF("Hello! I'm client.");
			dos.flush();
			

			is.close();
			dos.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}

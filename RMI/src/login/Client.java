package login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Client {
	public static void main(String[] args) throws NotBoundException, IOException {
		Registry registry = LocateRegistry.getRegistry("localhost", 1234);
		IAuthentication client = (IAuthentication) registry.lookup("authen");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String username = "admin";
		String password = "admin123";
		String sessionId = client.login(username, password);
		while(true) {
			//using app
			String request = reader.readLine();
			//get info user
			if(request.equals("get")) {
				User user = client.getUserData(sessionId);
				if(user != null) {					
					System.out.println(user);
				}else System.out.println("sessionId khong hop le");
			}
		}
	}
}

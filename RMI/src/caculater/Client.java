package caculater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
	public static void main(String[] args) throws NotBoundException, IOException {
		Registry registry = LocateRegistry.getRegistry("localhost", 1234);
		ICaculater client = (ICaculater) registry.lookup("caculate");

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.print("enter your command: ");
			String commnad = reader.readLine();
			String respone = client.caculate(commnad);
			System.out.println(respone);
		}
	}
}

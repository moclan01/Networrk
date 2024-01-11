package clientDemo;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import serverDemo.IServer;

public class App {
	public static void main(String[] args) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry("localhost", 2222);
		IServer server = (IServer) registry.lookup("server");
		server.echo("hello server");
	}
}

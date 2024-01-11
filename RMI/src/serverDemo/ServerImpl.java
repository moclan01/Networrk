package serverDemo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl extends UnicastRemoteObject implements IServer{

	protected ServerImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void echo(String message) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(message);
	}

}

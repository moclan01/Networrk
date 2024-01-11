package caculater;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICaculater extends Remote {
	public String caculate(String request) throws RemoteException;
}

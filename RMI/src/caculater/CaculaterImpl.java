package caculater;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CaculaterImpl extends UnicastRemoteObject implements ICaculater{

	protected CaculaterImpl() throws RemoteException {
		super();
		
	}

	@Override
	public String caculate(String request) throws RemoteException {
		
		String result = request;
		result += " = ";
		String[] requestSplit = request.split(" ");
		int firstNumber = Integer.parseInt(requestSplit[0]);
		String subtend = requestSplit[1];
		int secondNumber = Integer.parseInt(requestSplit[2]);
		
		switch (subtend) {
		case "+": {
			result += (firstNumber + secondNumber);
			break;
		}
		case "-": {
			result += (firstNumber - secondNumber);
			break;
		}
		case "*": {
			result += (firstNumber * secondNumber);
			break;
		}
		case "/": {
			if (secondNumber == 0) {
				System.out.println("phep tinh khong hop le");
			} else {
				result += (firstNumber / secondNumber);
			}
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + subtend);
		}
		
		return result;
	}

}

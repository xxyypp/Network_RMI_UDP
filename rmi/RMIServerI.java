/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import common.*;
//remote interface, inheriate Remote interface
//throw RemoteException when using remote method
public interface RMIServerI extends Remote {
	public void receiveMessage(MessageInfo msg) throws RemoteException;
}

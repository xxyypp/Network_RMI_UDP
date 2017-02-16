/*
* Created on 01-Mar-2016
*/
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.AccessException;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		// TO-DO: Bind to RMIServer
		// TO-DO: Attempt to send messages the specified number of times
		try{
			Registry r = LocateRegistry.getRegistry(args[0],8080);
			RMIServerI s = (RMIServerI) r.lookup("RMIServerI");

			for(int i = 0; i < numMessages; i++){
				MessageInfo msg = new MessageInfo(numMessages, i);
				s.receiveMessage(msg);
			}
			System.out.println("Msg sent:" + numMessages);
			System.exit(0);
		}catch(RemoteException e){
			System.out.println("Error: Remote Exception.");
			e.printStackTrace();
			System.exit(-1);
		}catch(NotBoundException e){
			System.out.println("Error: Not Bound Exception.");
			e.printStackTrace();
			System.exit(-1);
		}
		catch (Exception e){
			System.err.println("Exception in the Client, please check");
			e.printStackTrace();
			System.exit(-1);
		}
	}
}

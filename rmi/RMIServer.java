/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.AccessException;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
		/*
		super(a,b,c,d,e)
		a -> indicate remoteObject port
		b -> ?
		c -> ?
		d -> encrypt algorithm
		e -> two way confirmation between client and server
		*/
		super(); /**********used or not?**********/
	}
	//remote method to return msg, also throw RemoteException
	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (totalMessages != msg.totalMessages){
			totalMessages = msg.totalMessages;
			receivedMessages = new int[msg.totalMessages];
		}
		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;
		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if(msg.messageNum + 1 == totalMessages){
			System.out.println("Message sum");
			String tmp = "Package lost: ";
			int count = 0;
			for(int i = 0; i < totalMessages; i++){
				if(receivedMessages[i] != 1){
					count++;
					tmp = tmp + " " + (i+1) + ", ";
				}
			}
			if (count == 0){
				tmp = tmp + "None";
			}
			System.out.println("Messages sent: " + totalMessages);
			System.out.println("Messages received: " + (totalMessages - count));
			System.out.println("Messages lost: " + count);
			System.out.println(tmp);
			System.out.println("Test finished.");
			totalMessages = -1;
			//System.exit(0);
		}
	}

	//create rmiregistry, start RMIServer, put remote object into rmiregistry
	public static void main(String[] args) {

		RMIServer rmis = null;

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		// TO-DO: Instantiate the server class
		// TO-DO: Bind to RMI registry
		try{
			//LocateRegistry.createRegistry(8080);
			RMIServer server = new RMIServer();
			//Naming.rebind("RMIServer", new RMIServer());
			rebindServer("RMIServerI", server);
			System.out.println("Server done!");
		}catch(Exception Exce){
			System.err.println("RMIServerI exception: ");
			Exce.printStackTrace();
			System.exit(-1);
		}




	}

	protected static void rebindServer(String serverURL, RMIServer server) {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)
		try{
			Registry registry = LocateRegistry.createRegistry(8080);
			registry.rebind(serverURL,server);
		}
		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
		catch(RemoteException RmEx){
			System.out.println("Server can't bind regisrty");
			RmEx.printStackTrace();
			System.exit(-1);
		}/*catch(MalformedURLException e){
			System.out.println("Error: Malformed URL Exception.");
		}*/
	}
}

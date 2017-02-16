package udp;

import java.io.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;

		close = false;
		pacSize = 50;
		pacData = new byte[50];

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		do{
			try{
				pac = new DatagramPacket(pacData,pacSize);
				recvSoc.setSoTimeout(30000);
				recvSoc.receive(pac);
				String pacdata = new String(pac.getData()).trim();
				processMessage(pacdata);
			} catch(IOException e){
				close = true;
				System.out.println("Error due to timeout.");
			}
		}while(!close);

	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try{
			msg = new MessageInfo(data);
		}catch(Exception e){
			System.out.println("Cannot construct a new MessageInfo object");
			e.printStackTrace();
			System.exit(-1);
		}
		// TO-DO: On receipt of first message, initialise the receive buffer
		if (receivedMessages == null){
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages];
		}
		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;
		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		int counter = 0;
		if(msg.messageNum + 1 == msg.totalMessages){
			String tmp = "Packet lost: ";
			for(int i =0; i < totalMessages; i++){
				if(receivedMessages[i] != 1){
					tmp = tmp + " " + (i+1) + ", ";
					counter ++;
				}
			}
			if(counter == 0){
				tmp = tmp + " No packet lost! ";
			}

			System.out.println("Show result:");
			System.out.println("Total: " + msg.totalMessages + " -> " + (msg.totalMessages-counter) + " Received! ");
			System.out.println("Total: " + msg.totalMessages + "->" + counter + " Failed... ");
			System.out.println(tmp);
			System.out.println("Send done!");
			close = true;
		}
	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try {
			recvSoc = new DatagramSocket(rp);
		} catch (SocketException e) {
			System.out.println("Couldn't initialise recvSoc");
			e.printStackTrace();
			System.exit(-1);
		}
		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer udpsrv = new UDPServer(recvPort);
		udpsrv.run();
}

}

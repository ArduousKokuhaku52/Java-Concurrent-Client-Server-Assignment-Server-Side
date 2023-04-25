/***********************************************************
 * Name: Damien Gill
 * Assignment: Assignment 9
 * Description: This assignment simulates multiple clients
 * 	connecting to multiple servers concurrently and
 * 	accessing/manipulating a shared resource with no loss
 * 	of data.
 ***********************************************************/
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import messages.Message;

public class SenderServer extends Thread{

	protected ArrayList<Message> messages;
	private ServerSocket server;

	/********************************************************
	 * Description: Constructor method that assigns the messages
	 * 	member variable and instantiates the server on the passed
	 * 	in port
	 * Precondition: Requires a reference to the messages ArrayList
	 * 	and a port as an int
	 * Postcondition: The server is instantiated on the port and
	 * 	a conformation message is printed to the console.
	 ********************************************************/
	public SenderServer(ArrayList<Message> messages, int port) {
		this.messages = messages;
		try {
			this.server = new ServerSocket(port);
			System.out.println("Sending Server started...");
		}
		catch (IOException e) {
			System.out.println(e);
		}
	}

	/********************************************************
	 * Description: Overriden run method from the Thread class.
	 * 	Waits for a client to connect then instantiates
	 * 	and starts SenderHandler to process the client. Runs
	 * 	in an infinite loop until shut down manually
	 ********************************************************/
	@Override
	public void run() {
		while(true) {	//runs in infinite loop until manually terminated
			try {
				//establishes connection to client
				Socket client = server.accept();

				SenderHandler handler = new SenderHandler(this.messages, client);
				//starts thread so another client can be processed
				handler.start();
			}
			catch(IOException e) {
				System.out.println(e);
			}
		}
	}
}

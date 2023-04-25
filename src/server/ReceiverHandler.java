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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import messages.Message;

public class ReceiverHandler extends Thread {

	private Socket client;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	protected ArrayList<Message> messages;

	/********************************************************
	 * Description: Constructor method that assigns the messages
	 * 	and client member variables
	 * Precondition: Requires a reference to the messages ArrayList
	 * 	and a reference to the client Socket
	 ********************************************************/
	public ReceiverHandler(ArrayList<Message> messages, Socket client) {
		this.messages = messages;
		this.client = client;
	}

	/********************************************************
	 * Description: Overriden run method from the Thread class.
	 * 	Receives a Message from the client then writes a String
	 * 	object back to the client confirming the transaction.
	 * 	Closes the client afterwards.
	 ********************************************************/
	@Override
	public void run() {
		try {
			this.ois = new ObjectInputStream(client.getInputStream());
			this.oos = new ObjectOutputStream(client.getOutputStream());

			Message clientMessage = (Message) this.ois.readObject();

			//Locks the messages ArrayList to ensure no data loss when manipulating data
			synchronized (this.messages) {
				this.messages.add(clientMessage);
			}
			//Returns confirmation to client when added
			String s = "Recorded";
			this.oos.writeObject(s);
			//Closes client after finished
			this.client.close();
		}
		catch (IOException | ClassNotFoundException e) {
			System.out.println(e);
		}
	}
}

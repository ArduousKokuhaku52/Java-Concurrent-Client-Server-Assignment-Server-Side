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
import messages.Searcher;

public class SenderHandler extends Thread{

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
	public SenderHandler(ArrayList<Message> messages, Socket client) {
		this.messages = messages;
		this.client = client;
	}

	/********************************************************
	 * Description: Overriden run method from Thread class.
	 * 	A searcher object is received from the client
	 * 	then searches the messages ArrayList for the message.
	 * 	Returns either a String if the object isn't found or
	 * 	the Message if found. Closes the client after finishing.
	 ********************************************************/
	@Override
	public void run() {
		try {
			this.ois = new ObjectInputStream(client.getInputStream());
			this.oos = new ObjectOutputStream(client.getOutputStream());

			Searcher clientSearcher = (Searcher) this.ois.readObject();

			//Locks the messages ArrayList to ensure no data loss when manipulating data
			synchronized(this.messages) {
				int index = this.messages.indexOf(clientSearcher);
				if (index == -1) {		//If not found writes "No message"
					String s = "No message";
					this.oos.writeObject(s);
				}
				else {	//If found returns the message
					Message foundMessage = this.messages.remove(index);
					this.oos.writeObject(foundMessage);
				}
			}
			//Closes client after finished
			this.client.close();
		}
		catch(IOException | ClassNotFoundException e) {
			System.out.println(e);
		}
	}
}

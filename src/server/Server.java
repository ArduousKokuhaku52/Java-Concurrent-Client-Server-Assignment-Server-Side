/***********************************************************
 * Name: Damien Gill
 * Assignment: Assignment 9
 * Description: This assignment simulates multiple clients
 * 	connecting to multiple servers concurrently and
 * 	accessing/manipulating a shared resource with no loss
 * 	of data.
 ***********************************************************/
package server;

import java.util.ArrayList;
import messages.Message;

public class Server {
	
	public static void main(String []args) {
		ArrayList<Message> messages = new ArrayList<Message>();

		ReceiverServer receiverServer = new ReceiverServer(messages, 4242);
		SenderServer senderServer = new SenderServer(messages, 4343);

		receiverServer.start();
		senderServer.start();
	}

}

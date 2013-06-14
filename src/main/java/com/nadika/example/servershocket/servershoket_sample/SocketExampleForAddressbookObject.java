package com.nadika.example.servershocket.servershoket_sample;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ClassNotFoundException;
import java.lang.Runnable;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.tutorial.AddressBookProtos.AddressBook;
import com.example.tutorial.AddressBookProtos.Person;
import com.google.protobuf.CodedInputStream;

public class SocketExampleForAddressbookObject {
	private ServerSocket server;
	private int port = 6666;
	private static long noOfclientConnection = 1;

	public SocketExampleForAddressbookObject() {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SocketExampleForAddressbookObject example = new SocketExampleForAddressbookObject();
		example.handleConnection();
	}

	public void handleConnection() {
		System.out.println("Waiting for client message...");

		//
		// The server do a loop here to accept all connection initiated by the
		// client application.
		//
		while (true) {
			try {
				Socket socket = server.accept();

				++noOfclientConnection;
				new ConnectionHandler(socket, noOfclientConnection);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class ConnectionHandler implements Runnable {
	private Socket socket;
	private long clntNo;

	public ConnectionHandler(Socket socket, long clNO) {
		this.socket = socket;
		this.clntNo = clNO;

		Thread t = new Thread(this);
		t.start();
	}

	void Print(AddressBook addressBook) {

		System.out
				.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%Handling client #: "
						+ clntNo + "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");

		for (Person person : addressBook.getPersonList()) {

			System.out.println("Person ID: " + person.getId());
			System.out.println("  Name: " + person.getName());
			if (person.hasEmail()) {
				System.out.println("  E-mail address: " + person.getEmail());
			}
			for (Person.PhoneNumber phoneNumber : person.getPhoneList()) {
				switch (phoneNumber.getType()) {
				case MOBILE:
					System.out.print("  Mobile phone #: ");
					break;
				case HOME:
					System.out.print("  Home phone #: ");
					break;
				case WORK:
					System.out.print("  Work phone #: ");
					break;
				}
				System.out.println(phoneNumber.getNumber());
			}
		}
	}

	public void run() {
		try {
			//
			// Read a message sent by client application
			//

			CodedInputStream cis = CodedInputStream.newInstance(socket
					.getInputStream());
			// InputStream ois = new CodedInputStream(socket.getInputStream());
			AddressBook addressBook = AddressBook.parseFrom(cis);

			// System.out.println("Message Received: " );

			System.out.println("Just connected to "
					+ socket.getRemoteSocketAddress());

			Print(addressBook);

			//
			// Send a response information to the client application
			//
			// ObjectOutputStream oos = new
			// ObjectOutputStream(socket.getOutputStream());
			// oos.writeObject("Hi...");

			// ois.close();
			// oos.close();

			socket.close();

			System.out.println("Waiting for client message...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
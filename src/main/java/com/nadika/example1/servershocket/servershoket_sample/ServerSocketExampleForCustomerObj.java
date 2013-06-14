package com.nadika.example1.servershocket.servershoket_sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.example.tutorial1.CustomerProtos.Customer;
import com.google.protobuf.CodedInputStream;

public class ServerSocketExampleForCustomerObj {
	private ServerSocket server;
	private int port = 12345;
	private static long noOfclientConnection = 1;

	public ServerSocketExampleForCustomerObj() {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ServerSocketExampleForCustomerObj example = new ServerSocketExampleForCustomerObj();
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

	public void run() {
		try {
			//
			// Read a message sent by client application
			//

			CodedInputStream cis = CodedInputStream.newInstance(socket
					.getInputStream());

			System.out.println("Just connected to "
					+ socket.getRemoteSocketAddress());

			Customer customer = Customer.parseFrom(cis);

			System.out.println("Responce: " + customer.toString());

			socket.close();

			System.out.println("Waiting for client message...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
package com.nadika.example.servershocket.test;
import java.net.*;
import java.io.*;

import com.example.tutorial.AddressBookProtos.AddressBook;
import com.example.tutorial.AddressBookProtos.Person;
import com.google.protobuf.CodedOutputStream;

public class GreetingServer extends Thread
{
   private ServerSocket serverSocket;
   
   public GreetingServer(int port) throws IOException
   {
      serverSocket = new ServerSocket(port);
      serverSocket.setSoTimeout(10000);
   }

   public void run()
   {
      while(true)
      {
         try
         {
            System.out.println("Waiting for client on port " +
            serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            System.out.println("Just connected to "
                  + server.getRemoteSocketAddress());
            DataInputStream in =
                  new DataInputStream(server.getInputStream());
            System.out.println(in.readUTF());
            DataOutputStream out =
                 new DataOutputStream(server.getOutputStream());
           
            
            
            //
            Person.Builder person = Person.newBuilder();
            person.setId(22);
            person.setEmail("dnsw@gmail.com");
            person.setName("dinu");

            AddressBook.Builder addressBook = AddressBook.newBuilder();
            addressBook.addPerson(person.build());
            

            
       //     CodedOutputStream output=new CodedOutputStream();
			addressBook.build().writeTo(out);

            
            //
            
            
            
            out.writeUTF("Thank you for connecting to "
              + server.getLocalSocketAddress() + "\nGoodbye!");
            server.close();
         }catch(SocketTimeoutException s)
         {
            System.out.println("Socket timed out!");
            break;
         }catch(IOException e)
         {
            e.printStackTrace();
            break;
         }
      }
   }
}
package com.nadika.example.servershocket.test;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class Server 
{
    public static void main( String[] args )
    {
    	int port = 6666;
        try
        {
           Thread t = new GreetingServer(port);
           t.start();
        }catch(IOException e)
        {
           e.printStackTrace();
        }
    }
}

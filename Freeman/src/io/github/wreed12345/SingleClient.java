package io.github.wreed12345;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.esotericsoftware.minlog.Log;

/**
 * Initiates a socket system to ensure only one instance of the game is running at a time.
 * @author William Reed
 *
 */
public class SingleClient implements Runnable {

	@Override
	public void run() {
		Log.info("Freeman Client", "Socket opened to prevent other clients");
		while (true) {
			try {
				ServerSocket serverSocket = new ServerSocket(10359); //start up socket on port 10359
				Socket socket = serverSocket.accept(); //wait for connection to occur
				DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());//make our output stream
				
				dOut.writeBoolean(true);//send the value true signifying another instance is already running
				dOut.flush(); // sending remaining buffer data out of da buffer
				
				//close all our connections
				dOut.close();
				serverSocket.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}

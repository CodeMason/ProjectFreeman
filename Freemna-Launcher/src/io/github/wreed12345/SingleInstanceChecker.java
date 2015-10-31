package io.github.wreed12345;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

import com.esotericsoftware.minlog.Log;

public class SingleInstanceChecker implements Runnable {

	@Override
	public void run() {
		try {
			Socket socket = new Socket("127.0.0.1", 10359);
			DataInputStream dIn = new DataInputStream(socket.getInputStream());
			boolean message = dIn.readBoolean();
			MainLauncher.anotherInstanceRunning = message;
			Log.info("Freeman Client", "Another instance found running");

			socket.close();
		} catch (ConnectException e) {
			Log.info("Freeman Client", "No other instance found running");
			// assume this is ok since if you cant connect that means there is
			// no socket available to connect to :D
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

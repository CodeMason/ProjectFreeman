package io.github.wreed12345.util;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Custom error handler used to send text message
 * alerts to admins phones.
 * @author William Reed
 * @since 0.0.11 2/8/14
 */
public class ErrorHandler extends PrintStream{
	
	private TextMessageSender textMessageSender;
	
	public ErrorHandler(OutputStream out){
		super(out, true);
		textMessageSender =  new TextMessageSender();
	}
	
	@Override
	public void print(String s){
		//figure out how to send only the error type to the phones.
		if(s.contains("Exception")){
			textMessageSender.sendMessage(s);
		}
		super.print(s);
	}

}

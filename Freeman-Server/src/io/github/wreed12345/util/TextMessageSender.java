package io.github.wreed12345.util;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class TextMessageSender {
	
	private ArrayList<String> adresses = new ArrayList<String>();
	private Properties props;
	private Session session;
	
	public TextMessageSender(){
		adresses.add("6313830359@messaging.sprintpcs.com");
		adresses.add("6312359493@vtext.com");
		adresses.add("6319429151@vtext.com");
		
		props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}
	
	private final String username = "goldspringgames@gmail.com";
	private final String password = "mtcgriefteam";
	
	public void sendMessage(String textmessage){
		try {
			
			for(String s : adresses){
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("goldspringgames@gmail.com"));
				message.setRecipients(Message.RecipientType.TO, InternetAddress
						.parse(s));
				message.setText("[Freeman-Alert] " + textmessage);
					
				Transport.send(message);
			}
		}catch(MessagingException e){
			e.printStackTrace();
		}
	}

	
}

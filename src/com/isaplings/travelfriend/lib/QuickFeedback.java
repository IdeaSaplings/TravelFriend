package com.isaplings.travelfriend.lib;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.os.AsyncTask;

class MailFeedback extends AsyncTask<Message, Void, Boolean> {

	@Override
	protected Boolean doInBackground(Message... params) {
		try {
			Transport.send(params[0]);
			// Log.v("Debug", " MYGPS : Mail sent successfully - BG");

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Log.v("Debug", " MYGPS : sending Mail - Failed");
			return false;

		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean flag) {

		if (flag.booleanValue()) {
			//Toast.makeText(getApplication(), "Feedback Sent  !!", Toast.LENGTH_LONG).show();
		}
	}

}

public class QuickFeedback {

	public static void sendMessage(String msgInfo) {

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.user", "isaplings@gmail.com");
		props.put("mail.smtp.debug", "true");

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								"isaplings@gmail.com", "Idea1234");
					}
				});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("isaplings@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("isaplings@gmail.com"));
			message.setSubject("TravelFriend - Feedback");
//			message.setContent("Hi,\n I like your app. Give me more !!!",
//					"text/html; charset=utf-8");
			
			message.setContent(msgInfo,"text/html; charset=utf-8");

			// Transport.send(message);

			MailFeedback mailFeedback = new MailFeedback();
			mailFeedback.execute(message);
		} catch (MessagingException e) {
			// throw new RuntimeException(e);
		}
	}

}
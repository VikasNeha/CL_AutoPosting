package com.PageEvents;

import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GmailInteractor {
	
	public static String getAdActivationURL(String username, String password) throws MyException {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		
		try {
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", username, password);
			//System.out.println(store);

			Folder inbox = store.getFolder("Inbox");
			inbox.open(Folder.READ_ONLY);
			//Message messages[] = inbox.getMessages();
			
			Message message1 = inbox.getMessage(inbox.getMessageCount());
			Multipart mp = (Multipart)message1.getContent();
			int count = mp.getCount();
			BodyPart body_part = null;
			for (int i = 0; i < count; i++) 
				body_part = mp.getBodyPart(i); 
			
			//System.out.println(body_part.getContent());
			String html = body_part.getContent().toString();
			Document doc = Jsoup.parse(html);
			Element link = doc.select("a").first();
			String linkHref = link.attr("href");
			//String linkText = link.text();
			//System.out.println("--" + linkHref);
			//System.out.println("---" + linkText);
			
			return linkHref;
			
		} catch (NoSuchProviderException e) {
			throw new MyException("Problem with Gmail", e);
		} catch (MessagingException e) {
			throw new MyException("Problem with Gmail", e);
		} catch (IOException e) {
			throw new MyException("Problem with Gmail", e);
		}
	}
	
}

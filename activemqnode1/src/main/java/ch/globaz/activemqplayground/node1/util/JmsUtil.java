package ch.globaz.activemqplayground.node1.util;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Date;

public class JmsUtil {

	public static void sendMessage(Session session, MessageProducer producer, String message) {
		// Create a messages
		TextMessage txtMessage = null;

		try {
			txtMessage = session.createTextMessage(message);
			// Tell the producer to send the message
			System.out.println("Sent message: "+ message);
			producer.send(txtMessage);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}

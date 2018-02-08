package ch.globaz.activemqplayground.node2.helloworld;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class JMSConsumer implements ExceptionListener{

    private final static String BROKER_URL = "tcp://localhost:61616";
    private final static String QUEUE_EACH_10_SECONDS = "SECONDS.10";

    public static void main(String[] args) throws JMSException {

        // Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        // Create a Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();
        // Create a Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // Create the destination (Topic or Queue)
        Destination destination = session.createQueue(QUEUE_EACH_10_SECONDS);

        // Create a MessageConsumer from the Session to the Topic or Queue
        MessageConsumer consumer = session.createConsumer(destination);



        Executor service = Executors.newFixedThreadPool(1);

        service.execute(() -> {

            while(true){
                try {
                    listen(consumer);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static void listen(MessageConsumer consumer) throws JMSException {
        // Wait for a message
        Message message = consumer.receive();

        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            System.out.println("Received: " + text);
        } else {
            System.out.println("Received: " + message);
        }

    }

    public void onException(JMSException e) {
        System.out.println("Exception occurs: " + e.getErrorCode());

    }
}

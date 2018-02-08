package ch.globaz.activemqplayground.node1.helloworld;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class JMSProducteur {

    private final static String BROKER_URL = "tcp://localhost:61616";
    private final static String QUEUE_EACH_10_SECONDS = "SECONDS.10";

    public static void main(String[] args) throws Exception{

        // Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        // Create a Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();
        // Create a Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        Destination destination = session.createQueue(QUEUE_EACH_10_SECONDS);
        // Create a MessageProducer from the Session to the Topic or Queue
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);




        Executor service = Executors.newFixedThreadPool(1);

        service.execute(() -> {

            while(true){
                sendMessage(session, producer);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    private static void sendMessage(Session session, MessageProducer producer) {
        // Create a messages
        String text = "Message From: " + Thread.currentThread().getName() + ", time:" + new Date().getTime();
        TextMessage message = null;
        try {
            message = session.createTextMessage(text);
            // Tell the producer to send the message
            System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

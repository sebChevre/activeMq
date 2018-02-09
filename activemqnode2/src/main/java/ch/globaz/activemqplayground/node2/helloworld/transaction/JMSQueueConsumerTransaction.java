package ch.globaz.activemqplayground.node2.helloworld.transaction;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;

import javax.jms.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class JMSQueueConsumerTransaction implements ExceptionListener{

    private final static String BROKER_URL = "tcp://localhost:61616";
    private final static String QUEUE_EACH_10_SECONDS = "SECONDS.10.TRANSAC";

    public static void main(String[] args) throws JMSException {





        RedeliveryPolicy policy = new RedeliveryPolicy();
        policy.setMaximumRedeliveries(3);

       // policy.setMaximumRedeliveryDelay(10000);
        policy.setRedeliveryDelay(10000);



        // Create a ConnectionFactory
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        connectionFactory.setRedeliveryPolicy(policy);
        // Create a Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();
        // Create a Session
        Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
        // Create the destination (Topic or Queue)
        Destination destination = session.createQueue(QUEUE_EACH_10_SECONDS);

        // Create a MessageConsumer from the Session to the Topic or Queue
        MessageConsumer consumer = session.createConsumer(destination);



        Executor service = Executors.newFixedThreadPool(1);

        service.execute(() -> {

            while(true){
                try {
                    listen(consumer);
                    session.rollback();
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
            System.out.println("Received, before rollback: " + text);
        } else {
            System.out.println("Received, before rollback: " + message);
        }

    }

    public void onException(JMSException e) {
        System.out.println("Exception occurs: " + e.getErrorCode());

    }
}

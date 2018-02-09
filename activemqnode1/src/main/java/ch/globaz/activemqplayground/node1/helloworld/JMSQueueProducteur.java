package ch.globaz.activemqplayground.node1.helloworld;

import ch.globaz.activemqplayground.node1.util.JmsMessage;
import ch.globaz.activemqplayground.node1.util.JmsUtil;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class JMSQueueProducteur {

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

            int cpt = 1;

            while(true){
                String message = new JmsMessage("Message n°" + cpt,JMSQueueProducteur.class.getName()).asJsonString();
                JmsUtil.sendMessage(session, producer,message);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cpt++;
            }
        });



    }


}

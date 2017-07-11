import java.io.Closeable;
import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * Connect to ActiveMQ and set Listener to some Queue
 *
 */
public class ConnectToQueueBroker {
    
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        try {
            connectionFactory = new ActiveMQConnectionFactory("tcp://IP:PORT");
            connection = connectionFactory.createConnection();
            connection.start(); //don't forget
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("QueueName");
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(new MessageListener() {
                
                @Override
                public void onMessage(Message message) {
                    System.out.println(message);
                }
            });
        } catch (Exception ex) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

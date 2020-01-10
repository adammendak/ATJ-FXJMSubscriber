package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.Topic;
import com.sun.messaging.jms.JMSException;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.MessageListener;


public class JMSWindowController {

    private static final String URL = "localhost:7676/jmsProducer";
    private static final String TOPIC_NAME = "ATJTopic";

    private Subscriber subscriber;

    @FXML
    TextArea chatTextArea;

    @FXML
    private void initialize() throws javax.jms.JMSException {
        Subscriber subscriber = new Subscriber(URL, TOPIC_NAME);
    }

    public class Subscriber {
        private JMSContext jmsContext;
        private JMSConsumer jmsConsumer;
        private Topic topic;

        Subscriber(String url, String topicName) throws javax.jms.JMSException {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            jmsContext = connectionFactory.createContext();
            topic = new com.sun.messaging.Topic(topicName);
            jmsConsumer = jmsContext.createConsumer(topic);
            jmsConsumer.setMessageListener(new AsynchSubscriber());
        }
    }

    public class AsynchSubscriber implements MessageListener {

        @Override
        public void onMessage(Message message) {
            if (message instanceof TextMessage)
                try {
                    System.out.printf("Odebrano wiadomość:'%s'%n", ((TextMessage) message).getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                } catch (javax.jms.JMSException e) {
                    e.printStackTrace();
                }
        }
    }
}

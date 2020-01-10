package sample;

import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.Topic;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import javax.jms.*;

public class JMSWindowController {

    private static final String URL = "localhost:7676/jmsProducer";
    private static final String TOPIC_NAME = "ATJTopic";

    private Subscriber subscriber;

    @FXML
    TextArea chatTextArea;

    @FXML
    private void initialize() throws JMSException {
        Subscriber subscriber = new Subscriber(URL, TOPIC_NAME);
    }

    public class Subscriber {
        private JMSContext jmsContext;
        private JMSConsumer jmsConsumer;
        private Topic topic;

        Subscriber(String url, String topicName) throws JMSException {
            ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
            jmsContext = connectionFactory.createContext();
            ((com.sun.messaging.ConnectionFactory) connectionFactory)
                    .setProperty(com.sun.messaging.ConnectionConfiguration.imqAddressList, url);
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
                }
        }
    }
}

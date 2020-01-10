package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import javax.jms.*;
import java.io.IOException;


public class JMSWindowController {

    private static final String URL = "mq://localhost:7676/";
    private static final String TOPIC_NAME = "ATJTopic";

    private Subscriber subscriber;

    @FXML
    TextArea chatTextArea;

    @FXML
    private void initialize() throws JMSException {
        Subscriber subscriber = new Subscriber();
    }

    public class Subscriber {

        Subscriber() throws JMSException {
            com.sun.messaging.ConnectionFactory connFactory = new com.sun.messaging.ConnectionFactory();
            connFactory.setProperty(com.sun.messaging.ConnectionConfiguration.imqAddressList, URL);
            TopicConnection connection = connFactory.createTopicConnection();
            TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = new com.sun.messaging.Topic(TOPIC_NAME);
            TopicSubscriber subscriber = session.createSubscriber(topic);
            subscriber.setMessageListener(new AsynchSubscriber());
            connection.start();
        }
    }

    public class AsynchSubscriber implements MessageListener {

        @Override
        public void onMessage(Message message) {
            if (message instanceof TextMessage) {
                try {
                    chatTextArea.appendText("Wiadomośc z servera: " + ((TextMessage) message).getText() + "\n");
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

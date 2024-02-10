package com.labs.jms;
 
import java.util.Map;
// import java.util.logging.Level;
import java.util.logging.Logger;

// import javax.jms.Connection;
// import javax.jms.ConnectionFactory;
// import javax.jms.DeliveryMode;
// import javax.jms.Destination;
// import javax.jms.JMSContext;
import javax.jms.JMSException;
// import javax.jms.JMSProducer;
import javax.jms.Message;
// import javax.jms.MessageProducer;
import javax.jms.MapMessage;
import javax.jms.Session;
// import javax.jms.TextMessage;
// import javax.jms.Topic;
// import javax.jms.TopicConnection;
// import javax.jms.TopicPublisher;
// import javax.jms.TopicSession;
// import javax.naming.InitialContext;
// import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.labs.logging.ChangeType;
 
@Component
public class MessageSender {
    private static final Logger logger = Logger.getLogger(MessageSender.class.getName());

    @Autowired
    private JmsTemplate jmsTemplate;

    //@Autowired
    //private ConnectionFactory connectionFactory;

    //@Autowired
    //private Destination topic;

    public void sendMessage(ChangeType changeType, String entityClass, Long entityId, Map<String, Object> fields) {
        logger.info("sending a message");
        
        jmsTemplate.send(new MessageCreator(){
                @Override
                public Message createMessage(Session session) throws JMSException {
                    
                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setString("changeType", changeType.toString());
                    mapMessage.setString("entityClass", entityClass);
                    mapMessage.setLong("entityId", entityId);

                    for (Map.Entry<String, Object> entry : fields.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();

                        mapMessage.setObject(key, value);
                    }
                    
                    return mapMessage;
                }
            });
        
        /* 
        try {
            InitialContext cntxt = new InitialContext();
            //System.out.println("Context Created");
            
            ConnectionFactory connectionFactory = (ConnectionFactory) cntxt.lookup("jms/ConnectionFactory");
            Destination topic = (Destination) cntxt.lookup("jms/topic");
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer publisher = session.createProducer((Topic) topic);
            publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("changeType", changeType.toString());
            mapMessage.setString("entityClass", entityClass);
            mapMessage.setLong("entityId", entityId);

            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                mapMessage.setObject(key, value);
            }

            publisher.send(mapMessage);

            
            // logger.info("JMS Correlation ID: " + mapMessage.getJMSCorrelationID());
            // logger.info("JMS Message ID: " + mapMessage.getJMSMessageID());
            // logger.info("JMS Delivery Mode: " + String.valueOf(mapMessage.getJMSDeliveryMode()));
            // logger.info("JMS Destination: " + mapMessage.getJMSDestination().toString());
            // logger.info("JMS Redeliviered: " + String.valueOf(mapMessage.getJMSRedelivered()));
            

            session.close();
            System.out.println("Message sent.");
        } catch (JMSException | NamingException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        */
        /* 
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            MessageProducer publisher = session.createProducer(topic);
            //publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("changeType", changeType.toString());
            mapMessage.setString("entityClass", entityClass);
            mapMessage.setLong("entityId", entityId);

            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                mapMessage.setObject(key, value);
            }

            publisher.send(mapMessage);

            publisher.close();
            session.close();
            connection.close();
            
            
            System.out.println("Message sent.");
        } catch (JMSException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        */
        
    }
 
}
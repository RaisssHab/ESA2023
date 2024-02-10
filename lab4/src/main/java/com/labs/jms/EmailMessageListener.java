package com.labs.jms;

import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
//import javax.jms.MessageListener;
import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.labs.logging.Notification;
import com.labs.service.EmailService;
import com.labs.service.NotificationService;

import java.util.HashSet;
import java.util.Set;

@Component
public class EmailMessageListener { // implements MessageListener {

    private static final Logger logger = Logger.getLogger(EmailMessageListener.class.getName());

    private static Set<String> processedMessageIds = new HashSet<>();

    @Autowired
    NotificationService notificationService;

    @Autowired
    EmailService emailService;

    @JmsListener(destination = "topic", containerFactory = "jmsListenerContainerFactory")
    public void onMessage(Message message) {
        try {
            logger.info("got a message");
            if (message instanceof MapMessage) {
                MapMessage mapMessage = (MapMessage) message;
                String messageId = message.getJMSMessageID();

                if (processedMessageIds.contains(messageId)) {
                    logger.info("Duplicate message detected. Skipping processing.");
                    return;
                }

                String changeType = mapMessage.getString("changeType");
                String entityClass = mapMessage.getString("entityClass");
                Long entityId = mapMessage.getLong("entityId");

                Notification notification;

                try {
                    notification = notificationService.getNotificationByEntityName(entityClass);
                } catch (NoResultException ex) {
                    logger.info("No address found for this entity: " + entityClass);
                    return;
                }
                emailService.sendLogMessage(notification.getEmail(), changeType, entityClass, entityId);

                processedMessageIds.add(messageId);
                //logger.info("Message processed successfully.");
            }
        } catch (JMSException e) {
            logger.severe("Error processing JMS message"+ e);
        }
    }
}

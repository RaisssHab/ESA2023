package com.labs.jms;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
//import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
//import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.labs.logging.ChangeLog;
import com.labs.logging.ChangeType;
import com.labs.service.ChangeLogService;

import java.util.logging.Logger;

@Component
public class LogMessageListener { // implements MessageListener {
    private static final Logger logger = Logger.getLogger(LogMessageListener.class.getName());

    //private UUID listenerId = UUID.randomUUID();
    private static Boolean isInitialized = false;

    public LogMessageListener() {
        logger.info("logmessagelistener instantiated");
        synchronized(this) {
            if (isInitialized) {
                throw new InstantiationError("already initialized once");
            }
            isInitialized = true;
        }
    }
    
    @Autowired
    ChangeLogService changeLogService;

    @JmsListener(destination = "topic", containerFactory = "jmsListenerContainerFactory")
    public void onMessage(Message message) {
        try {
            //logger.info("my ID: " + listenerId.toString());
            //logger.info("got a message");
            if (message instanceof MapMessage) {
                logger.info("got a message of type MapMessage");

                MapMessage mapMessage = (MapMessage) message;
                /* 
                logger.info("JMS Correlation ID: " + mapMessage.getJMSCorrelationID());
                logger.info("JMS Message ID: " + mapMessage.getJMSMessageID());
                logger.info("JMS Delivery Mode: " + String.valueOf(mapMessage.getJMSDeliveryMode()));
                logger.info("JMS Destination: " + mapMessage.getJMSDestination().toString());
                logger.info("JMS Redeliviered: " + String.valueOf(mapMessage.getJMSRedelivered()));
                */

                String changeType = mapMessage.getString("changeType");
                String entityClass = mapMessage.getString("entityClass");
                Long entityId = mapMessage.getLong("entityId");

                Map<String, Object> fields = new HashMap<>();
                Enumeration<String> mapNames = mapMessage.getMapNames();

                while (mapNames.hasMoreElements()) {
                    String key = mapNames.nextElement();

                    if (key.equals("changeType") || key.equals("entityClass") || key.equals("entityId")) {
                        continue;
                    } else {
                        Object value = mapMessage.getObject(key);
                        fields.put(key, value);
                    }
                }

                if (changeType.equals(ChangeType.DELETE.toString())) {
                    ChangeLog changeLog = new ChangeLog();
                    changeLog.setChangeType(changeType);
                    changeLog.setEntityClass(entityClass);
                    changeLog.setEntityId(entityId);
                    changeLogService.createLog(changeLog);
                } else {
                    for (Map.Entry<String, Object> entry : fields.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();

                        ChangeLog changeLog = new ChangeLog();
                        changeLog.setChangeType(changeType);
                        changeLog.setEntityClass(entityClass);
                        changeLog.setEntityId(entityId);
                        changeLog.setFieldName(key);
                        changeLog.setNewValue(value.toString());

                        changeLogService.createLog(changeLog);
                    }
                }
            }
        } catch (JMSException e) {
            logger.severe("Error processing JMS message" + e);
        }
    }
}
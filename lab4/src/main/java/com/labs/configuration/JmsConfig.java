package com.labs.configuration;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.ComponentScan;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
// import org.springframework.jms.connection.TransactionAwareConnectionFactoryProxy;
import org.springframework.jms.core.JmsTemplate;
// import org.springframework.jms.listener.DefaultMessageListenerContainer;
// import org.springframework.jndi.JndiObjectFactoryBean;
// import org.springframework.jndi.JndiTemplate;

// import com.labs.jms.DefaultErrorHandler;

/* 
@Configuration
@EnableJms
@ComponentScan(basePackages="com.labs.jms")
*/
public class JmsConfig {
    
    @Bean
    public ConnectionFactory connectionFactory() {

        InitialContext ctx;
        try {
            ctx = new InitialContext();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        // lookup the queue connection factory
        ConnectionFactory connFactory;
        try {
            connFactory = (ConnectionFactory) ctx.lookup("jms/ConnectionFactory");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        return connFactory;

    
        /* 
        JndiObjectFactoryBean connectionFactory = new JndiObjectFactoryBean();
        connectionFactory.setJndiName("jms/ConnectionFactory");
        connectionFactory.setResourceRef(true);
        
        try {
            connectionFactory.afterPropertiesSet();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        return (ConnectionFactory) connectionFactory.getObject();
        */
    
    
    }

    @Bean
    public Destination destination() {
        InitialContext ctx;
        try {
            ctx = new InitialContext();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        try {
            return (Destination) ctx.lookup("jms/topic");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        /* 
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName("jms/topic");
        jndiObjectFactoryBean.setResourceRef(true);
        try {
            jndiObjectFactoryBean.afterPropertiesSet();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        return (Destination) jndiObjectFactoryBean.getObject();
        */
    }
 
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, Destination destination) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        jmsTemplate.setDefaultDestination(destination);
        jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
        
        return jmsTemplate;
    }

    @Bean(name="jmsListenerContainerFactory")
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true); // Set it to true for topics
        //factory.setErrorHandler(defaultErrorHandler);
        return factory;
    }
}

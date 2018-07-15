package com.zhang.order.dao;

import com.google.gson.Gson;
import com.zhang.common.dto.OrderDTO;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.TransactionAwareConnectionFactoryProxy;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.ConnectionFactory;
import java.util.HashMap;
import java.util.Map;
@EnableJms
@Configuration
public class JmsConfig {

    @Bean
    public Gson gson(){
        return new Gson();
    }

    @Bean
    public ConnectionFactory connectionFactory(){
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616");
        cf.setTrustAllPackages( true );
        TransactionAwareConnectionFactoryProxy factoryProxy = new TransactionAwareConnectionFactoryProxy();
        factoryProxy.setTargetConnectionFactory( cf );
        factoryProxy.setSynchedLocalTransactionAllowed( true );
        return cf;
    }

    @Bean
    public JmsTemplate jmsTemplate( ConnectionFactory connectionFactory){
        JmsTemplate jmsTemplate = new JmsTemplate( connectionFactory );
        jmsTemplate.setSessionTransacted( true );
        return jmsTemplate;
    }

    @Bean
    public JmsListenerContainerFactory<?> msgFactory (ConnectionFactory cf ,
                                                      PlatformTransactionManager transactionManager,
                                                      DefaultJmsListenerContainerFactoryConfigurer configurer){
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure( factory , cf );
        factory.setReceiveTimeout( 10000l );
        factory.setTransactionManager( transactionManager );
        factory.setConcurrency( "10" );
        factory.setMessageConverter( jacksonJmsMessageConverter() );
        return factory;
    }

    @Bean
    public  MessageConverter jacksonJmsMessageConverter(){
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType( MessageType.TEXT);
        converter.setTypeIdPropertyName( "_type" );
//        converter.setTargetType( MessageType.TEXT );
//        Map<String, Class<?>> typeIdMap = new HashMap<>();
//        typeIdMap.put("Order", OrderDTO.class);
//        converter.setTypeIdMappings(typeIdMap);
//        converter.setTypeIdPropertyName("Order");
//        converter.setEncoding("UTF-8");
        return converter;
    }
}

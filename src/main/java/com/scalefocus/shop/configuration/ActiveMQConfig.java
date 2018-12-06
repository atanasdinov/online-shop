package com.scalefocus.shop.configuration;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;


/**
 * <b>A class providing configuration beans used for ActiveMQ configuration.</b>
 */
@Configuration
@EnableJms
@PropertySource(value = {"classpath:application.properties"})
public class ActiveMQConfig {

    private static final String ACTIVEMQ_USERNAME = "activemq.username";
    private static final String ACTIVEMQ_PASSWORD = "activemq.password";
    private static final String ACTIVEMQ_BROKER_URL = "activemq.broker.url";
    private static final String ACTIVEMQ_QUEUE_NAME = "activemq.queue.name";

    @Autowired
    private Environment env;

    /**
     * ActiveMQConnectionFactory configuration bean.
     *
     * @return {@link ActiveMQConnectionFactory} object with configured BrokerURL and credentials.
     */
    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setUserName(env.getProperty(ACTIVEMQ_USERNAME));
        factory.setPassword(env.getProperty(ACTIVEMQ_PASSWORD));
        factory.setBrokerURL(env.getProperty(ACTIVEMQ_BROKER_URL));
        factory.setTrustAllPackages(true);
        return factory;
    }

    /**
     * MessageConverter configuration bean.
     *
     * @return {@link MessageConverter} bean with a {@link XmlMapper} object mapper.
     */
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTypeIdPropertyName("_Type");
        converter.setObjectMapper(new XmlMapper());
        return converter;
    }

    /**
     * JmsTemplate configuration bean.
     *
     * @param activeMQConnectionFactory  used for JmsTemplate creation/instantiation.
     * @param jacksonJmsMessageConverter used to set the given message converter for the JmsTemplate.
     * @return {@link JmsTemplate} object.
     */
    @Bean
    public JmsTemplate jmsTemplate(ActiveMQConnectionFactory activeMQConnectionFactory, MessageConverter jacksonJmsMessageConverter) {
        JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory());
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter);
        return jmsTemplate;
    }

    /**
     * ActiveMQ queue configuration bean.
     *
     * @return a {@link Queue} instance with a given name.
     */
    @Bean
    public Queue getQueue() {
        return new ActiveMQQueue(env.getProperty(ACTIVEMQ_QUEUE_NAME));
    }

    /**
     * Configures a {@link DefaultJmsListenerContainerFactory} and sets a {@link MessageConverter} and
     * {@link ConnectionFactory}.
     *
     * @param connectionFactory          {@link ConnectionFactory}
     * @param configurer                 {@link DefaultJmsListenerContainerFactoryConfigurer}
     * @param jacksonJmsMessageConverter {@link MessageConverter}
     * @return DefaultJmsListenerContainerFactory object.
     */
    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer configurer,
                                                    MessageConverter jacksonJmsMessageConverter) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setMessageConverter(jacksonJmsMessageConverter);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

}
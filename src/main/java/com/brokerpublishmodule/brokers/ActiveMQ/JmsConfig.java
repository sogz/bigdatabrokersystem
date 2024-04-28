package com.brokerpublishmodule.brokers.ActiveMQ;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;


@Configuration
public class JmsConfig {

  @Value("${spring.activemq.broker-url}")
  private String broker_url;

  @Value("${spring.activemq.user}")
  private String broker_username;

  @Value("${spring.activemq.password}")
  private String broker_password;


  @Bean
  public ActiveMQConnectionFactory connectionFactory() {
    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
    connectionFactory.setBrokerURL(broker_url);
    connectionFactory.setPassword(broker_username);
    connectionFactory.setUserName(broker_password);
    return connectionFactory;
  }

  @Bean
  @Qualifier("queueJmsTemplate")
  public JmsTemplate jmsQueueTemplate() {
    JmsTemplate template = new JmsTemplate();
    template.setConnectionFactory(connectionFactory());
    return template;
  }

  @Bean(name = "queueListenerFactory")
  public DefaultJmsListenerContainerFactory jmsListenerQueueContainerFactory() {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory());
    return factory;
  }
}

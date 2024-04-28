package com.brokerpublishmodule;

import com.brokerpublishmodule.brokers.RabbitMQ.base.ConsumerRegistry;
import com.brokerpublishmodule.business.ConstantQueue;
import com.brokerpublishmodule.consumers.RabbitMQLocationConsumer;
import com.brokerpublishmodule.enums.BrokerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Collections;

@SpringBootApplication
@EnableCassandraRepositories
public class BrokerPublishAPI {

    final
    ConsumerRegistry registry;

    static ConsumerRegistry staticRegistry;
    private static final Logger logger = LoggerFactory.getLogger(BrokerPublishAPI.class);

    public BrokerPublishAPI(ConsumerRegistry registry) {
        this.registry = registry;
        staticRegistry = registry;
    }

    public static void main(String[] args) {
        try {
            SpringApplication app = new SpringApplication(BrokerPublishAPI.class);
            app.setDefaultProperties(Collections.singletonMap("spring.autoconfigure.exclude", "org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration,org.springframework.boot.autoconfigure.activemq.ActiveMQAutoConfiguration"));

            ConfigurableApplicationContext context;

            String activateBrokerType = System.getenv("BROKER_TYPE");
            BrokerType brokerType = BrokerType.valueOf(activateBrokerType);
            switch (brokerType) {
                case ACTIVEMQ:
                    context = app.run(args);
                    app.setDefaultProperties(Collections.singletonMap("spring.autoconfigure.exclude", "org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"));


                    logger.info("ActiveMQ broker is activated");
                    break;
                case RABBITMQ:
                    context = app.run(args);

                    staticRegistry.Register(RabbitMQLocationConsumer.class.getName(), ConstantQueue.RabbitMQ_IOT_CONSUMER, 0);
                    break;
                case KAFKA:
                    app.setDefaultProperties(Collections.singletonMap("spring.autoconfigure.exclude", "org.springframework.boot.autoconfigure.activemq.ActiveMQAutoConfiguration"));

                    context = app.run(args);
                    break;
                default:
                    logger.error("Invalid broker type: " + brokerType + " queue not consumed!");
            }

        } catch (Exception e) {
            logger.error("BrokerPublishAPI main method error: ", e);
        }
    }
}

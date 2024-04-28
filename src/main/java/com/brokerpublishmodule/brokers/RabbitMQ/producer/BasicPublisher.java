package com.brokerpublishmodule.brokers.RabbitMQ.producer;

import com.brokerpublishmodule.brokers.RabbitMQ.base.RabbitMQConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

public class BasicPublisher {

    private BasicPublisher() {
    }
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BasicPublisher.class);

    public static <T> boolean Publish(String queueName, T message) {
        try {
            if (queueName == null && queueName.isEmpty()) {
                throw new Exception("Queue name was not null!");
            }
            try (Channel channel = RabbitMQConnection.getInstance().GetChannel(queueName)) {

                ObjectMapper mapper = new ObjectMapper();
                String jsonData = mapper.writeValueAsString(message);
                BasicProperties properties = new AMQP.BasicProperties.Builder()
                        .deliveryMode(2)
                        .build();
                channel.basicPublish("", queueName, (AMQP.BasicProperties) properties, jsonData.getBytes(StandardCharsets.UTF_8));

            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(),ex);
            return false;
        }
        return true;
    }
}

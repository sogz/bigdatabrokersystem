package com.brokerpublishmodule.services;

import com.brokerpublishmodule.entities.LocationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "broker", name = "type", havingValue = "kafka")
public class KafkaService implements IBrokerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQService.class);


    private KafkaTemplate<String, LocationEntity> kafkaTemplate;

    @Autowired(required = false)
    public KafkaService(@Autowired(required = false) KafkaTemplate<String, LocationEntity> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void generateAndPublishMock(LocationEntity locationEntity) {
        // Publish the locationEntity to the RabbitMQ
        try {
            Message<LocationEntity> message = MessageBuilder
                    .withPayload(locationEntity)
                    .setHeader(KafkaHeaders.TOPIC, "locationEntityTopic")
                    .build();

            kafkaTemplate.send(message);
        } catch (Exception ex) {
            LOGGER.error("Mock veri KAFKA ile yayınlanamadı: " + locationEntity + "Error Message: " + ex.getMessage());
        }
    }
}
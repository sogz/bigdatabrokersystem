package com.brokerpublishmodule.services;

import com.brokerpublishmodule.brokers.RabbitMQ.producer.BasicPublisher;
import com.brokerpublishmodule.business.ConstantQueue;
import com.brokerpublishmodule.entities.LocationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService implements IBrokerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQService.class);

    @Override
    public void generateAndPublishMock(LocationEntity locationEntity) {
        // Publish the locationEntity to the RabbitMQ
        if (BasicPublisher.Publish(ConstantQueue.RabbitMQ_IOT_CONSUMER, locationEntity)) {
            LOGGER.debug("Mock veri RabbitMQ ile yayınlandı: " + locationEntity);
        } else {
            // if error occurs while publishing the locationEntity to the RabbitMQ
            // you should execute error business logic
            LOGGER.error("Mock veri RabbitMQ ile yayınlanamadı: " + locationEntity);
        }
    }
}
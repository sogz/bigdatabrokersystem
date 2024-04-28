package com.brokerpublishmodule.services;

import com.brokerpublishmodule.brokers.ActiveMQ.jms.Producer;
import com.brokerpublishmodule.entities.LocationEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public class ActiveMQService implements IBrokerService {
    final Producer producer;

    public ActiveMQService(Producer producer) {
        this.producer = producer;
    }

    @Override
    public void generateAndPublishMock(LocationEntity locationEntity) throws JsonProcessingException {

        producer.sendMessageToQueue(locationEntity);
        System.out.println("Mock veri ActiveMQ ile yayınlandı: " + locationEntity);
    }
}
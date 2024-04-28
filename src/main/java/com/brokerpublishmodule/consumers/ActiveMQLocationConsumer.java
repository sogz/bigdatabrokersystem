package com.brokerpublishmodule.consumers;

import com.brokerpublishmodule.brokers.ActiveMQ.jms.Listener;
import com.brokerpublishmodule.entities.LocationEntity;
import com.brokerpublishmodule.services.LocationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ActiveMQLocationConsumer implements Listener {

    @Value(value = "${simple-activemq.queue}")
    private String queue;

    public ActiveMQLocationConsumer(LocationService locationService) {
        this.locationService = locationService;
    }
    LocationService locationService;

    @JmsListener(destination = "${simple-activemq.queue}", containerFactory = "queueListenerFactory")
    public void receiveMessageFromQueue(Message jsonMessage) throws JMSException, JsonProcessingException {

        TextMessage textMessage = (TextMessage) jsonMessage;
        String messageData = textMessage.getText();

        ObjectMapper mapper = new ObjectMapper();
        LocationEntity parsedData = mapper.readValue(messageData, LocationEntity.class);

        locationService.saveLocationEntity(parsedData);

        log.info("Received message: " + messageData + " from queue - " + queue);
    }
}
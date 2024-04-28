package com.brokerpublishmodule.brokers.ActiveMQ.jms.impl;

import com.brokerpublishmodule.brokers.ActiveMQ.jms.Producer;
import com.brokerpublishmodule.entities.LocationEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@Component
@RestController
public class ProducerImpl implements Producer {

  @Autowired
  @Qualifier("queueJmsTemplate")
  private JmsTemplate queueJmsTemplate;

  @Value("${simple-activemq.queue}")
  private String queue;


  public void sendMessageToQueue(LocationEntity locationEntity) throws JsonProcessingException {

    log.info("Sending message " + locationEntity + " to queue - " + queue);
    ObjectMapper mapper = new ObjectMapper();
    String jsonData = mapper.writeValueAsString(locationEntity);

    queueJmsTemplate.convertAndSend(queue, jsonData);
  }
}


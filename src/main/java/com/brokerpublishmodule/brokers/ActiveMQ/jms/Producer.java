package com.brokerpublishmodule.brokers.ActiveMQ.jms;


import com.brokerpublishmodule.entities.LocationEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface Producer {

  void sendMessageToQueue(LocationEntity message) throws JsonProcessingException;
}

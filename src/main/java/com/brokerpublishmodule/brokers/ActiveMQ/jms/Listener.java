package com.brokerpublishmodule.brokers.ActiveMQ.jms;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.jms.JMSException;
import org.apache.activemq.Message;



public interface Listener {

  void receiveMessageFromQueue(Message jsonMessage) throws JMSException, JsonProcessingException;

}

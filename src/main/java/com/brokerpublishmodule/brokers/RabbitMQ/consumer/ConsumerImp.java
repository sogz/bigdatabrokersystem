package com.brokerpublishmodule.brokers.RabbitMQ.consumer;

public interface ConsumerImp {

    String getListenQueueName();

    void setListenQueueName(String listenQueueName);

    boolean getAutoAck ();

    void setAutoAck (boolean autoAck);

    short getPrefectCount();

    void setPrefectCount(short prefectCount);
}

package com.brokerpublishmodule.brokers.RabbitMQ.base;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class RabbitMQConnection {

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RabbitMQConnection.class);

    private static final ReentrantLock lock = new ReentrantLock();

    private com.rabbitmq.client.Connection Connection;

    private boolean getIsConnected(){
        return Connection != null && Connection.isOpen();
    }

    private static final RabbitMQConnection instance = new RabbitMQConnection();

    public static RabbitMQConnection getInstance() {
        return instance;
    }

    public RabbitMQConnection() {
        GetConnection();
    }

    public com.rabbitmq.client.Connection GetConnection() {
        if (getIsConnected())
            return Connection;
        try {
            if (getIsConnected())
                return Connection;
            lock.lock();
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setUri(System.getenv("RABBITMQ_URI"));
            Connection = connectionFactory.newConnection();
            Connection.addShutdownListener(listener->{
                LOGGER.error(listener.getMessage());
            });
            return Connection;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(),ex);
        } finally {
            lock.unlock();
        }
        return null;
    }

    public Channel GetChannel(String queueName) throws IOException {
        GetConnection();
        Channel channel = Connection.createChannel();
        channel.queueDeclare(queueName, true, false, false, null);
        return channel;
    }

    public Channel GetChannel(String queueName, int maxPriority) {

        try {
            Channel channel = Connection.createChannel();
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("x-max-priority", maxPriority);
            channel.queueDeclare(queueName, true, false, false, hashMap);

            return channel;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(),ex);
        }
        return null;
    }
}

package com.brokerpublishmodule.brokers.RabbitMQ.consumer;

import com.brokerpublishmodule.brokers.RabbitMQ.base.RabbitMQConnection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BasicConsumer<T> implements ConsumerImp {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(BasicConsumer.class);
    private Class<T> type;
    private String listenQueueName;
    private short prefectCount;
    private boolean autoAck;
    private DeliverCallback _deliveryCallBack;
    private Channel _rabitMqChannel;
    private int MaxPriority;

    public String getListenQueueName() {
        return listenQueueName;
    }

    public void setListenQueueName(String listenQueueName) { this.listenQueueName = listenQueueName; }

    public short getPrefectCount() {
        return prefectCount;
    }

    public void setPrefectCount(short prefectCount) {
        this.prefectCount = prefectCount;
    }

    public boolean getAutoAck() {
        return autoAck;
    }

    public void setAutoAck(boolean autoAck) {
        this.autoAck = autoAck;
    }

    public int getMaxPriority() {
        return MaxPriority;
    }

    public void setMaxPriority(int maxPriority) {
        MaxPriority = maxPriority;
    }

    public void setClassType(Class<T> type) {
        this.type = type;
    }

    public abstract void DataReceivedMethod(T sender, Delivery e) throws IOException;

    private void setValues(String listenQueueName, short prefectCount, boolean autoAck, int maxPriority, Class<T> type) {
        setListenQueueName(listenQueueName);
        setPrefectCount(prefectCount);
        setAutoAck(autoAck);
        setMaxPriority(maxPriority);
        setClassType(type);
    }

    public BasicConsumer(String listenQueueName, short prefectCount, boolean autoAck, int maxPriority, Class<T> type) {
        setValues(listenQueueName, prefectCount, autoAck, maxPriority, type);

        try {

            if (getListenQueueName() == null) {
                throw new Exception("ListenQueueName was not null!");
            }

            _rabitMqChannel = getMaxPriority() > 0
                    ? RabbitMQConnection.getInstance().GetChannel(getListenQueueName(), getMaxPriority())
                    : RabbitMQConnection.getInstance().GetChannel(getListenQueueName());

            if (getPrefectCount() != 0)
                _rabitMqChannel.basicQos(0, getPrefectCount(), false);

            _deliveryCallBack = this::DocumentConsumerOnReceived;

            _rabitMqChannel.basicConsume(getListenQueueName(), getAutoAck(), _deliveryCallBack, consumerTag -> {
            });
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(),ex);
        }
    }

    private void DocumentConsumerOnReceived(String consumerTag, Delivery e) throws IOException {

        String jsonData = new String(e.getBody(), StandardCharsets.UTF_8);
        try {

            ObjectMapper mapper = new ObjectMapper();
            T parsedData = mapper.readValue(jsonData, type);
            DataReceivedMethod(parsedData, e);
        } catch (Exception ex) {
            _rabitMqChannel.basicNack(e.getEnvelope().getDeliveryTag(), false, false);
            LOGGER.error(ex.getMessage(),ex);
        }
    }

    protected void BasicAck(long deliveryTag, boolean multiple) throws Exception {
        _rabitMqChannel.basicAck(deliveryTag, multiple);
    }
}

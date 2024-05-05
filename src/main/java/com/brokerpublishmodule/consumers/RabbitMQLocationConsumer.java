package com.brokerpublishmodule.consumers;

import com.brokerpublishmodule.brokers.RabbitMQ.consumer.BasicConsumer;
import com.brokerpublishmodule.entities.LocationEntity;
import com.brokerpublishmodule.services.LocationService;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.Random;

public class RabbitMQLocationConsumer extends BasicConsumer<LocationEntity> {
    public RabbitMQLocationConsumer(String listenQueueName, LocationService locationService) {
        super(listenQueueName, (short) 1, false, 0, LocationEntity.class);
        this.locationService = locationService;
    }

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(RabbitMQLocationConsumer.class);

    private final LocationService locationService;

    @Override
    public void DataReceivedMethod(LocationEntity sender, Delivery e) throws IOException {
        try {

            // other process yapay zeka modülünü çalıştır vb.
            //
            var systemTime = System.currentTimeMillis();

            sender.setNearPointId(sender.findNearestPointId(sender, locationService.getAllLocationEntity(), new Random().nextDouble() * 10));

            sender.setCalculatedMs((System.currentTimeMillis() - systemTime));
            //save data
            locationService.saveLocationEntity(sender);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            try {
                BasicAck(e.getEnvelope().getDeliveryTag(), false);
            } catch (Exception ackEx) {
                LOGGER.error(ackEx.getMessage(), ackEx);
            }
        }
    }
}

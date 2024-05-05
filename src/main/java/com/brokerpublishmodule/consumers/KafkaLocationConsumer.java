package com.brokerpublishmodule.consumers;


import com.brokerpublishmodule.entities.LocationEntity;
import com.brokerpublishmodule.services.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class KafkaLocationConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaLocationConsumer.class);
    private final LocationService locationService;

    public KafkaLocationConsumer(LocationService locationService) {
        this.locationService = locationService;
    }
    @KafkaListener(topics = "locationEntityTopic", groupId = "myGroup")
    public void consumeJson(LocationEntity locationentity) {
        LOGGER.info(String.format("locationEntity: %s",locationentity.toString()));
        System.out.println("locationEntity: " + locationentity.toString());
        var systemTime = System.currentTimeMillis();

        locationentity.setNearPointId(locationentity.findNearestPointId(locationentity, locationService.getAllLocationEntity(), new Random().nextDouble() * 10));
        locationentity.setCalculatedMs((System.currentTimeMillis() - systemTime));

        locationService.saveLocationEntity(locationentity);
    }
}

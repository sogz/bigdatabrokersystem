package com.brokerpublishmodule.controller;

import com.brokerpublishmodule.entities.LocationEntity;
import com.brokerpublishmodule.enums.BrokerType;
import com.brokerpublishmodule.services.ActiveMQService;
import com.brokerpublishmodule.services.KafkaService;
import com.brokerpublishmodule.services.LocationService;
import com.brokerpublishmodule.services.RabbitMQService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;
    private final ActiveMQService activeMQService;
    private final RabbitMQService rabbitMQService;
    private final KafkaService kafkaService;

    @Autowired
    public LocationController(LocationService locationService, @Autowired(required = false) ActiveMQService activeMQService,
                              @Autowired(required = false) RabbitMQService rabbitMQService, @Autowired(required = false) KafkaService kafkaService) {
        this.locationService = locationService;
        this.activeMQService = activeMQService;
        this.rabbitMQService = rabbitMQService;
        this.kafkaService = kafkaService;
    }

    @GetMapping("/generateAndPublish")
    public void generateAndPublish(@RequestParam BrokerType brokerType, @RequestParam String testGroupId) throws JsonProcessingException {
        LocationEntity mockLocation = locationService.generateMockLocation(brokerType);
        if(testGroupId == null || testGroupId.isBlank()){
            throw new IllegalArgumentException("Test grubu id boş olamaz.");
        }
        mockLocation.setTestGroupId(testGroupId);
        switch (brokerType) {
            case ACTIVEMQ:
                activeMQService.generateAndPublishMock(mockLocation);
                break;
            case RABBITMQ:
                rabbitMQService.generateAndPublishMock(mockLocation);
                break;
            case KAFKA:
                kafkaService.generateAndPublishMock(mockLocation);
                break;
            default:
                throw new IllegalArgumentException("Geçersiz broker tipi: " + brokerType);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<byte[]> getLocationsByDate(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) throws IOException, IOException {
        Timestamp startTimestamp = Timestamp.valueOf(startDate);
        Timestamp endTimestamp = Timestamp.valueOf(endDate);

        List<LocationEntity> locations = locationService.getByTimeBetween(startTimestamp, endTimestamp);
        byte[] excelData = locationService.locationsToExcel(locations);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("locations.xlsx").build());

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }
}

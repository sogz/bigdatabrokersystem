package com.brokerpublishmodule.services;

import com.brokerpublishmodule.entities.LocationEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface IBrokerService {
    void generateAndPublishMock(LocationEntity locationEntity) throws JsonProcessingException;
}

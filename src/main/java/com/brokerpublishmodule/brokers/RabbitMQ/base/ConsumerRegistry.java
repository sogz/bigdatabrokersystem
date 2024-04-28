package com.brokerpublishmodule.brokers.RabbitMQ.base;

import com.brokerpublishmodule.brokers.RabbitMQ.consumer.ConsumerImp;
import com.brokerpublishmodule.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.util.*;

import java.util.ArrayList;

@Service
public class ConsumerRegistry {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ConsumerRegistry.class);

    private final ArrayList<String> ConsumerList = new ArrayList<>();

    @Autowired
    private  LocationService locationService ;


    public ConsumerRegistry(LocationService locationService){
        this.locationService = locationService;
    }
    public void Register(String className, @Nullable String queueName, @Nullable int parallelismCount) throws Exception {

        if (parallelismCount == 0)
            parallelismCount = 1;

        Class<?> aClass = Class.forName(className);
        if (!ClassUtils.isAssignable(ConsumerImp.class, aClass)) {
            throw new Exception("Consumer class must be inherited from ConsumerImp.");
        }
        try {
            for (int i = 0; i < parallelismCount; i++) {
                int finalI = i;
                if (ConsumerList.stream().anyMatch(x -> (x.getClass().getName() + "_" + finalI).equals(className)))
                    throw new Exception("Consumer already registered.");
                Object object;
                assert queueName != null;
                if (!queueName.isEmpty()) {
                    object = aClass.getDeclaredConstructor(String.class,LocationService.class).newInstance(queueName,locationService);
                } else {
                    object = aClass.getDeclaredConstructor().newInstance();
                }
                ConsumerImp consumerObject = (ConsumerImp) object;
                String consumerName = consumerObject.getClass().getName() + "_" + i;

                ConsumerList.add(consumerName);
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}

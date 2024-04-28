package com.brokerpublishmodule.entities;

import com.brokerpublishmodule.enums.BrokerType;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Timestamp;
import java.util.*;

@Table
public class LocationEntity {


    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;
    @Column("city_name")
    private String cityName;
    @Column("neighborhood_name")
    private String neighborhoodName;
    @Column("latitude")
    private double latitude;
    @Column("longitude")
    private double longitude;

    @Column("timestamp")
    private Timestamp createdDate;

    @Column("brokerType")
    private BrokerType brokerType;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getNeighborhoodName() {
        return neighborhoodName;
    }

    public void setNeighborhoodName(String neighborhoodName) {
        this.neighborhoodName = neighborhoodName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }
    //set Id
    public void setId(String id) {
        this.id = id;
    }
    //get CreatedDate
    public Timestamp getCreatedDate() {
        return createdDate;
    }
    //set CreatedDate
    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    //get BrokerType
    public BrokerType getBrokerType() {
        return brokerType;
    }
    //set BrokerType
    public void setBrokerType(BrokerType brokerType) {
        this.brokerType = brokerType;
    }

    // Constructor
    public LocationEntity(String cityName, String neighborhoodName, double latitude, double longitude, BrokerType brokerType) {
        this.cityName = cityName;
        this.neighborhoodName = neighborhoodName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = UUID.randomUUID().toString();
        this.createdDate = new Timestamp(System.currentTimeMillis());
        this.brokerType = brokerType;

    }

    public LocationEntity() {

    }
}

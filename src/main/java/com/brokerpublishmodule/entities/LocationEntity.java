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

    @Column("calculatedMs")
    private long calculatedMs;

    @Column("testGroupId")
    private String testGroupId;

    public String getNearPointId() {
        return NearPointId;
    }

    public void setNearPointId(String nearPointId) {
        NearPointId = nearPointId;
    }

    @Column("NearPointId")
    private String NearPointId;

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

    public long getCalculatedMs() {
        return calculatedMs;
    }

    public void setCalculatedMs(long calculatedMs) {
        this.calculatedMs = calculatedMs;
    }

    public LocationEntity() {

    }

    public String findNearestPointId(LocationEntity targetLocation, Iterable<LocationEntity> otherLocations, double searchRadius) {
        double targetLatitude = targetLocation.getLatitude();
        double targetLongitude = targetLocation.getLongitude();
        double minDistance = Double.MAX_VALUE;
        String nearestPointId = null;

        // Hedef konuma en yakın olanı bulma
        for (LocationEntity location : otherLocations) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double distance = calculateDistance(targetLatitude, targetLongitude, latitude, longitude);

            if (distance < minDistance && distance <= searchRadius) {
                minDistance = distance;
                nearestPointId = location.getId();
            }
        }

        return nearestPointId;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        return dist;
    }

    public String getTestGroupId() {
        return testGroupId;
    }

    public void setTestGroupId(String testGroupId) {
        this.testGroupId = testGroupId;
    }
}

package com.brokerpublishmodule.services;

import com.brokerpublishmodule.entities.LocationEntity;
import com.brokerpublishmodule.enums.BrokerType;
import com.brokerpublishmodule.repository.LocationRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    private static final String[] CITIES = {"Istanbul", "Sivas", "Izmir", "Bursa"};
    private static final String[] NEIGHBORHOODS = {"Kadikoy", "Besiktas", "Levent", "Kizilay", "İstasyon", "OSB"};
    private static final double[] LATITUDES = {41.0082, 39.9334, 38.4192, 40.1829};
    private static final double[] LONGITUDES = {28.9784, 32.8597, 27.1287, 29.0671};

    private final Random random = new Random();

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public LocationEntity generateMockLocation(BrokerType brokerType) {
        int cityIndex = random.nextInt(CITIES.length);
        int neighborhoodIndex = random.nextInt(NEIGHBORHOODS.length);

        String cityName = CITIES[cityIndex];
        String neighborhoodName = NEIGHBORHOODS[neighborhoodIndex];
        double latitude = LATITUDES[cityIndex];
        double longitude = LONGITUDES[cityIndex];

        return new LocationEntity(cityName, neighborhoodName, latitude, longitude, brokerType);
    }

    public Iterable<LocationEntity> getAllLocationEntity() {
        return locationRepository.findAll();
    }

    public LocationEntity saveLocationEntity(LocationEntity locationEntity) {
        return locationRepository.save(locationEntity);
    }

    public List<LocationEntity> getByTimeBetween(Timestamp startDate, Timestamp endDate) {
        return locationRepository.findByCreatedDateBetween(startDate, endDate);
    }

    public byte[] locationsToExcel(List<LocationEntity> locations) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Locations");

        // Header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Id");
        headerRow.createCell(1).setCellValue("City Name");
        headerRow.createCell(2).setCellValue("Neighborhood Name");
        headerRow.createCell(3).setCellValue("Latitude");
        headerRow.createCell(4).setCellValue("Longitude");
        headerRow.createCell(5).setCellValue("Created Date");
        headerRow.createCell(6).setCellValue("Broker Type");
        headerRow.createCell(7).setCellValue("Near Point Id");
        headerRow.createCell(8).setCellValue("Time to find Near Point Id");
        headerRow.createCell(9).setCellValue("Test Group Id");

        // Data rows
        for (int i = 0; i < locations.size(); i++) {
            LocationEntity location = locations.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(location.getId());
            row.createCell(1).setCellValue(location.getCityName());
            row.createCell(2).setCellValue(location.getNeighborhoodName());
            row.createCell(3).setCellValue(location.getLatitude());
            row.createCell(4).setCellValue(location.getLongitude());
            row.createCell(5).setCellValue(location.getCreatedDate().toString());
            row.createCell(6).setCellValue(location.getBrokerType().toString());
            row.createCell(7).setCellValue(location.getNearPointId() == null ? "" : location.getNearPointId());
            row.createCell(8).setCellValue(location.getCalculatedMs());
            row.createCell(9).setCellValue(location.getTestGroupId());
        }

        // Write workbook to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        return bos.toByteArray();
    }
}

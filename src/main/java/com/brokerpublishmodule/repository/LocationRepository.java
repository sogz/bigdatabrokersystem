package com.brokerpublishmodule.repository;

import com.brokerpublishmodule.entities.LocationEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface LocationRepository extends CassandraRepository<LocationEntity, Integer> {
    @Query("SELECT * FROM LocationEntity WHERE timestamp >= ?0 AND timestamp <= ?1 ALLOW FILTERING")
    List<LocationEntity> findByCreatedDateBetween(Timestamp startDate, Timestamp endDate);
}

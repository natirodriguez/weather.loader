package com.example.weather.loader.infraestructure.persistence;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.example.weather.loader.domain.WeatherData;

@Repository
public interface WeatherDataRepository extends MongoRepository<WeatherData, String> {
    WeatherData findTopByCityOrderByTimestampDesc(String city);
    List<WeatherData> findByCityAndTimestampBetween(String city, Instant start, Instant end);
    List<WeatherData> findByCityAndTimestampAfter(String city, Instant startOfDay);
}

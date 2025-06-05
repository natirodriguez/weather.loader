package com.example.weather.loader.infraestructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.example.weather.loader.domain.WeatherData;

@Repository
public interface WeatherDataRepository extends MongoRepository<WeatherData, String> {

}

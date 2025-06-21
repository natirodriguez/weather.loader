package com.example.weather.loader.service;

import java.time.Instant;
import java.util.List;

import com.example.weather.loader.domain.WeatherData;
import com.example.weather.loader.infraestructure.rest.dto.WeatherDataDTO;

public interface IWeatherLoaderService {
	void createWeatherData(WeatherDataDTO request) throws Exception;

	WeatherData getLastTemperature(String city) throws Exception;
	
	List<WeatherData> getTemperaturesLastWeek(String city);
	
    List<WeatherData> getTemperaturesToday(String city);
}

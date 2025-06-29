package com.example.weather.loader.service;

import java.io.FileWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.weather.loader.domain.WeatherData;
import com.example.weather.loader.infraestructure.persistence.WeatherDataRepository;
import com.example.weather.loader.infraestructure.rest.dto.WeatherDataDTO;
import io.micrometer.common.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class WeatherLoaderService implements IWeatherLoaderService {
	
	@Autowired
	private WeatherDataRepository weatherDataRepository;
	
	private static final Logger LOG = LogManager.getLogger(WeatherLoaderService.class);

	@Override
	public void createWeatherData(WeatherDataDTO request) throws Exception {
		
		if(StringUtils.isBlank(request.getCity())) {
			throw new Exception("El campo mail no debe viajar vacio");
		}
		
        WeatherData weatherData = new WeatherData();
        weatherData.setCity(request.getCity());
        weatherData.setTemperature(request.getTemperature());
        weatherData.setWeather(request.getWeather());
        weatherData.setTimestamp(request.getTimestamp());

        // Guardar en MongoDB
        weatherDataRepository.save(weatherData);
	}
	
	@Override
	public WeatherData getLastTemperature(String city) throws Exception {
		
		try {
			
			return weatherDataRepository.findTopByCityOrderByTimestampDesc(city);
			
		}catch(Exception e) {
			LOG.error("Hubo un error al consumir el repositorio de MongoDB", e);
			throw new Exception("Error al consultar la bdd");
		}
    }
	
	@Override
	public List<WeatherData> getTemperaturesLastWeek(String city) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekAgo = now.minusDays(7);
        
        // Convertir a Instant para MongoDB
        Instant start = oneWeekAgo.atZone(ZoneId.systemDefault()).toInstant();
        Instant end = now.atZone(ZoneId.systemDefault()).toInstant();
        return weatherDataRepository.findByCityAndTimestampBetween(city, start, end);
    }
	
	@Override
	public List<WeatherData> getTemperaturesToday(String city) {
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime startOfDay = now.toLocalDate().atStartOfDay(ZoneId.systemDefault());
        
        // Convertir a Instant para MongoDB
        Instant start = startOfDay.toInstant();
        
        return weatherDataRepository.findByCityAndTimestampAfter(city, start);
    }
}

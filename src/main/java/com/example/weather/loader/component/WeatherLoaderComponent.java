package com.example.weather.loader.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.example.weather.loader.infraestructure.rest.dto.WeatherDataDTO;
import com.example.weather.loader.service.WeatherLoaderService;
import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class WeatherLoaderComponent {
    private static final Logger logger = LogManager.getLogger(WeatherLoaderComponent.class);

    private final String openWeatherApiKey = "a400bb918db70d65d96c4c27212bb92b"; 
    private final String city = "Quilmes,ar";

    @Autowired
    private WeatherLoaderService weatherLoaderService;

    @Scheduled(fixedRate = 3600000) // Cada 60 segundos
    public void fetchAndSendWeather() {
        try {
        	logger.info("Ejecutando proceso...");

        	RestTemplate restTemplate = new RestTemplate();
            String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric",
                city, openWeatherApiKey);
            
            String response = restTemplate.getForObject(url, String.class);

            com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);
            double temp = node.get("main").get("temp").asDouble();
            String weatherDescription = node.get("weather").get(0).get("description").asText();

            // Crear objeto WeatherData
            WeatherDataDTO weatherData = new WeatherDataDTO(city, temp, weatherDescription, LocalDateTime.now());

            // Guardar en MongoDB
            weatherLoaderService.createWeatherData(weatherData);
            
            logger.info("Temperatura actual guardada");
            System.out.println("Se guardo la temperatura actual para el día " + weatherData.getTimestamp());
        } catch (Exception e) {
        	logger.error("Error encontrado: ", e);
            e.printStackTrace();
        }        
    }
}

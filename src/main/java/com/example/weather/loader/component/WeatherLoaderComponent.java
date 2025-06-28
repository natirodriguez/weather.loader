package com.example.weather.loader.component;

import java.time.LocalDateTime;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.example.weather.loader.infraestructure.rest.dto.WeatherDataDTO;
import com.example.weather.loader.service.WeatherLoaderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Component
public class WeatherLoaderComponent {
	
    private static final Logger logger = LogManager.getLogger(WeatherLoaderComponent.class);
    
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private WeatherLoaderService weatherLoaderService;

	@Value("${weather.loader.city}")
	private String city;

	@Value("${weather.loader.ApiKey}")
	private String apiKey;
	
	@Value("${weather.loader.url}")
	private String weatherUrl;
	

    @Scheduled(fixedRate = 3600000) // 1 hora
    //@Scheduled(fixedRate = 10000) // 10 segundos
    @CircuitBreaker(name = "myDataCircuitBreaker", fallbackMethod = "fallbackRandomActivity")
    public void fetchAndSendWeather() throws Exception {
    	
    	
        	logger.info("Ejecutando proceso...");

            String url = String.format(weatherUrl, city, apiKey);
            
            String response = getUrl(url);
            
            com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);

            double temp = node.get("main").get("temp").asDouble();
            String weatherDescription = node.get("weather").get(0).get("description").asText();

            // Crear objeto WeatherData
            WeatherDataDTO weatherData = new WeatherDataDTO(city, temp, weatherDescription, LocalDateTime.now());

            // Guardar en MongoDB
            weatherLoaderService.createWeatherData(weatherData);
            
            logger.info("Temperatura actual guardada");
            System.out.println("Se guardo la temperatura actual para el día " + weatherData.getTimestamp());
    
    }
    
    /**
     * Metodo que devuelve la respuesta de la url en forma de json
     * @param url
     * @return String
     */
    public String getUrl(String url) {
    	System.out.println("getURL "+new Date().toString());
    	return restTemplate.getForObject(url, String.class);
    	
    }
    
    /**
     * Metodo para fallback
     * En caso de que el metodo fetchAndSendWeather de algun error, el circuitbreaker llamara a este metodo
     * Y el Scheduled seguira funcionando
     */
    public void fallbackRandomActivity(Throwable t) {
    	
    	logger.error("Fallback activado, error: ", t);
    	System.err.println("getFallback "+new Date().toString());
    }

}

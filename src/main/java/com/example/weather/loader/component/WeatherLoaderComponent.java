package com.example.weather.loader.component;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.weather.loader.domain.WeatherData;
import com.example.weather.loader.infraestructure.persistence.WeatherDataRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Properties;

@Component
public class WeatherLoaderComponent {

    private final String topic = "weather-data";
    private final String openWeatherApiKey = "a400bb918db70d65d96c4c27212bb92b"; 
    private final String city = "Quilmes,ar";

    private KafkaProducer<String, String> producer;

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
    }

    @Scheduled(fixedRate = 3600000) // Cada 60 segundos
    public void fetchAndSendWeather() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric",
                city, openWeatherApiKey);

            String response = restTemplate.getForObject(url, String.class);

            com.fasterxml.jackson.databind.JsonNode node = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);
            double temp = node.get("main").get("temp").asDouble();
            String weatherDescription = node.get("weather").get(0).get("description").asText();

            // Crear objeto WeatherData
            WeatherData weatherData = new WeatherData();
            weatherData.setCity(city);
            weatherData.setTemperature(temp);
            weatherData.setWeather(weatherDescription);
            weatherData.setTimestamp(LocalDateTime.now());

            // Guardar en MongoDB
            weatherDataRepository.save(weatherData);

            // Publicar en Kafka
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, response);
            producer.send(record, (RecordMetadata metadata, Exception e) -> {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    System.out.println("Mensaje enviado a Kafka, offset: " + metadata.offset());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.example.weather.loader.infraestructure.rest.dto;

import java.time.LocalDateTime;

public class WeatherDataDTO {
	private String id;
    private String city;
    private String weather;
    private double temperature;
    private LocalDateTime timestamp;

    public WeatherDataDTO() {
    	
    }
    
    public WeatherDataDTO(String city, double temperature, String weather, LocalDateTime timestamp) {
		super();
		this.city = city;
		this.weather = weather;
		this.temperature = temperature;
		this.timestamp = timestamp;
	}
	
    // Getters y Setters

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

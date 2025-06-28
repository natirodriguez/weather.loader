package com.example.weather.loader.infraestructure.rest;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.weather.loader.domain.WeatherData;
import com.example.weather.loader.infraestructure.rest.dto.WeatherDataDTO;
import com.example.weather.loader.service.WeatherLoaderService;

@RestController
public class WeatherLoaderController {
	
	@Autowired
	private WeatherLoaderService weatherLoaderService;
	private ModelMapper modelMapper = new ModelMapper();
	
    private final String city = "Quilmes,ar";

    @GetMapping("/current")
	public WeatherDataDTO obtenerTemperaturaActual() throws Exception {
		
    	try {
    		
    		WeatherData response = weatherLoaderService.getLastTemperature(city);
    		WeatherDataDTO dto = modelMapper.map(response, WeatherDataDTO.class);
    		return dto;
			
		} catch (Exception e) {
			throw new Exception("Hubo un error, por favor vuelva a probar mas adelante.", e);		
		}
    }
    
    @GetMapping("/today")
    public List<WeatherDataDTO> obtenerTemperaturasHoy() throws Exception {
        try {
            List<WeatherData> response = weatherLoaderService.getTemperaturesToday(city);
            return response.stream()
                .map(data -> modelMapper.map(data, WeatherDataDTO.class))
                .toList(); 
        } catch (Exception e) {
            throw new Exception("Hubo un error, por favor vuelva a probar más adelante.", e);
        }
    }
    
    @GetMapping("/last-week") 
    public List<WeatherDataDTO> obtenerTemperaturasUltimaSemana() throws Exception {
        try {
            List<WeatherData> response = weatherLoaderService.getTemperaturesLastWeek(city);
            return response.stream()
                .map(data -> modelMapper.map(data, WeatherDataDTO.class))
                .toList();
        } catch (Exception e) {
            throw new Exception("Hubo un error, por favor vuelva a probar más adelante.", e);
        }
    }
}

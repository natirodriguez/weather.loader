package com.example.weather.loader.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.SocketSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString("mongodb+srv://nati:vJ0aq52WYzsScyBX@cluster0.rja1e.mongodb.net/arqSoft2?retryWrites=true&w=majority");

        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .applyToClusterSettings(builder ->
                builder.serverSelectionTimeout(3, TimeUnit.SECONDS))
            .applyToSocketSettings(builder -> builder
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS))
            .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "weatherMap");
    }
    
    /*
     MongoClient es la clase que maneja la conexión física con la base de datos MongoDB. Se encarga de:

Abrir sockets.

Conectar al clúster.

Administrar conexiones, timeouts y autenticación.

MongoRepository es una interfaz de Spring Data que define métodos para acceder a la base, como findBy..., save, delete, etc.

Spring Data MongoDB, al iniciar tu aplicación, detecta:

Tu clase @Configuration que define un @Bean MongoClient.

Tu clase MongoTemplate, que se crea usando ese MongoClient.

Tus interfaces MongoRepository.

Luego, Spring crea automáticamente una implementación real de WeatherDataRepository que internamente usa el MongoTemplate → que usa el MongoClient.

Cuando llamás por ejemplo a:

java
Copiar
Editar
weatherDataRepository.findTopByCityOrderByTimestampDesc("Quilmes");
Esto ocurre internamente:

Spring Data genera una implementación del método usando una query equivalente a:

json
Copiar
Editar
db.weatherData.find({ city: "Quilmes" }).sort({ timestamp: -1 }).limit(1)
Esta consulta se ejecuta mediante el MongoTemplate.

El MongoTemplate usa el MongoClient que vos configuraste para:

Abrir la conexión.

Enviar la query.

Esperar la respuesta (respetando connectTimeout, readTimeout, etc.).
     */
    
}
package com.example.weather.controller;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.weather.dto.Weather;
import com.example.weather.services.WeatherService;


@RestController
public class Controller {

    public static final String weather_API = "/weatherInfo";

    @Autowired
    private WeatherService weatherService;

    @GetMapping(weather_API)
    public ResponseEntity<Weather> sendWeatherInfo(@RequestParam Integer pincode) {
        
        LocalDate date = LocalDate.now();

        Weather weather = weatherService.sendWeatherInfo(pincode, date);

        if (weather == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(weather);
        }

        return ResponseEntity.ok().body(weather);
    }


}

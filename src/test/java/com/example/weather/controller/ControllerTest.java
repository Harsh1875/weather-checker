package com.example.weather.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.example.weather.WeatherApplication;
import com.example.weather.dto.Weather;
import com.example.weather.services.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = {WeatherApplication.class})
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class ControllerTest {
    
    private static final String weather_API = "/weatherInfo";

    @MockBean
    private WeatherService weatherService;

    private ObjectMapper objectMapper;
    private MockMvc mvc;

    @InjectMocks
    private Controller controller;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void validHttpRequest() throws Exception {
        URI uri = UriComponentsBuilder
            .fromPath(weather_API)
            .queryParam("pincode", "411014")
            .build().toUri();

        assertEquals(weather_API + "?pincode=411014", uri.toString());

        Weather weather = new Weather();

        when(weatherService.sendWeatherInfo(411014, LocalDate.now())).thenReturn(weather);

        MockHttpServletResponse response = mvc.perform(
            get(uri.toString()).accept(APPLICATION_JSON)
            ).andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
  }

    @Test
    public void invalidHttpRequest() throws Exception {
        URI uri = UriComponentsBuilder
            .fromPath(weather_API)
            .queryParam("pincode", "400")
            .build().toUri();

        assertEquals(weather_API + "?pincode=400", uri.toString());

        Weather weather = null;

        when(weatherService.sendWeatherInfo(400, LocalDate.now())).thenReturn(weather);

        MockHttpServletResponse response = mvc.perform(
            get(uri.toString()).accept(APPLICATION_JSON)
            ).andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    public void missingParameter() throws Exception {
        // calling api without longitude
        URI uri = UriComponentsBuilder
            .fromPath(weather_API)
            .build().toUri();

        assertEquals(weather_API, uri.toString());

        MockHttpServletResponse response = mvc.perform(
            get(uri.toString()).accept(APPLICATION_JSON)
        ).andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }


}

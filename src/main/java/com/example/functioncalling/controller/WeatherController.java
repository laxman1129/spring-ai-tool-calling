package com.example.functioncalling.controller;

import com.example.functioncalling.model.Answer;
import com.example.functioncalling.model.Question;
import com.example.functioncalling.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping
    public Answer getWeather(String question) {
        return weatherService.getWeather(new Question(question));
    }
}

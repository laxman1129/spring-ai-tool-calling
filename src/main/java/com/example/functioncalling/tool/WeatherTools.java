package com.example.functioncalling.tool;

import com.example.functioncalling.model.WeatherRequest;
import com.example.functioncalling.model.WeatherResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.web.client.RestClient;

import java.net.URI;

@RequiredArgsConstructor
@Slf4j
public class WeatherTools {
    public static final String WEATHER_URL = "https://api.api-ninjas.com/v1/weather";
    private final String apiNinjasKey;


    @Tool(name = "currentWeather", description = "Get current weather information for a location")
    public WeatherResponse getWeather(WeatherRequest weatherRequest) {
        log.info("Get current weather information for {}", weatherRequest);
        RestClient restClient = RestClient.builder()
                .baseUrl(WEATHER_URL)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.set("X-Api-Key", apiNinjasKey);
                    httpHeaders.set("Accept", "application/json");
                    httpHeaders.set("Content-Type", "application/json");
                }).build();

        var response = restClient.get().uri(uriBuilder -> {
            log.info("Building URI for weather request: {}", weatherRequest);

            uriBuilder.queryParam("city", weatherRequest.location());
            if (weatherRequest.state() != null && !weatherRequest.state().isBlank()) {
                uriBuilder.queryParam("state", weatherRequest.state());
            }
            if (weatherRequest.country() != null && !weatherRequest.country().isBlank()) {
                uriBuilder.queryParam("country", weatherRequest.country());
            }
            URI uri = uriBuilder.build();
            log.info("URI for weather request: {}", uri);
            return uri;
        }).retrieve().body(WeatherResponse.class);
        log.info("Weather request returned: {}", response);
        return response;
    }
}

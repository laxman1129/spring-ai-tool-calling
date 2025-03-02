package com.example.functioncalling.service;

import com.example.functioncalling.model.Answer;
import com.example.functioncalling.model.Question;
import com.example.functioncalling.tool.WeatherTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherService {
    private final ChatModel chatModel;
    @Value("${application.aiapp.apiNinjasKey}")
    private String apiNinjasKey;


    public Answer getWeather(Question question) {
        Message systemMessage = new PromptTemplate("""
                You are geography expert and can identify city/state/countries from sentences. 
                Use your knowledge to call weather tools to get the current weather information 
                for a city/state/country provided by the user by mapping it to the location.
                EXAMPLE :
                what is the weather in London
                location: London
                what is the weather in Goa
                location: Goa
                """).createMessage();
        Message userMessage = new PromptTemplate(question.question()).createMessage();

        String response = ChatClient.create(chatModel)
                .prompt()
                .messages(List.of(userMessage))
                .tools(new WeatherTools(apiNinjasKey))
                .call()
                .content();
        return new Answer(response);
    }

}

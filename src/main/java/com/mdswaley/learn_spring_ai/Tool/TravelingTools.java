package com.mdswaley.learn_spring_ai.Tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class TravelingTools {

    @Tool(description = "Get the weather of a city")
    public String getWeather(@ToolParam(description = "city name for which to get the weather information") String city){
        return switch (city){
            case "Delhi" -> "Sunny, 26 deg";
            case "Mumbai" -> "Cloudy, 14 deg";
            default -> "Cannot identify the city";
        };
    }
}

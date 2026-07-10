package com.mdswaley.learn_spring_ai.Controller;

import com.mdswaley.learn_spring_ai.Tool.FlightBookingTools;
import com.mdswaley.learn_spring_ai.Tool.TravelingTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class chatController {
    private final ChatClient chatClient;
    private final TravelingTools travelingTools;
    private final FlightBookingTools flightBookingTools;
    private final ChatMemory chatMemory;

    @PostMapping("/chat")
    public String chat(
            @RequestParam String userId,
            @RequestBody String message) {

        return chatClient.prompt()
                .system("""
                        You are a helpful travel assistant.
                        
                        Rules:
                        1. Use flight booking tools whenever the user wants to book, cancel, or check a flight.
                        2. Use weather tools whenever weather information is requested.
                        3. Always answer politely.
                        
                        Current User ID: %s
                        """.formatted(userId))
                .user(message)
                .tools(travelingTools, flightBookingTools)
                .advisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .advisors(a -> a.param(
                ChatMemory.CONVERSATION_ID,
                userId
                ))
                .call()
                .content();
    }
}

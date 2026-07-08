package com.mdswaley.learn_spring_ai.Controller;

import com.mdswaley.learn_spring_ai.Tool.TravelingTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class chatController {
    private final ChatClient chatClient;
    private final TravelingTools travelingTools;

    @PostMapping("/chat")
    public String chat(@RequestBody String message){
        return chatClient.prompt()
                .user(message)
                .tools(travelingTools)
                .call()
                .content();
    }
}

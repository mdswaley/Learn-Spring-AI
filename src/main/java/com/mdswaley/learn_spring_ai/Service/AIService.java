package com.mdswaley.learn_spring_ai.Service;

import com.mdswaley.learn_spring_ai.DTO.Joke;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIService {
    private final ChatClient chatClient;

    public String getJoke(String topic){
        String systemPrompt = """
                you are a sarcastic joker, you make poetic jokes in 4 lines.
                you don't make joke about politics.
                give a joke on topic: {topic}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(systemPrompt);
        String render = promptTemplate.render(Map.of("topic", topic));

        return chatClient
                .prompt()
//                .system("you are a sarcastic joker, give me in one line.")
//                .user("give me a joke on the given topic: "+topic)
                .user(render)
                .call()
                .content();
    }

    public String getJoke1(String topic){
        String systemPrompt = """
                you are a sarcastic joker, you make poetic jokes in 4 lines.
                you don't make joke about politics.
                give a joke on topic: {topic}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(systemPrompt);
        String render = promptTemplate.render(Map.of("topic", topic));

        var response =  chatClient
                .prompt()
                .advisors(new SimpleLoggerAdvisor())
                .user(render)
                .call()
                .chatClientResponse();

        return response.chatResponse().getResult().getOutput().getText();
    }

    public String getJoke2(String topic){
        String systemPrompt = """
                you are a sarcastic joker, you make poetic jokes in 4 lines.
                you don't make joke about politics.
                give a joke on topic: {topic}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(systemPrompt);
        String render = promptTemplate.render(Map.of("topic", topic));

        var response =  chatClient
                .prompt()
                .advisors(new SimpleLoggerAdvisor())
                .user(render)
                .call()
                .entity(Joke.class);  // This will help to convert text response from
        // chatClient to java Object which is Joke.class

        return response.text();
    }
}

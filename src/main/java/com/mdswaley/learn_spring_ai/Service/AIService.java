package com.mdswaley.learn_spring_ai.Service;

import com.mdswaley.learn_spring_ai.DTO.Joke;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
public class AIService {
    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    public float[] getEmbedding(String text){
        return embeddingModel.embed(text);
    }

    public void ingestDataToVectorStore(String text){
        Document document = new Document(text);

        vectorStore.add(List.of(document)); // it will use embedding model internally after converting array of float it will store here.
    }

    public void ingestDataToVectorStoreListOfData(){
        List<Document> marvelMovies = List.of(
                new Document(
                        """
                        Iron Man is a 2008 Marvel superhero movie featuring Tony Stark,
                        a genius billionaire who builds a powered suit of armor and becomes Iron Man.
                        """,
                        Map.of(
                                "title", "Iron Man",
                                "year", 2008,
                                "hero", "Tony Stark",
                                "genre", "Action"
                        )
                ),

                new Document(
                        """
                        Captain America: The First Avenger follows Steve Rogers,
                        a weak young man transformed into a super soldier during World War II.
                        """,
                        Map.of(
                                "title", "Captain America: The First Avenger",
                                "year", 2011,
                                "hero", "Steve Rogers",
                                "genre", "Action"
                        )
                ),

                new Document(
                        """
                        The Avengers brings together Iron Man, Captain America, Thor,
                        Hulk, Black Widow, and Hawkeye to stop Loki's invasion of Earth.
                        """,
                        Map.of(
                                "title", "The Avengers",
                                "year", 2012,
                                "team", "Avengers",
                                "genre", "Superhero"
                        )
                ),

                new Document(
                        """
                        Avengers: Infinity War follows the Avengers as they attempt
                        to stop Thanos from collecting all six Infinity Stones.
                        """,
                        Map.of(
                                "title", "Avengers: Infinity War",
                                "year", 2018,
                                "villain", "Thanos",
                                "genre", "Superhero"
                        )
                )
        );

        vectorStore.add(marvelMovies);
    }



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

    @PostConstruct
    public void checkTimezone() {
        System.out.println("Java Timezone = " + TimeZone.getDefault().getID());
    }
}

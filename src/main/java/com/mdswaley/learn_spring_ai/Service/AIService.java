package com.mdswaley.learn_spring_ai.Service;

import com.mdswaley.learn_spring_ai.DTO.Joke;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

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

//   // The model receives only the current prompt.
    // It does not know previous prompts or responses unless
    // conversation history is explicitly passed or ChatMemory is used.

//    public String askAI(String prompt){
//        return chatClient.prompt()
//                .user(prompt)
//                .call()
//                .content();
//    }


//    before that is knows what prompt is and tell accordingly
//    like :- what is Apple but know don't know because it read document from out provided data.
    public String askAI(String prompt){
        String temp = """
                You are an AI assistant helping a developer.
                
                     Rules:
                         - Use only the information provided in the context.
                         - Do not make up facts or invent information.
                         - If the answer is not present in the context, say:
                           "I don't have enough information in the provided context to answer that."
                         - You may rephrase and summarize the context to improve clarity.
                
                          context:
                         {context}
                """;

        List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder()
                        .query(prompt)
                        .topK(2)
//                        .filterExpression("topic == 'spring-ai' or topic == 'vectorstore'")
                .build());

        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n")); // display in 2 lines

        PromptTemplate promptTemplate = new PromptTemplate(temp);

        String systemPrompt = promptTemplate.render(Map.of("context", context));

                return chatClient.prompt()
                        .system(systemPrompt)
                .user(prompt)
                        .advisors(new SimpleLoggerAdvisor())
                .call()
                .content();
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
        vectorStore.add(springAiDocs());
    }

//    public List<Document> getSimilaritySearch(String str){
//        return vectorStore.similaritySearch(str);
//    }


    public List<Document> springAiDocs() {

        return List.of(
                new Document(
                        """
                        Spring AI is a framework that simplifies building AI-powered applications
                        using Spring Boot.
                        """,
                        Map.of("topic", "spring-ai")
                ),

                new Document(
                        """
                        RAG (Retrieval Augmented Generation) combines LLMs with external knowledge
                        sources to provide accurate and context-aware responses.
                        """,
                        Map.of("topic", "rag")
                ),

                new Document(
                        """
                        Vector stores are used to store embeddings and perform similarity searches.
                        Examples include PGVector, Pinecone, and Milvus.
                        """,
                        Map.of("topic", "vector-store")
                ),

                new Document(
                        """
                        Embedding models convert text into high-dimensional vectors that capture
                        semantic meaning.
                        """,
                        Map.of("topic", "embedding")
                ),

                new Document(
                        """
                        ChatModel is the core abstraction in Spring AI for interacting with LLMs.
                        """,
                        Map.of("topic", "chat-model")
                )
        );
    }

    public List<Document> getSimilaritySearch(String str){
        return vectorStore.similaritySearch(SearchRequest.builder()
                        .query(str)
                        .topK(1)
                        .filterExpression("title == 'Iron Man'")
                        .build());
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

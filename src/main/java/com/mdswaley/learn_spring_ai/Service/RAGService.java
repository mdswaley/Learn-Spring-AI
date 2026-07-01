package com.mdswaley.learn_spring_ai.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RAGService {
    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    @Value("classpath:faq.pdf")
    Resource pdfFile;


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
                .topK(4)
                .similarityThreshold(0.4)
                .filterExpression("file_name == 'faq.pdf'")
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

    public void ingestPDFToVectorStore(){
        PagePdfDocumentReader reader = new PagePdfDocumentReader(pdfFile);

        List<Document> page = reader.get();

        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder()
                .withChunkSize(200)
                .build();

        List<Document> chunks = tokenTextSplitter.apply(page);
        vectorStore.add(chunks);
    }



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
}

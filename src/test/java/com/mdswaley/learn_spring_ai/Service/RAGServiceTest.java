package com.mdswaley.learn_spring_ai.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RAGServiceTest {

    @Autowired
    private RAGService ragService;


    @Test
    public void addPDFToVSTest(){
        ragService.ingestPDFToVectorStore();
    }

    @Test
    public void getAskAI(){
        var res = ragService.askAI("how to connect to my discord account?");
        System.out.println(res);
    }

    @Test
    public void getAskAIWithAdvisors(){
        String res = ragService.askAIWithAdvisors("my name is Swaley and DOB is 9th June 2003. can you tell me the way to cuttack odisha", "swaley123");  // If you use a different userId, the conversation
        // memory associated with the previous user will not be available, so the chat model will treat you as a new user.
        System.out.println(res);
    }

}
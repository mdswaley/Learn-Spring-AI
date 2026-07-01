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

}
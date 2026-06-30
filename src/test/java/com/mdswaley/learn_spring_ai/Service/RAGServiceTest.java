package com.mdswaley.learn_spring_ai.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RAGServiceTest {

    @Autowired
    private RAGService ragService;


    @Test
    public void addPDFToVSTest(){
        ragService.ingestPDFToVectorStore();
    }

}
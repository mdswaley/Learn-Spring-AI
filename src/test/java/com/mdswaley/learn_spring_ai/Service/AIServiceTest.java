package com.mdswaley.learn_spring_ai.Service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AIServiceTest {

    @Autowired
    private AIService aiService;

    @Test
    public void getJokeTest(){
        var j = aiService.getJoke("Dogs");
        System.out.println(j);
    }

    @Test
    public void getJokeTest1(){
        var j = aiService.getJoke1("Programmer");
        System.out.println(j);
    }

}
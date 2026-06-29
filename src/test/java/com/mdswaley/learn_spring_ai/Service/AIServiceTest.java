package com.mdswaley.learn_spring_ai.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


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

    @Test
    public void getJokeTest2(){
        var j = aiService.getJoke2("Cats");
        System.out.println(j);
    }

    @Test
    public void getEmbedText(){
        var j = aiService.getEmbedding("This is the big text here.");
        System.out.println(j.length);

        for(float e:j){
            System.out.println(e+" ");
        }
    }

    @Test
    public void testStoreData(){
        aiService.ingestDataToVectorStore("This is the big text."); // this is use for to store the float
        // of array which is converted from the given text in the database.
    }

    @Test
    public void testStoreListData(){
        aiService.ingestDataToVectorStoreListOfData();
    }

    @Test
    public void getSimilaritySearch(){
//        var res = aiService.getSimilaritySearch("infinity stone");
        String s = "superhero Movies";
        var res = aiService.getSimilaritySearch(s);
        System.out.println(res);
    }

    @Test
    public void getAskAI(){
        var res = aiService.askAI("what is vector store.");
        System.out.println(res);
    }

}
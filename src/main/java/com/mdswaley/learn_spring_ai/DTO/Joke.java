package com.mdswaley.learn_spring_ai.DTO;


public record Joke(
    String text,
    String category,
    Double laughRating,
    Boolean isNSFW
){}

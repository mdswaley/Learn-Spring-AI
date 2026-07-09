package com.mdswaley.learn_spring_ai.DTO;

import com.mdswaley.learn_spring_ai.Entity.BookingStatus;

import java.time.Instant;

public record BookingResponse(Long id, String destination, Instant departureTime, BookingStatus status) {
}

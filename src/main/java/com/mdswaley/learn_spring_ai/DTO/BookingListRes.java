package com.mdswaley.learn_spring_ai.DTO;

import java.util.List;

public record BookingListRes(List<BookingResponse> bookingResponseList, String message) {
}

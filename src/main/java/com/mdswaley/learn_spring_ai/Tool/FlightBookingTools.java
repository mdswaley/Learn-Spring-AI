package com.mdswaley.learn_spring_ai.Tool;

import com.mdswaley.learn_spring_ai.DTO.BookingListRes;
import com.mdswaley.learn_spring_ai.DTO.BookingResponse;
import com.mdswaley.learn_spring_ai.Entity.BookingStatus;
import com.mdswaley.learn_spring_ai.Entity.FlightBooking;
import com.mdswaley.learn_spring_ai.Service.FlightBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FlightBookingTools {
    private final FlightBookingService flightBookingService;

     @Tool(
             name = "flight_booking_tool",
             description = "create a new flight booking for user"
     )
    public BookingResponse createBooking(
            @ToolParam(description = "The unique user id (e.g. userId is user123")
            String userId,

            @ToolParam(description = "The destination for the flight booking (e.g. city like Delhi, London, etc")
            String destination,

            @ToolParam(description = "Departure date and time in ISO-8601 format (e.g., 2026-07-03T14:30:00Z)")
            Instant departureTime
     ){
         var flightBooking = flightBookingService.createBooking(userId, destination, departureTime);

         return new BookingResponse(
                 flightBooking.getId(),
                 flightBooking.getDestination(),
                 flightBooking.getDepartureTime(),
                 flightBooking.getBookingStatus());
    }

    public BookingListRes getUserBookings(

            @ToolParam(description = "Unique user id")
            String userId
    ) {

         List<FlightBooking> bookings = flightBookingService.getUserBooking(userId);

         List<BookingResponse> res = bookings.stream()
                 .map(b -> new BookingResponse(
                         b.getId(),
                         b.getDestination(),
                         b.getDepartureTime(),
                         b.getBookingStatus()
                 )).toList();

         String msg = bookings.isEmpty() ? "You have no upcoming flight Booking"
                 : "Here are your current flight booking: ";

         return new BookingListRes(res, msg);
    }


}

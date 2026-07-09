package com.mdswaley.learn_spring_ai.Service;

import com.mdswaley.learn_spring_ai.Entity.BookingStatus;
import com.mdswaley.learn_spring_ai.Entity.FlightBooking;
import com.mdswaley.learn_spring_ai.Repository.FlightBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightBookingService {
    private final FlightBookingRepository flightBookingRepository;

    public FlightBooking createBooking(String userId, String destination, Instant departureTime){
        boolean exist = flightBookingRepository.existsByUserIdAndDestinationAndDepartureTime(userId, destination, departureTime);

        if(exist){
            throw new IllegalArgumentException(
                    "You already have a booking to "+ destination +" on that date."
            );
        }


        FlightBooking booking = FlightBooking.builder()
                .userId(userId)
                .destination(destination)
                .departureTime(departureTime)
                .bookedAt(Instant.now())
                .bookingStatus(BookingStatus.CONFIRMED)
                .build();

        return flightBookingRepository.save(booking);
    }
}

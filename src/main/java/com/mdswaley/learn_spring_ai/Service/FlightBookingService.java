package com.mdswaley.learn_spring_ai.Service;

import com.mdswaley.learn_spring_ai.Entity.BookingStatus;
import com.mdswaley.learn_spring_ai.Entity.FlightBooking;
import com.mdswaley.learn_spring_ai.Repository.FlightBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightBookingService {
    private final FlightBookingRepository flightBookingRepository;

    public FlightBooking createBooking(String userId, String destination, Instant departureTime){

//        In production level you need to pass user from security context holder. Not from parameter

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

    public List<FlightBooking> getUserBooking(String userId){
        FlightBooking exist = flightBookingRepository.findById(Long.parseLong(userId)).orElse(null);
        if(exist == null){
            throw new RuntimeException("User not found with id : "+userId);
        }
        return flightBookingRepository.findByUserIdOrderByDepartureTimeDesc(userId);
    }

    public FlightBooking updateBookingStatus(Long bookingId, String userId, BookingStatus newStatus){
        FlightBooking booking = flightBookingRepository.findById(bookingId).orElseThrow(
                () -> new IllegalArgumentException("Booking not found!")
        );

        if(!booking.getUserId().equals(userId)){
            throw new IllegalArgumentException("You can only modify your own bookings");
        }

        booking.setBookingStatus(newStatus);
        return flightBookingRepository.save(booking);
    }
}

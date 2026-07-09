package com.mdswaley.learn_spring_ai.Repository;

import com.mdswaley.learn_spring_ai.Entity.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {
    List<FlightBooking> findByUserIdOrderByDepartureTimeDesc(String userId);

    boolean existsByUserIdAndDestinationAndDepartureTime(String userId, String destination, Instant departureTime);
}

package com.mdswaley.learn_spring_ai.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FlightBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    String destination;

    Instant departureTime;

    @Enumerated(EnumType.STRING)
    BookingStatus bookingStatus;

    @CreationTimestamp
    Instant bookedAt;
}

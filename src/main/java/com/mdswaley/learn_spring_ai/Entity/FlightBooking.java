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

    Instant departureTime; // Instant: represents a specific point in time on the UTC timeline

    @Enumerated(EnumType.STRING)
    BookingStatus bookingStatus;

    @CreationTimestamp
    Instant bookedAt;
}


// | Instant                           | LocalDateTime                             |
// | --------------------------------- | ----------------------------------------- |
// | Represents an exact moment in UTC | Represents date and time without timezone |
// | Best for database timestamps      | Best for user-facing date/time            |
// | Timezone independent              | Timezone dependent                        |
// | Example: `2026-07-09T05:42:15Z`   | Example: `2026-07-09T11:12:15`            |

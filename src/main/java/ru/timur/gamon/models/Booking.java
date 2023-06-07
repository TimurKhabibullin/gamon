package ru.timur.gamon.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "person_id")
    private int personId;
    @Column(name = "booking_date")
    private long bookingDateForDb;
    @Column(name = "booking_time")
    private String bookingTime;
    @Column(name = "computer_id")
    private int computerId;
    @Column(name = "booking_date_time")
    private String bookingDateTime;
    @Transient
    private String bookingDate;
    @Transient
    private long startTime;
    @Transient
    private long endTime;
    @Transient
    private boolean error;
}

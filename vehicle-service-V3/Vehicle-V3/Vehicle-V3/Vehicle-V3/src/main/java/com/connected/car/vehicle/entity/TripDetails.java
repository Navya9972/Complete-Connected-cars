package com.connected.car.vehicle.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="car_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="trip_details_id")
    private long tripDetailsId;

    @Column(name="trip_name")
    private String tripName;

    @Column(name="car_id")
    private int carId;
    @Column(name="max_speed")
    private double maxSpeed;
    @Column(name="min_speed")
    private double minSpeed;

    @Column(name = "travel_start_datetime")
    private LocalDateTime travelStartDateTime;
    @Column(name="travel_end_datetime")
   private LocalDateTime travelEndDateTime;
    @Column(name="initial_fuel_reading")
    private double initialFuelReading;
    @Column(name = "current_fuel_reading")
    private double currentFuelReading;
    @Column(name="odometer_start_reading")
    private double odometerStartReading;
    @Column(name="odometer_end_reading")
    private double odometerEndReading;
    @CreatedDate
    @Column(name = "created_datetime")
    private LocalDateTime createdDate;
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
    @LastModifiedDate
    @Column(name = "modified_datetime")
    private LocalDateTime updatedDate;
    @LastModifiedBy
    @Column(name = "modified_by")
    private String updatedBy;
    
	private boolean active;



}

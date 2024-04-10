package com.connected.car.vehicle.dto;

import jakarta.persistence.Column;
import jakarta.validation.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDetailsDto {
    private long tripDetailsId;
    private int carId;
    private String tripName;
    @NotNull
    private double maxSpeed;
    @NotNull
    private double minSpeed;

    @NotNull
    private LocalDateTime travelStartDateTime;
    
    @NotNull
    private LocalDateTime travelEndDateTime;
    
    @NotNull
    private double initialFuelReading;
    
    @NotNull
    private double currentFuelReading;
    
    @NotNull
    private double odometerStartReading;
    
    @NotNull
    private double odometerEndReading;

    private boolean active;
    
    @AssertTrue(message = "Start date-time must be present or in the past, and end date-time should be after start date-time")
    private boolean isDateTimeValid() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return travelStartDateTime == null || travelEndDateTime == null ||
               !travelStartDateTime.isAfter(currentDateTime) &&
               !travelEndDateTime.isAfter(currentDateTime) &&
               travelStartDateTime.isBefore(travelEndDateTime);
    }
}




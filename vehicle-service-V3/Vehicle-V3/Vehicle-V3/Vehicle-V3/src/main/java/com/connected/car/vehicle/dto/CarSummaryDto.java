package com.connected.car.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarSummaryDto {

    private long carSummaryId;
    private long tripDetailsId;
    private double mileage;
    private double avgMileage;
    private double fuelConsumption;
    private double totalDistance;
    private String idleTime;
    private int duration;
    private boolean active;
}

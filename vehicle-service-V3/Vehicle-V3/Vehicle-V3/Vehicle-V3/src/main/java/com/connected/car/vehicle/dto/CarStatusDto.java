package com.connected.car.vehicle.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarStatusDto {
    private long carStatusId;
    private int fuelStatus;
    private int car_id;
    private boolean lockStatus;
    private com.connected.car.vehicle.entity.batteryStatus batteryStatus;
    private boolean active;

}

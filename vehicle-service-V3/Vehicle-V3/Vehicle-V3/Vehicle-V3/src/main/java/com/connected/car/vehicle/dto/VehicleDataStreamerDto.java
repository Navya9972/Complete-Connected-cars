package com.connected.car.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDataStreamerDto {

    private int streamId;
    private int currentSpeed;
    private boolean isEngineActive;
    private Long tripDetailsId;
    private boolean active;
}


package com.connected.car.vehicle.service;


public interface VehicleDataStreamerService {

    void setEngineStatusActive(Long tripDetailId);

    void setEngineStatusInActive(Long tripDetailId);

    int updateCurrentSpeed(Integer currentSpeed, long tripDetailId);
}

package com.connected.car.vehicle.service.impl;

import com.connected.car.vehicle.dto.VehicleDataStreamerDto;
import com.connected.car.vehicle.entity.VehicleDataStreamer;
import com.connected.car.vehicle.exceptions.EngineAlreadyActiveException;
import com.connected.car.vehicle.exceptions.EngineAlreadyInActiveException;
import com.connected.car.vehicle.exceptions.TripDetailsNotFoundException;
import com.connected.car.vehicle.repository.VehicleDataStreamerRepository;
import com.connected.car.vehicle.service.VehicleDataStreamerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VehicleDataStreamerServiceimpl implements VehicleDataStreamerService {

    LocalDateTime startTime = null, endTime = null;
    @Autowired
    private CarSummaryImpl carSummaryServiceImpl;

    @Autowired
    private VehicleDataStreamerRepository vehicleDataStreamerRepository;

    @Override
    public void setEngineStatusInActive(Long tripDetailId) {

        VehicleDataStreamer vehicleDataStreamer = vehicleDataStreamerRepository.findByTripDetailsId(tripDetailId)
                .orElseThrow(() -> new TripDetailsNotFoundException("No data associated with trip id:" + tripDetailId));
        try {
            if (!(vehicleDataStreamer.isEngineActive())) {
                throw new EngineAlreadyInActiveException(tripDetailId);
            }
            vehicleDataStreamer.setEngineActive(false);
            startTime = LocalDateTime.now();
            carSummaryServiceImpl.startTime = startTime;
            vehicleDataStreamerRepository.save(vehicleDataStreamer);
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public void setEngineStatusActive(Long tripDetailId) {

        VehicleDataStreamer vehicleDataStreamer = vehicleDataStreamerRepository.findByTripDetailsId(tripDetailId)
                .orElseThrow(() -> new TripDetailsNotFoundException("No data associated with trip id:" + tripDetailId));
        try {
            if (vehicleDataStreamer.isEngineActive()) {
                throw new EngineAlreadyActiveException(tripDetailId);
            }
            vehicleDataStreamer.setEngineActive(true);
            endTime = LocalDateTime.now();
            VehicleDataStreamer vehicleDataStreamer1 = vehicleDataStreamerRepository.findByTripDetailsId(tripDetailId)
                    .orElseThrow(() -> new TripDetailsNotFoundException(tripDetailId));

            vehicleDataStreamerRepository.save(vehicleDataStreamer1);
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public int updateCurrentSpeed(Integer currentSpeed, long tripDetailId) {
        VehicleDataStreamer vehicleDataStreamer = vehicleDataStreamerRepository.findByTripDetailsId(tripDetailId)
                .orElseThrow(() -> new TripDetailsNotFoundException(tripDetailId));

        vehicleDataStreamer.setCurrentSpeed(currentSpeed);
        if (vehicleDataStreamer.isEngineActive() && vehicleDataStreamer.getCurrentSpeed() == 0) {
            startTime = LocalDateTime.now();
        }

        if (vehicleDataStreamer.isEngineActive() && vehicleDataStreamer.getCurrentSpeed() > 0) {
            {
                endTime = LocalDateTime.now();
                carSummaryServiceImpl.endTime = endTime;
                carSummaryServiceImpl.calculateTotalIdleTime(startTime, endTime, tripDetailId);
            }
        }
        vehicleDataStreamerRepository.save(vehicleDataStreamer);
        VehicleDataStreamerDto vehicleDataStreamerDto = entityToDto(vehicleDataStreamer);
        return vehicleDataStreamerDto.getCurrentSpeed();
    }

    public VehicleDataStreamerDto entityToDto(VehicleDataStreamer vehicleDataStreamer) {
        VehicleDataStreamerDto dto = new VehicleDataStreamerDto();

        dto.setStreamId(vehicleDataStreamer.getStreamId());
        dto.setCurrentSpeed(vehicleDataStreamer.getCurrentSpeed());
        dto.setEngineActive(vehicleDataStreamer.isEngineActive());
        dto.setTripDetailsId(vehicleDataStreamer.getTripDetailsId());
        dto.setActive(vehicleDataStreamer.isActive());

        return dto;
    }

}
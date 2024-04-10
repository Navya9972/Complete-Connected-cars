package com.connected.car.vehicle.service.impl;

import com.connected.car.vehicle.dto.CarStatusDto;

import com.connected.car.vehicle.entity.CarStatus;
import com.connected.car.vehicle.entity.TripDetails;
import com.connected.car.vehicle.entity.batteryStatus;
import com.connected.car.vehicle.exceptions.RecordDeactivatedException;
import com.connected.car.vehicle.exceptions.TripDetailsNotFoundException;
import com.connected.car.vehicle.repository.CarStatusRepository;
import com.connected.car.vehicle.repository.TripDetailsRepository;
import com.connected.car.vehicle.service.CarStatusService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarStatusServiceImpl implements CarStatusService {
	
	private static final Logger logger=LoggerFactory.getLogger(CarStatusServiceImpl.class);
	
    @Autowired
    private TripDetailsRepository tripDetailsRepository;
    @Autowired
    private CarStatusRepository carStatusRepository;

    @Override
    public CarStatusDto getCarStatusDetails(int carId) {
    	try {
        List<TripDetails> tripDetails = tripDetailsRepository.findTopByCarIdOrderByTravelEndDateTimeDesc(carId);

        if (!tripDetails.isEmpty()) {
            TripDetails tripDetails1 = tripDetails.get(0);
            int currentFuel = (int)tripDetails1.getCurrentFuelReading();
            CarStatus existingCarStatus = carStatusRepository.findByCarId(carId);
            if (existingCarStatus != null) {
                existingCarStatus.setFuelStatus(currentFuel);
                existingCarStatus.setBatteryStatus(batteryStatus.HIGH);
                existingCarStatus.setLockStatus(false);
                existingCarStatus.setCarId(tripDetails1.getCarId());
                existingCarStatus.setActive(tripDetails1.isActive());
                CarStatus carStatus1 = carStatusRepository.save(existingCarStatus);
                CarStatusDto carStatusDto=entityToDto(carStatus1);
                logger.info("Successfully updated car status details for car with carId: {}", carId);
                return carStatusDto;
            } else {
                CarStatus newCarStatus = new CarStatus();
                newCarStatus.setFuelStatus(currentFuel);
                newCarStatus.setCarId(tripDetails1.getCarId());
                newCarStatus.setBatteryStatus(batteryStatus.HIGH);
                newCarStatus.setLockStatus(false);
                newCarStatus.setActive(tripDetails1.isActive());
                CarStatus carStatus1 = carStatusRepository.save(newCarStatus);
                CarStatusDto carStatusDto=entityToDto(carStatus1);
                logger.info("Successfully created car status details for car with carId: {}", carId);
                return carStatusDto;
            }
        } else {
            throw new TripDetailsNotFoundException(carId);
        }
    	}catch(Exception e)
    	{
            logger.error("Error getting car status details for car ID: {}", carId, e);
    		throw e;
    	}

    }

    private CarStatusDto entityToDto(CarStatus carStatus1) {
        CarStatusDto carStatusDto = new CarStatusDto();
        carStatusDto.setCarStatusId(carStatus1.getCarStatusId());
        carStatusDto.setFuelStatus(carStatus1.getFuelStatus());
        carStatusDto.setCar_id(carStatus1.getCarId());
        carStatusDto.setLockStatus(carStatus1.isLockStatus());
        carStatusDto.setBatteryStatus(carStatus1.getBatteryStatus());
        carStatusDto.setActive(carStatus1.isActive());
        return carStatusDto;
    }

    private CarStatus dtoToEntity(CarStatusDto carStatusDto) {
        CarStatus carStatus = new CarStatus();
        carStatus.setCarStatusId(carStatusDto.getCarStatusId());
        carStatus.setFuelStatus(carStatusDto.getFuelStatus());
        carStatus.setCarId(carStatusDto.getCar_id());
        carStatus.setLockStatus(carStatusDto.isLockStatus());
        carStatus.setBatteryStatus(carStatusDto.getBatteryStatus());
        carStatus.setActive(carStatusDto.isActive());
        return carStatus;
    }


}
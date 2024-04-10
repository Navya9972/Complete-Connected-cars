package com.connected.car.vehicle.service;

import java.time.DateTimeException;
import java.time.Year;
import java.util.List;

import com.connected.car.vehicle.dto.CarDto;
import com.connected.car.vehicle.dto.TripDetailsDto;

public interface CarService {

	CarDto createCar(CarDto carDto);
	CarDto getCarById(Integer userId);
	List<CarDto> getAllCars();
	CarDto updateCar(CarDto car, Integer carId);
	void deleteCar(Integer carId);
	TripDetailsDto getRecentTripDetailsByCarId(int carId);

	List<CarDto> getCarsByUserId(int userId);
	public static boolean isValidYear(int year){
		if(year>0 && year<Year.now().getValue())
			return true;
		else
			return false;

		}
	}


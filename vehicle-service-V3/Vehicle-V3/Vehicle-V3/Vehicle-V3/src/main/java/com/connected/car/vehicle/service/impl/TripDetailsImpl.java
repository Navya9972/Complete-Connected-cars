package com.connected.car.vehicle.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.connected.car.vehicle.exceptions.TripDetailsNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.connected.car.vehicle.dto.TripDetailsDto;
import com.connected.car.vehicle.entity.Car;
import com.connected.car.vehicle.entity.TripDetails;
import com.connected.car.vehicle.exceptions.CarNotFoundException;
import com.connected.car.vehicle.repository.CarRepository;
import com.connected.car.vehicle.repository.TripDetailsRepository;
import com.connected.car.vehicle.service.TripDetailsService;

import jakarta.validation.constraints.AssertTrue;

@Service
public class TripDetailsImpl implements TripDetailsService {
	
	private static final Logger logger=LoggerFactory.getLogger(TripDetailsImpl.class);
	@Autowired
	private TripDetailsRepository tripDetailsRepository;

	@Autowired
	private CarRepository carRepo;

	@Override
	public TripDetailsDto createTripDetails(TripDetailsDto tripDetailsDto) {
		try {
			TripDetails carDetails = dtoToEntity(tripDetailsDto);
			carDetails.setActive(true);
			TripDetails savedCar = tripDetailsRepository.save(carDetails);
	        logger.info("Trip details created successfully");
			return entityToDto(savedCar);
		} catch (Exception ex) {
			logger.error("Error while creating TripDetails");
			throw ex;
		}
	}
	

	@Override
	public TripDetailsDto getTripDetailsById(long tripDetailsId) {
		try {
			TripDetails tripDetails = tripDetailsRepository.findById(tripDetailsId)
					.orElseThrow(() -> new TripDetailsNotFoundException(tripDetailsId));
	        logger.info("Retrieved trip details by tripDetailsId:{}", tripDetailsId);
			return entityToDto(tripDetails);
		} catch (Exception ex) {
			logger.error("Error While getting Trip Details by TripDetailsId:{}",tripDetailsId );
			throw ex;
		}
	}

	@Override
	public List<TripDetailsDto> getAllTripDetails() {
		try {
			List<TripDetails> carDetails = tripDetailsRepository.findAll();

			if (carDetails.isEmpty()) {
				logger.info("No trip details found.");
				throw new TripDetailsNotFoundException();
			}
			List<TripDetailsDto> carDtos = carDetails.stream().map(details -> entityToDto(details))
					.collect(Collectors.toList());
			logger.info("Retrieved all trip details");
			return carDtos;
		} catch (Exception ex) {
	        logger.error("Error while getting all trip details");
			throw ex;
		}
	}

	@Override
	public List<TripDetailsDto> getTripDetailsByCarId(Integer carId) {
		try {
			Car car = carRepo.findById(carId).orElseThrow(() -> new CarNotFoundException(carId));
			List<TripDetails> carDetails = tripDetailsRepository.findByCarId(carId);
			List<TripDetailsDto> carDetailsDto = carDetails.stream().map(details -> entityToDto(details))
					.collect(Collectors.toList());
			if (carDetailsDto.isEmpty()) {
	            logger.info("No trip details found for car with carID:{}", carId);
				throw new TripDetailsNotFoundException(carId);
			}
	        logger.info("Retrieved trip details for car with ID {}", carId);
			return carDetailsDto;
		} catch (Exception ex) {
	        logger.error("Error getting trip details for car with ID: {}", carId);
			throw ex;
		}
	}

	@Override
	public void deleteTripDetails(long tripDetailsId) {
		try {
			TripDetails carDetails = tripDetailsRepository.findById(tripDetailsId)
					.orElseThrow(() -> new TripDetailsNotFoundException(tripDetailsId));
			tripDetailsRepository.delete(carDetails);
	        logger.info("Deleted trip details with tripDetailsId: {}", tripDetailsId);
		} catch (Exception ex) {
	        logger.error("Error deleting trip details with tripDetailsID: {}", tripDetailsId);
			throw ex;
		}
	}

	@Override
	public TripDetailsDto getRecentTripDetailsByCarId(int carId) {
		try {
			List<TripDetails> carDetailsList = tripDetailsRepository.findTopByCarIdOrderByTravelEndDateTimeDesc(carId);
			if (carDetailsList.isEmpty()) {
	            logger.info("No recent trip details found for car with CarId:{}", carId);
 			    throw new TripDetailsNotFoundException(carId);
			}
			TripDetails carDetails = carDetailsList.get(0);
	        logger.info("Retrieved most recent trip details for car with ID {}", carId);
			return entityToDto(carDetails);
		} catch (Exception ex) {
	        logger.error("Error getting most recent trip details for car with carId: {}", carId);
			throw ex;
		}
	}

	private TripDetails dtoToEntity(TripDetailsDto tripDetailsDto) {
		TripDetails tripDetails = new TripDetails();
		tripDetails.setTripDetailsId(tripDetailsDto.getTripDetailsId());
		tripDetails.setCarId(tripDetailsDto.getCarId());
		tripDetails.setMaxSpeed(tripDetailsDto.getMaxSpeed());
		tripDetails.setMinSpeed(tripDetailsDto.getMinSpeed());
		tripDetails.setInitialFuelReading(tripDetailsDto.getInitialFuelReading());
		tripDetails.setCurrentFuelReading(tripDetailsDto.getCurrentFuelReading());
		tripDetails.setOdometerStartReading(tripDetailsDto.getOdometerStartReading());
		tripDetails.setOdometerEndReading(tripDetailsDto.getOdometerEndReading());
		tripDetails.setTravelStartDateTime(tripDetailsDto.getTravelStartDateTime());
		tripDetails.setTravelEndDateTime(tripDetailsDto.getTravelEndDateTime());
		tripDetails.setTripName(tripDetails.getTripName());
		return tripDetails;
	}

	private TripDetailsDto entityToDto(TripDetails tripDetails) {
		TripDetailsDto tripDetailsDto = new TripDetailsDto();
		tripDetailsDto.setTripDetailsId(tripDetails.getTripDetailsId());
		tripDetailsDto.setCarId(tripDetails.getCarId());
		tripDetailsDto.setTravelStartDateTime(tripDetails.getTravelStartDateTime());
		tripDetailsDto.setTravelEndDateTime(tripDetails.getTravelEndDateTime());
		tripDetailsDto.setMaxSpeed(tripDetails.getMaxSpeed());
		tripDetailsDto.setMinSpeed(tripDetails.getMinSpeed());
		tripDetailsDto.setInitialFuelReading(tripDetails.getInitialFuelReading());
		tripDetailsDto.setCurrentFuelReading(tripDetails.getCurrentFuelReading());
		tripDetailsDto.setOdometerStartReading(tripDetails.getOdometerStartReading());
		tripDetailsDto.setOdometerEndReading(tripDetails.getOdometerEndReading());
		tripDetailsDto.setTripName(tripDetails.getTripName());
		tripDetailsDto.setActive(tripDetails.isActive());
		return tripDetailsDto;
	}

}

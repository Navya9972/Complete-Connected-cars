package com.connected.car.vehicle.service.impl;

import com.connected.car.vehicle.dto.CarSummaryDto;
import com.connected.car.vehicle.entity.Car;
import com.connected.car.vehicle.entity.CarSummary;
import com.connected.car.vehicle.entity.TripDetails;
import com.connected.car.vehicle.exceptions.InvalidvalueException;
import com.connected.car.vehicle.exceptions.ResourceNotFoundException;
import com.connected.car.vehicle.repository.CarRepository;
import com.connected.car.vehicle.repository.CarSummaryRepository;
import com.connected.car.vehicle.repository.TripDetailsRepository;
import com.connected.car.vehicle.service.CarSummaryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarSummaryImpl implements CarSummaryService {

	private static final Logger logger = LoggerFactory.getLogger(CarSummaryImpl.class);

	@Autowired
	private CarSummaryRepository carSummaryRepository;

	@Autowired
	private TripDetailsRepository tripDetailsRepository;

	@Autowired
	private CarRepository carRepository;
	
	private Duration totalIdleDuration = Duration.ZERO;
	LocalDateTime startTime = null,
			endTime = null;

	@Override
	public String calculateTotalIdleTime(LocalDateTime startTime, LocalDateTime endTime, Long tripDetailsId) {
		CarSummary carSummary = carSummaryRepository.findByTripDetailsId(tripDetailsId);

		String formattedDurationIdleTime = "";

		if (startTime != null && endTime != null) {
			Duration duration = Duration.between(startTime, endTime);
			totalIdleDuration = totalIdleDuration.plus(duration);
			formattedDurationIdleTime = formatDuration(totalIdleDuration);
			System.out.println(formattedDurationIdleTime);
			carSummary.setIdleTime(formattedDurationIdleTime);
			carSummaryRepository.save(carSummary);
		}
		return formattedDurationIdleTime;
	}
	private static String formatDuration(Duration duration) {
		long day = duration.toDays();
		long hours = duration.toHours();
		long minutes = duration.toMinutesPart();
		long seconds = duration.toSecondsPart();
		return String.format("days:%d|hours:%d|minutes:%d|seconds:%d ", day, hours % 24, minutes, seconds);
	}
	
	public double calculateMileage(int carId, long tripDetailsId) throws Exception {
		try {
			//CarSummary carSummary = carSummaryRepository.findCarSummaryByTripDetailsId(tripDetailsId);
			double totalDistance =calculateTotalDistance(carId,tripDetailsId) ;
			if(totalDistance<=0) {
				logger.warn("Total distance is negetive");
				throw new InvalidvalueException("Total distance is negetive");

			}
			double fuelConsumed = calculateFuelConsumption(carId,tripDetailsId);
			if (fuelConsumed == 0 ) {
				logger.warn("Fuel value is zero for tripdetails ID: {}", tripDetailsId);
				throw new InvalidvalueException("Fuel value is zero");
			}


			double mileage = totalDistance / fuelConsumed;
			///carSummary.setMileage(mileage);
			//carSummaryRepository.save(carSummary);

			logger.info("Mileage calculated successfully for trip details with tripDetailsId {}: {}", tripDetailsId,
					mileage);
			return mileage;
		} catch (Exception e) {
			logger.error("Error calculating mileage for trip details with tripDetailsId: {}", tripDetailsId);
			throw e;
		}
	}

	public List<TripDetails> listOfTripForCarId(int carId) {
		try {
			List<TripDetails> tripDetails = tripDetailsRepository.findAllTripDetailsByCarId(carId);

			if (tripDetails.isEmpty()) {
				logger.warn("No trip details found for car with CarId: {}", carId);
			}
			logger.info("Retrieved {} trip details for car with CarId: {}", tripDetails.size(), carId);
			return tripDetails;
		} catch (Exception e) {
			logger.error("Error while retrieving trip details for car with CarID: {}", carId);
			throw e;
		}
	}
    
	@Override
	public double calculateFuelConsumption(int carId, long tripDetailsId) {
		try {
			
			double initialFuelReading = tripDetailsRepository.findInitialFuelReadingByTripDetailsId(tripDetailsId);
			double currentFuelReading = tripDetailsRepository.findCurrentFuelReadingByTripDetailsId(tripDetailsId);
			double FuelConsumption = initialFuelReading - currentFuelReading;
			if (initialFuelReading<=currentFuelReading) {
				logger.warn("Initial fuel reading cannot be less than current fuel reading for tripDetailsId {}",
						tripDetailsId);
				throw new InvalidvalueException("Initial fuel reading cannot be less than current fuel reading");
			}
			//CarSummary carSummary = carSummaryRepository.findCarSummaryByTripDetailsId(tripDetailsId);
			//carSummary.setFuelConsumption(FuelConsumption);
			//carSummaryRepository.save(carSummary);
			logger.info("Fuel consumption calculated successfully for trip details with tripDetailsId {}: {}",
					tripDetailsId, FuelConsumption);
			return FuelConsumption;
		} catch (Exception e) {
			logger.error("Error while calculating fuel consumption for trip details tripDetailsId: {}", tripDetailsId);
			throw e;
		}
 
	}


	public double calculatePresentAvgMileage(int carId, List<Long> tripDetailsIdList) {
		try {
			double sumMileage = 0;
			double avgMileage = 0;
			for (int i = 0; i < tripDetailsIdList.size(); i++) {
				CarSummary carSummary = carSummaryRepository.findCarSummaryByTripDetailsId(tripDetailsIdList.get(i));
				System.out.println(carSummary);
				if (carSummary != null && carSummary.getMileage() != 0 && !Double.isNaN(carSummary.getMileage()))
					sumMileage = (sumMileage + carSummary.getMileage());
			}
			System.out.println(sumMileage);
			logger.info("Sum of mileages calculated successfully: {}", sumMileage);

			avgMileage = sumMileage / tripDetailsIdList.size();
			Car car = carRepository.findByCarId(carId);
			car.setOverallAvgMileage(avgMileage);
			carRepository.save(car);

			logger.info("Overall average mileage for car with carID {} set to: {}", carId, avgMileage);

			for (int i = 0; i < tripDetailsIdList.size(); i++) {
				CarSummary carSummary = carSummaryRepository.findCarSummaryByTripDetailsId(tripDetailsIdList.get(i));
				if (carSummary != null) {
					carSummary.setOverallAvgMileage(avgMileage);
					carSummaryRepository.save(carSummary);
				}
			}
			return avgMileage;
		} catch (Exception e) {
			logger.error("Error while calculating present average mileage for car with carId:{}", carId);
			throw e;
		}

	}

	@Override
	public double calculateTotalDistance(int carId, long tripDetailsId) {
		try {
			double odometerStartReading = tripDetailsRepository.findOdometerStartReadingByTripDetailsId(tripDetailsId);
			double odometerEndReading = tripDetailsRepository.findOdometerEndReadingByTripDetailsId(tripDetailsId);
			double totalDistance = odometerEndReading - odometerStartReading;

			if (totalDistance < 0) {
				logger.warn("Negative total distance calculated for trip details with tripDetailsId: {}",
						tripDetailsId);
			}

			//CarSummary carSummary = carSummaryRepository.findCarSummaryByTripDetailsId(tripDetailsId);
			// carSummary.setTotalDistance(totalDistance);
			//carSummaryRepository.save(carSummary);
			logger.info("Total distance calculated successfully for trip details with tripDetailsId {}: {}",
					tripDetailsId, totalDistance);
			return totalDistance;
		} catch (Exception e) {
			logger.error("Error while calculating total distance for trip details with tripDetailsId: {}",
					tripDetailsId);
			throw e;
		}

	}

	@Override
	public int calculateDuration(int carId, long tripDetailsId) {
		try {
			LocalDateTime travelStartDateTime = tripDetailsRepository.findTravelStartDateByTripDetailsId(tripDetailsId);
			LocalDateTime travelEndDateTime = tripDetailsRepository.findTravelEndDateByTripDetailsId(tripDetailsId);
			long durationInMinutes = ChronoUnit.MINUTES.between(travelStartDateTime, travelEndDateTime);

			if (durationInMinutes < 0) {
				logger.warn("Negative duration calculated for trip details with tripDetailsId: {}", tripDetailsId);
			}
			//CarSummary carSummary = carSummaryRepository.findCarSummaryByTripDetailsId(tripDetailsId);
			//carSummary.setDuration((int) durationInMinutes);
			//carSummaryRepository.save(carSummary);
			int duration = (int) durationInMinutes;
			logger.info("Duration calculated successfully for trip details with tripDetailsId {}: {} minutes",
					tripDetailsId, durationInMinutes);
			return duration;
		} catch (Exception e) {
			logger.error("Error while calculating duration for trip details with tripDetailsId: {}", tripDetailsId);
			throw e;
		}
	}
	
	@Override
	public CarSummaryDto generateCarSummary(int carId, long tripDetailsId) throws Exception {
		CarSummary carSummary = carSummaryRepository.findCarSummaryByTripDetailsId(tripDetailsId);
		try {
			if (carSummary == null) {
				carSummary = new CarSummary();
				double totalDistance = calculateTotalDistance(carId, tripDetailsId);
				double fuelConsumption = calculateFuelConsumption(carId, tripDetailsId);
				int duration = calculateDuration(carId, tripDetailsId);
				double mileage = calculateMileage(carId, tripDetailsId);
				List<TripDetails> tripDetailsList = listOfTripForCarId(carId);
				List<Long> listofTripIdforCarid = new ArrayList<>();
				for (int i = 0; i < tripDetailsList.size(); i++) {
					listofTripIdforCarid.add(tripDetailsList.get(i).getTripDetailsId());
				}
				double avgMileage = calculatePresentAvgMileage(carId, listofTripIdforCarid);
				String idleTime=calculateTotalIdleTime(startTime,endTime,tripDetailsId);

				carSummary.setTripDetailsId(tripDetailsId);
				carSummary.setTotalDistance(totalDistance);
				carSummary.setFuelConsumption(fuelConsumption);
				carSummary.setDuration(duration);
				carSummary.setMileage(mileage);
				carSummary.setOverallAvgMileage(avgMileage);
				carSummary.setIdleTime(idleTime);
				carSummaryRepository.save(carSummary);
			}
			CarSummaryDto carSummaryDto = entityToDto(carSummary);
			logger.info(
					"Successfully generated car summary for car with carId {} and trip details with tripDetailsId {}",
					carId, tripDetailsId);
			return carSummaryDto;
		} catch (Exception e) {
			logger.error("Error generating car summary for car with carId {} and trip details tripDetailsId {}", carId,
					tripDetailsId);
			throw e;
		}
	}

	@Override
	public List<CarSummary> getFindByTravelStartDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
		try {
			List<CarSummary> carSummaries = carSummaryRepository.FindByTravelStartDateTimeBetween(startDate, endDate);
			if (!carSummaries.isEmpty()) {
				logger.info("Successfully retrieved car summaries between {} and {}", startDate, endDate);
				return carSummaries;
			} else {
				logger.warn("No car summaries found between {} and {}", startDate, endDate);
				throw new ResourceNotFoundException("Data not found between " + startDate + " to " + endDate);
			}
		} catch (Exception e) {
			logger.error("Error retrieving car summaries between {} and {}", startDate, endDate);
			throw e;
		}
	}

	public CarSummaryDto entityToDto(CarSummary carSummary) {
		CarSummaryDto carSummaryDto = new CarSummaryDto();
		carSummaryDto.setCarSummaryId(carSummary.getCarSummaryId());
		carSummaryDto.setFuelConsumption(carSummary.getFuelConsumption());
		carSummaryDto.setMileage(carSummary.getMileage());
		carSummaryDto.setIdleTime(carSummary.getIdleTime());
		carSummaryDto.setTotalDistance(carSummary.getTotalDistance());
		carSummaryDto.setDuration(carSummary.getDuration());
		carSummaryDto.setAvgMileage(carSummary.getOverallAvgMileage());
		carSummaryDto.setTripDetailsId(carSummary.getTripDetailsId());
		return carSummaryDto;
	}
	


/*
	public double calculateMileage(int carId, long tripDetailsId) {
		try {
			CarSummary carSummary = carSummaryRepository.findCarSummaryByTripDetailsId(tripDetailsId);

			double totalDistance = carSummaryRepository.findTotalDistanceByTripDetailsId(tripDetailsId);

			double fuelConsumed = carSummaryRepository.findFuelConsumptionByTripDetailsId(tripDetailsId);
			if (fuelConsumed == 0) {
				logger.error("Fuel value is zero for tripdetails ID: {}", tripDetailsId);
				throw new InvalidFuelvalueException("Fuel value is zero");
			}

			double mileage = totalDistance / fuelConsumed;
			carSummary.setMileage(mileage);
			carSummaryRepository.save(carSummary);

			logger.info("Mileage calculated successfully for trip details with tripDetailsId {}: {}", tripDetailsId,
					mileage);
			return mileage;
		} catch (Exception e) {
			logger.error("Error calculating mileage for trip details with tripDetailsId: {}", tripDetailsId);
			throw e;
		}
	}

	public List<TripDetails> listOfTripForCarId(int carId) {
		try {
			List<TripDetails> tripDetails = tripDetailsRepository.findAllTripDetailsByCarId(carId);

			if (tripDetails.isEmpty()) {
				logger.warn("No trip details found for car with CarId: {}", carId);
			}
			logger.info("Retrieved {} trip details for car with CarId: {}", tripDetails.size(), carId);
			return tripDetails;
		} catch (Exception e) {
			logger.error("Error while retrieving trip details for car with CarID: {}", carId);
			throw e;
		}
	}

	@Override
	public double calculateFuelConsumption(int carId, long tripDetailsId) {
		try {
			double initialFuelReading = tripDetailsRepository.findInitialFuelReadingByTripDetailsId(tripDetailsId);
			double currentFuelReading = tripDetailsRepository.findCurrentFuelReadingByTripDetailsId(tripDetailsId);
			double FuelConsumption = initialFuelReading - currentFuelReading;

			if (FuelConsumption < 0) {
				logger.warn("Negative fuel consumption calculated for trip details with tripDetailsID: {}",
						tripDetailsId);
			}
			CarSummary carSummary = carSummaryRepository.findCarSummaryByTripDetailsId(tripDetailsId);
			carSummary.setFuelConsumption(FuelConsumption);
			carSummaryRepository.save(carSummary);
			logger.info("Fuel consumption calculated successfully for trip details with tripDetailsId {}: {}",
					tripDetailsId, FuelConsumption);
			return FuelConsumption;
		} catch (Exception e) {
			logger.error("Error while calculating fuel consumption for trip details tripDetailsId: {}", tripDetailsId);
			throw e;
		}

	}

	public double calculatePresentAvgMileage(int carId, List<Long> tripDetailsIdList) {
		try {
			double sumMileage = 0;
			double avgMileage = 0;
			for (int i = 0; i < tripDetailsIdList.size(); i++) {
				CarSummary carSummary = carSummaryRepository.findCarSummaryByTripDetailsId(tripDetailsIdList.get(i));
				System.out.println(carSummary);
				if (carSummary != null && carSummary.getMileage() != 0 && !Double.isNaN(carSummary.getMileage()))
					sumMileage = (sumMileage + carSummary.getMileage());
			}
			System.out.println(sumMileage);
			logger.info("Sum of mileages calculated successfully: {}", sumMileage);

			avgMileage = sumMileage / tripDetailsIdList.size();
			Car car = carRepository.findByCarId(carId);
			car.setOverallAvgMileage(avgMileage);
			carRepository.save(car);

			logger.info("Overall average mileage for car with carID {} set to: {}", carId, avgMileage);

			for (int i = 0; i < tripDetailsIdList.size(); i++) {
				CarSummary carSummary = carSummaryRepository.findCarSummaryByTripDetailsId(tripDetailsIdList.get(i));
				if (carSummary != null) {
					carSummary.setOverallAvgMileage(avgMileage);
					carSummaryRepository.save(carSummary);
				}
			}
			return avgMileage;
		} catch (Exception e) {
			logger.error("Error while calculating present average mileage for car with carId:{}", carId);
			throw e;
		}

	}

	@Override
	public double calculateTotalDistance(int carId, long tripDetailsId) {
		try {
			double odometerStartReading = tripDetailsRepository.findOdometerStartReadingByTripDetailsId(tripDetailsId);
			double odometerEndReading = tripDetailsRepository.findOdometerEndReadingByTripDetailsId(tripDetailsId);
			double totalDistance = odometerEndReading - odometerStartReading;

			if (totalDistance < 0) {
				logger.warn("Negative total distance calculated for trip details with tripDetailsId: {}",
						tripDetailsId);
			}

			CarSummary carSummary = carSummaryRepository.findCarSummaryByTripDetailsId(tripDetailsId);
			carSummary.setTotalDistance(totalDistance);
			carSummaryRepository.save(carSummary);
			logger.info("Total distance calculated successfully for trip details with tripDetailsId {}: {}",
					tripDetailsId, totalDistance);
			return totalDistance;
		} catch (Exception e) {
			logger.error("Error while calculating total distance for trip details with tripDetailsId: {}",
					tripDetailsId);
			throw e;
		}

	}

	@Override
	public int calculateDuration(int carId, long tripDetailsId) {
		try {
			LocalDateTime travelStartDateTime = tripDetailsRepository.findTravelStartDateByTripDetailsId(tripDetailsId);
			LocalDateTime travelEndDateTime = tripDetailsRepository.findTravelEndDateByTripDetailsId(tripDetailsId);
			long durationInMinutes = ChronoUnit.MINUTES.between(travelStartDateTime, travelEndDateTime);

			if (durationInMinutes < 0) {
				logger.warn("Negative duration calculated for trip details with tripDetailsId: {}", tripDetailsId);
			}
			CarSummary carSummary = carSummaryRepository.findCarSummaryByTripDetailsId(tripDetailsId);
			carSummary.setDuration((int) durationInMinutes);
			carSummaryRepository.save(carSummary);
			int duration = (int) durationInMinutes;
			logger.info("Duration calculated successfully for trip details with tripDetailsId {}: {} minutes",
					tripDetailsId, durationInMinutes);
			return duration;
		} catch (Exception e) {
			logger.error("Error while calculating duration for trip details with tripDetailsId: {}", tripDetailsId);
			throw e;
		}
	}

	@Override
	public CarSummaryDto generateCarSummary(int carId, long tripDetailsId) {
		CarSummary carSummary = carSummaryRepository.findCarSummaryByTripDetailsId(tripDetailsId);
		try {
			if (carSummary == null) {
				carSummary = new CarSummary();
				double totalDistance = calculateTotalDistance(carId, tripDetailsId);
				double fuelConsumption = calculateFuelConsumption(carId, tripDetailsId);
				int duration = calculateDuration(carId, tripDetailsId);
				double mileage = calculateMileage(carId, tripDetailsId);
				List<TripDetails> tripDetailsList = listOfTripForCarId(carId);
				List<Long> listofTripIdforCarid = new ArrayList<>();
				for (int i = 0; i < tripDetailsList.size(); i++) {
					listofTripIdforCarid.add(tripDetailsList.get(i).getTripDetailsId());
				}
				double avgMileage = calculatePresentAvgMileage(carId, listofTripIdforCarid);

				carSummary.setTripDetailsId(tripDetailsId);
				carSummary.setTotalDistance(totalDistance);
				carSummary.setFuelConsumption(fuelConsumption);
				carSummary.setDuration(duration);
				carSummary.setMileage(mileage);
				carSummary.setOverallAvgMileage(avgMileage);
				carSummary.setIdleTime("0");// yet to add calculateIdleTime
				carSummary.setParking_time("0");// yet to add
				carSummaryRepository.save(carSummary);
			}
			CarSummaryDto carSummaryDto = entityToDto(carSummary);
			logger.info(
					"Successfully generated car summary for car with carId {} and trip details with tripDetailsId {}",
					carId, tripDetailsId);
			return carSummaryDto;
		} catch (Exception e) {
			logger.error("Error generating car summary for car with carId {} and trip details tripDetailsId {}", carId,
					tripDetailsId);
			throw e;
		}
	}

	@Override
	public List<CarSummary> getFindByTravelStartDateBetween(Date startDate, Date endDate) {
		try {
			List<CarSummary> carSummaries = carSummaryRepository.FindByTravelStartDateTimeBetween(startDate, endDate);
			if (!carSummaries.isEmpty()) {
				logger.info("Successfully retrieved car summaries between {} and {}", startDate, endDate);
				return carSummaries;
			} else {
				logger.warn("No car summaries found between {} and {}", startDate, endDate);
				throw new DataNotFoundException("Data not found between " + startDate + " to " + endDate);
			}
		} catch (Exception e) {
			logger.error("Error retrieving car summaries between {} and {}", startDate, endDate);
			throw e;
		}
	}

	public CarSummaryDto entityToDto(CarSummary carSummary) {
		CarSummaryDto carSummaryDto = new CarSummaryDto();
		carSummaryDto.setCarSummaryId(carSummary.getCarSummaryId());
		carSummaryDto.setFuelConsumption(carSummary.getFuelConsumption());
		carSummaryDto.setMileage(carSummary.getMileage());
		carSummaryDto.setIdleTime(carSummary.getIdleTime());
		carSummaryDto.setParking_time(carSummary.getParking_time());
		carSummaryDto.setTotalDistance(carSummary.getCarSummaryId());
		carSummaryDto.setDuration(carSummary.getDuration());
		carSummaryDto.setAvgMileage(carSummary.getOverallAvgMileage());
		carSummaryDto.setTripDetailsId(carSummary.getCarSummaryId());
		return carSummaryDto;
	}

*/

}

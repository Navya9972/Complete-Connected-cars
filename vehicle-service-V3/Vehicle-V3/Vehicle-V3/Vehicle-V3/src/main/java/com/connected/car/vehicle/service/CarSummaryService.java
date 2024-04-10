package com.connected.car.vehicle.service;

import com.connected.car.vehicle.dto.CarSummaryDto;
import com.connected.car.vehicle.entity.CarSummary;
import com.connected.car.vehicle.entity.TripDetails;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface CarSummaryService {
	public double calculateMileage(int carId, long tripDetailsId) throws Exception;

	public List<TripDetails> listOfTripForCarId(int carId);

	double calculatePresentAvgMileage(int carId, List<Long> tripDetailsIdList);

	public double calculateTotalDistance(int carId, long tripDetailsId);
	
	public double calculateFuelConsumption(int carId, long tripDetailsId);

	public int calculateDuration(int carId, long tripDetailsId);

	public CarSummaryDto generateCarSummary(int carId, long tripDetailsId) throws Exception;

	List<CarSummary> getFindByTravelStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);
	String calculateTotalIdleTime(LocalDateTime startTime, LocalDateTime endTime, Long carSummaryId);
}

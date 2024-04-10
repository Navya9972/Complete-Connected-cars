package com.connected.car.vehicle.repository;

import com.connected.car.vehicle.entity.CarSummary;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;


public interface CarSummaryRepository extends JpaRepository<CarSummary,Long> {

    public CarSummary findCarSummaryByTripDetailsId(long tripDetailsId);

    @Procedure(name = "findFuelConsumedByTripDetailsId")
    public double findFuelConsumptionByTripDetailsId(@Param("trip_details_id") long tripDetailsId);


    @Procedure(name="findTotalDistanceByTripDetailsId")
    public double findTotalDistanceByTripDetailsId(@Param("trip_details_id") long tripDetailsId);
    
    @Procedure(name = "getCarSummaryInDateRange")
    public List<CarSummary> FindByTravelStartDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    public CarSummary findByTripDetailsId(long tripDetailsId);


}
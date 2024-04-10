package com.connected.car.vehicle.repository;

import com.connected.car.vehicle.entity.TripDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface TripDetailsRepository extends JpaRepository<TripDetails,Long> {
	   // @Query("SELECT t FROM TripDetails t where t.carId=?1")


	 List<TripDetails> findByCarId(int  userId);
	    public List<TripDetails> findAllTripDetailsByCarId(int carId);

	    public List<TripDetails> findTopByCarIdOrderByTravelEndDateTimeDesc(int carId);
	    @Procedure(name = "getInitialFuelReading")
	    public double findInitialFuelReadingByTripDetailsId(@Param("trip_details_id")long tripDetailsId);

	    @Procedure(name = "getCurrentFuelReading")
	    public double findCurrentFuelReadingByTripDetailsId(@Param("trip_details_id")long tripDetailsId);

     	@Procedure(name = "getOdometerStartReading")
     	public double findOdometerStartReadingByTripDetailsId(@Param("trip_details_id")long tripDetailsId);

	   @Procedure(name = "getOdometerEndReading")
	   public double findOdometerEndReadingByTripDetailsId(@Param("trip_details_id")long tripDetailsId);

	   @Procedure(name = "getTravelStartDateTime")
	   public LocalDateTime findTravelStartDateByTripDetailsId(@Param("trip_details_id")long tripDetailsId);

	   @Procedure(name = "getTravelEndDateTime")
	   public LocalDateTime findTravelEndDateByTripDetailsId(@Param("trip_details_id")long tripDetailsId);

	   public  TripDetails findByTripDetailsId(long tripDetailsId);
	}
	 



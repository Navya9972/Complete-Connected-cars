package com.connected.car.vehicle.service;


import com.connected.car.vehicle.dto.TripDetailsDto;

import java.util.List;

public interface TripDetailsService {
    TripDetailsDto createTripDetails(TripDetailsDto carDetailsDto);
    TripDetailsDto getTripDetailsById(long tripDetailsId);
    void deleteTripDetails(long tripDetailsId);

    TripDetailsDto getRecentTripDetailsByCarId(int carId);

    List<TripDetailsDto> getTripDetailsByCarId(Integer carId);
    List<TripDetailsDto> getAllTripDetails();

}

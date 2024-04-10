package com.connected.car.vehicle.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.connected.car.vehicle.entity.Car;
import com.connected.car.vehicle.entity.CarSummary;
import com.connected.car.vehicle.entity.TripDetails;
import com.connected.car.vehicle.repository.CarSummaryRepository;
import com.connected.car.vehicle.repository.TripDetailsRepository;
import com.connected.car.vehicle.exceptions.TripDetailsNotFoundException;
import com.connected.car.vehicle.service.impl.CarSummaryImpl;
import com.connected.car.vehicle.service.impl.TripDetailsImpl;
import com.connected.car.vehicle.repository.CarRepository;
import com.connected.car.vehicle.exceptions.InvalidvalueException;
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarSummaryImplTest{

    @Mock
    private CarSummaryRepository carSummaryRepository;

    @InjectMocks
    private CarSummaryImpl carSummaryImpl;
    @Mock
    private TripDetailsRepository TripDetailsRepository;
    @Mock
    private CarRepository CarRepository;

    @InjectMocks
    private TripDetailsImpl tripDetailsImpl;


    @Test
    @Order(1)
    void testListOfTripForCarId_positive() {
        int carId = 1;
        TripDetails tripDetails1 = new TripDetails();
        tripDetails1.setTripDetailsId(1L);
        tripDetails1.setCarId(1);
        tripDetails1.setCurrentFuelReading(25);
        tripDetails1.setInitialFuelReading(67);
        tripDetails1.setMaxSpeed(34);
        tripDetails1.setMinSpeed(38);
        tripDetails1.setOdometerEndReading(456);
        tripDetails1.setOdometerStartReading(567);
        TripDetails tripDetails2 = new TripDetails();
        tripDetails2.setTripDetailsId(2L);
        tripDetails2.setCarId(2);
        tripDetails2.setCurrentFuelReading(90);
        tripDetails2.setInitialFuelReading(89);
        tripDetails2.setMaxSpeed(67);
        tripDetails2.setMinSpeed(23);
        tripDetails2.setOdometerEndReading(890);
        tripDetails2.setOdometerStartReading(1112);
        List<TripDetails> mockCars = Arrays.asList(tripDetails1,tripDetails2);
        Mockito.when(TripDetailsRepository.findAllTripDetailsByCarId(carId))
                .thenReturn(mockCars);
        List<TripDetails> result = carSummaryImpl.listOfTripForCarId(carId);
        assertEquals(mockCars, result);
    }

    @Test
    @Order(2)
    void ListOfTripForCarIdTestNegative() {
        int carId = 1;
        Mockito.when(TripDetailsRepository.findAllTripDetailsByCarId(carId))
                .thenThrow(new RuntimeException("Simulating an error"));
        Assertions.assertThrows(Exception.class, () -> {
            carSummaryImpl.listOfTripForCarId(carId);
        });
    }

    @Test
    @Order(3)
    void CalculateTotalDistanceTest() {
        long tripDetailsId = 1L;
        double odometerStartReading = 100.0;
        double odometerEndReading = 200.0;
        when(TripDetailsRepository.findOdometerStartReadingByTripDetailsId(tripDetailsId))
                .thenReturn(odometerStartReading);
        when(TripDetailsRepository.findOdometerEndReadingByTripDetailsId(tripDetailsId))
                .thenReturn(odometerEndReading);
        double result = carSummaryImpl.calculateTotalDistance(1, tripDetailsId);
        assertEquals(odometerEndReading - odometerStartReading, result);
    }

    @Test
    @Order(4)
    void CalculateTotalDistanceTestnegative() {
        long tripDetailsId = 1L;
        when(TripDetailsRepository.findOdometerStartReadingByTripDetailsId(tripDetailsId))
                .thenThrow(new RuntimeException("Simulating an error"));
        assertThrows(Exception.class, () -> {
            carSummaryImpl.calculateTotalDistance(1, tripDetailsId);
        });
    }

    @Test
    @Order(5)
    void CalculateDurationTestpositive() {
        long tripDetailsId = 1L;
        LocalDateTime travelStartDateTime = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime travelEndDateTime = LocalDateTime.of(2022, 1, 1, 12, 30);
        when(TripDetailsRepository.findTravelStartDateByTripDetailsId(tripDetailsId))
                .thenReturn(travelStartDateTime);
        when(TripDetailsRepository.findTravelEndDateByTripDetailsId(tripDetailsId))
                .thenReturn(travelEndDateTime);
        int result = carSummaryImpl.calculateDuration(1, tripDetailsId);
        long expectedDurationInMinutes = ChronoUnit.MINUTES.between(travelStartDateTime, travelEndDateTime);
        assertEquals(expectedDurationInMinutes, result);
    }


    @Test
    @Order(6)
    void CalculateDurationTestnegative() {
        long tripDetailsId = 1L;
        when(TripDetailsRepository.findTravelStartDateByTripDetailsId(tripDetailsId))
                .thenThrow(new RuntimeException("Simulating an error"));
        assertThrows(Exception.class, () -> {
            carSummaryImpl.calculateDuration(1, tripDetailsId);
        });
    }

    @Test
    @Order(7)
    void CalculateFuelConsumptionTestpositive() {
        long tripDetailsId = 1L;
        double initialFuelReading = 100.0;
        double currentFuelReading = 50.0;
        when(TripDetailsRepository.findInitialFuelReadingByTripDetailsId(tripDetailsId))
                .thenReturn(initialFuelReading);
        when(TripDetailsRepository.findCurrentFuelReadingByTripDetailsId(tripDetailsId))
                .thenReturn(currentFuelReading);
        double result = carSummaryImpl.calculateFuelConsumption(1, tripDetailsId);
        double expectedFuelConsumption = initialFuelReading - currentFuelReading;
        assertEquals(expectedFuelConsumption, result);
    }

    @Test
    @Order(8)
    void testCalculateFuelConsumption_negative() {
        long tripDetailsId = 1L;
        when(TripDetailsRepository.findInitialFuelReadingByTripDetailsId(tripDetailsId))
                .thenReturn(50.0);
        when(TripDetailsRepository.findCurrentFuelReadingByTripDetailsId(tripDetailsId))
                .thenReturn(100.0);
        assertThrows(InvalidvalueException.class, () -> {
            carSummaryImpl.calculateFuelConsumption(1, tripDetailsId);
        });
    }

}

package com.connected.car.vehicle.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.connected.car.vehicle.repository.TripDetailsRepository;
import com.connected.car.vehicle.service.impl.TripDetailsImpl;
import com.connected.car.vehicle.repository.CarRepository;
import com.connected.car.vehicle.dto.TripDetailsDto;
import com.connected.car.vehicle.entity.Car;
import com.connected.car.vehicle.entity.TripDetails;
import com.connected.car.vehicle.exceptions.CarNotFoundException;
import com.connected.car.vehicle.exceptions.TripDetailsNotFoundException;



@ExtendWith(MockitoExtension.class)
public class TripDetailsImplTest {

    @Mock
    private TripDetailsRepository tripDetailsRepository;

    @InjectMocks
    private TripDetailsImpl tripDetailsImpl;

    @Mock
    private CarRepository CarRepository;

    @Test
    @Order(1)
    void testCreateTripDetails() {
        TripDetailsDto expectedTripDetailsDto = new TripDetailsDto();
        expectedTripDetailsDto.setCarId(1);
        expectedTripDetailsDto.setCurrentFuelReading(25);
        expectedTripDetailsDto.setInitialFuelReading(67);
        expectedTripDetailsDto.setMaxSpeed(34);
        expectedTripDetailsDto.setMinSpeed(38);
        expectedTripDetailsDto.setOdometerEndReading(456);
        expectedTripDetailsDto.setOdometerStartReading(567);
        expectedTripDetailsDto.setTripDetailsId(2);

        TripDetails tripDetails = new TripDetails();
        tripDetails.setCarId(1);
        tripDetails.setCurrentFuelReading(25);
        tripDetails.setInitialFuelReading(67);
        tripDetails.setMaxSpeed(34);
        tripDetails.setMinSpeed(38);
        tripDetails.setOdometerEndReading(456);
        tripDetails.setOdometerStartReading(567);
        tripDetails.setTripDetailsId(2);

        when(tripDetailsRepository.save(any(TripDetails.class))).thenReturn(tripDetails);

        TripDetailsDto resultDto = tripDetailsImpl.createTripDetails(expectedTripDetailsDto);

        assertNotNull(resultDto);
        assertEquals(1, resultDto.getCarId());
        assertEquals(25, resultDto.getCurrentFuelReading());
        assertEquals(67, resultDto.getInitialFuelReading());
        assertEquals(34, resultDto.getMaxSpeed());
        assertEquals(38, resultDto.getMinSpeed());
        assertEquals(456, resultDto.getOdometerEndReading());
        assertEquals(567, resultDto.getOdometerStartReading());
        assertEquals(2, resultDto.getTripDetailsId());
    }

    @Test
    @Order(2)
    void testCreateTripDetails_Exception() {
        TripDetailsDto inputDto = new TripDetailsDto();
        when(tripDetailsRepository.save(any())).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> {
            tripDetailsImpl.createTripDetails(inputDto);
        });
        verify(tripDetailsRepository, times(1)).save(any(TripDetails.class));
    }


    @Test
    @Order(3)
    void testGetTripDetailsById_Success() {
        long tripDetailsId = 1;
        TripDetails mockedTripDetails = new TripDetails();
        mockedTripDetails.setTripDetailsId(tripDetailsId);
        mockedTripDetails.setCarId(1);
        mockedTripDetails.setCurrentFuelReading(25);
        mockedTripDetails.setInitialFuelReading(67);
        mockedTripDetails.setMaxSpeed(34);
        mockedTripDetails.setMinSpeed(38);
        mockedTripDetails.setOdometerEndReading(456);
        mockedTripDetails.setOdometerStartReading(567);
        when(tripDetailsRepository.findById(tripDetailsId)).thenReturn(java.util.Optional.of(mockedTripDetails));
        TripDetailsDto resultDto = tripDetailsImpl.getTripDetailsById(tripDetailsId);
        assertNotNull(resultDto);
        assertEquals(tripDetailsId, resultDto.getTripDetailsId());
    }


    @Test
    @Order(4)
    void testGetTripDetailsById_IdNotFoundException() {
        long tripDetailsId = 1;
        when(tripDetailsRepository.findById(tripDetailsId)).thenReturn(java.util.Optional.empty());
        assertThrows(TripDetailsNotFoundException.class, () -> {
            tripDetailsImpl.getTripDetailsById(tripDetailsId);
        });

    }

    @Test
    @Order(5)
    void testDeleteTripDetails() {
        long tripDetailsId = 1L;
        TripDetails tripDetails = new TripDetails();
        tripDetails.setTripDetailsId(tripDetailsId);
        tripDetails.setCarId(1);
        tripDetails.setCurrentFuelReading(25);
        tripDetails.setInitialFuelReading(67);
        tripDetails.setMaxSpeed(34);
        tripDetails.setMinSpeed(38);
        tripDetails.setOdometerEndReading(456);
        tripDetails.setOdometerStartReading(567);
        when(tripDetailsRepository.findById(tripDetailsId)).thenReturn(java.util.Optional.of(tripDetails));
        tripDetailsImpl.deleteTripDetails(tripDetailsId);
    }

    @Test
    @Order(6)
    void testDeleteTripDetails_NoDetailsFound() {
        long tripDetailsId = 1;
        when(tripDetailsRepository.findById(tripDetailsId)).thenReturn(java.util.Optional.empty());
        assertThrows(TripDetailsNotFoundException.class, () -> {
            tripDetailsImpl.deleteTripDetails(tripDetailsId);
        });
    }


    @Test
    @Order(7)
    void testGetAllTripDetails() {
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
        when(tripDetailsRepository.findAll()).thenReturn(mockCars);
        List<TripDetailsDto> resultDtos = tripDetailsImpl.getAllTripDetails();

        assertNotNull(resultDtos);
        assertEquals(2, resultDtos.size());

        TripDetailsDto resultDto1 = resultDtos.get(0);
        assertEquals(1L, resultDto1.getTripDetailsId());
        assertEquals(25, resultDto1.getCurrentFuelReading());
        assertEquals(67, resultDto1.getInitialFuelReading());
        assertEquals(34, resultDto1.getMaxSpeed());
        assertEquals(38, resultDto1.getMinSpeed());
        assertEquals(456, resultDto1.getOdometerEndReading());
        assertEquals(567, resultDto1.getOdometerStartReading());
        TripDetailsDto resultDto2 = resultDtos.get(1);
        assertEquals(2L, resultDto2.getTripDetailsId());
        assertEquals(90, resultDto2.getCurrentFuelReading());
        assertEquals(89, resultDto2.getInitialFuelReading());
        assertEquals(67, resultDto2.getMaxSpeed());
        assertEquals(23, resultDto2.getMinSpeed());
        assertEquals(890, resultDto2.getOdometerEndReading());
        assertEquals(1112, resultDto2.getOdometerStartReading());
    }

    @Test
    @Order(8)
    void testGetAllTripDetails_NoDetailsFound() {
        when(tripDetailsRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(TripDetailsNotFoundException.class, () -> {
            tripDetailsImpl.getAllTripDetails();
        });
    }

    @Test
    @Order(9)
    void testGetAllTripDetails_Exception() {
        when(tripDetailsRepository.findAll()).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class, () -> {
            tripDetailsImpl.getAllTripDetails();
        });
    }


    @Test
    @Order(10)
    void testGetRecentTripDetailsByCarId() {
        int carId = 1;
        int carId2 = 2;
        LocalDateTime now = LocalDateTime.now();
        TripDetails recentTripDetails = new TripDetails();
        recentTripDetails.setTripDetailsId(1L);
        recentTripDetails.setCarId(carId);
        recentTripDetails.setTravelEndDateTime(now);
        recentTripDetails.setCurrentFuelReading(25);
        recentTripDetails.setInitialFuelReading(67);
        recentTripDetails.setMaxSpeed(34);
        recentTripDetails.setMinSpeed(38);
        recentTripDetails.setOdometerEndReading(456);
        recentTripDetails.setOdometerStartReading(567);
        when(tripDetailsRepository.findTopByCarIdOrderByTravelEndDateTimeDesc(carId2))
                .thenReturn(Arrays.asList(recentTripDetails));

        TripDetailsDto resultDto = tripDetailsImpl.getRecentTripDetailsByCarId(carId2);
        assertNotNull(resultDto);
        assertEquals(1L, resultDto.getTripDetailsId());
        assertEquals(carId, resultDto.getCarId());
        assertEquals(now, resultDto.getTravelEndDateTime());
        assertEquals(25, resultDto.getCurrentFuelReading());
        assertEquals(67, resultDto.getInitialFuelReading());
        assertEquals(34, resultDto.getMaxSpeed());
        assertEquals(38, resultDto.getMinSpeed());
        assertEquals(456, resultDto.getOdometerEndReading());
        assertEquals(567, resultDto.getOdometerStartReading());

    }
    @Test
    @Order(11)
    void testGetRecentTripDetailsByCarId_NoDetailsFound() {
        int carId = 1;
        when(tripDetailsRepository.findTopByCarIdOrderByTravelEndDateTimeDesc(carId)).thenReturn(Collections.emptyList());
        TripDetailsNotFoundException exception = assertThrows(TripDetailsNotFoundException.class, () -> {
            tripDetailsImpl.getRecentTripDetailsByCarId(carId);
        });

    }

    @Test
    @Order(12)
    void testGetTripDetailsByCarId_Success() {
        Integer carId = 1;
        Car mockedCar = new Car();
        mockedCar.setCarId(carId);
        when(CarRepository.findById(carId)).thenReturn(java.util.Optional.of(mockedCar));
        TripDetails tripDetails1 = new TripDetails();
        tripDetails1.setTripDetailsId(1L);
        TripDetails tripDetails2 = new TripDetails();
        tripDetails2.setTripDetailsId(2L);
        when(tripDetailsRepository.findByCarId(carId)).thenReturn(Arrays.asList(tripDetails1, tripDetails2));
        List<TripDetailsDto> resultDtoList = tripDetailsImpl.getTripDetailsByCarId(carId);
        verify(tripDetailsRepository, times(1)).findByCarId(carId);
        assertNotNull(resultDtoList);
        assertEquals(2, resultDtoList.size());

    }

    @Test
    @Order(13)
    void testGetTripDetailsByCarId_CarNotFound() {
        Integer carId = 1;
        when(CarRepository.findById(carId)).thenReturn(java.util.Optional.empty());
        assertThrows(CarNotFoundException.class, () -> {
            tripDetailsImpl.getTripDetailsByCarId(carId);
        });
        verify(CarRepository, times(1)).findById(carId);
    }
    
    @Test
    @Order(14)
    void testGetTripDetailsByCarIdNotFound() {
        Integer carId = 1;
        when(CarRepository.findById(carId)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> tripDetailsImpl.getTripDetailsByCarId(carId));
    }
}





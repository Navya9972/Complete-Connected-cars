package com.connected.car.vehicle.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.connected.car.vehicle.dto.CarStatusDto;
import com.connected.car.vehicle.entity.CarStatus;
import com.connected.car.vehicle.entity.TripDetails;
import com.connected.car.vehicle.entity.batteryStatus;
import com.connected.car.vehicle.exceptions.TripDetailsNotFoundException;
import com.connected.car.vehicle.repository.CarStatusRepository;
import com.connected.car.vehicle.repository.TripDetailsRepository;
import com.connected.car.vehicle.service.impl.CarStatusServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CarStatusServiceImplTest {

    @InjectMocks
    private CarStatusServiceImpl carStatusService;

    @Mock
    private TripDetailsRepository tripDetailsRepository;

    @Mock
    private CarStatusRepository carStatusRepository;

    @Test
    public void testGetCarStatusDetails() {
        int carId = 1;
        TripDetails tripDetails = new TripDetails();
        tripDetails.setCarId(carId);
        tripDetails.setCurrentFuelReading(10.0);
        tripDetails.setActive(true);
        List<TripDetails> tripDetailsList = Collections.singletonList(tripDetails);
        when(tripDetailsRepository.findTopByCarIdOrderByTravelEndDateTimeDesc(carId)).thenReturn(tripDetailsList);
        when(carStatusRepository.findByCarId(carId)).thenReturn(null);
        CarStatus savedCarStatus = new CarStatus();
        savedCarStatus.setCarId(carId);
        savedCarStatus.setFuelStatus(40);
        savedCarStatus.setBatteryStatus(batteryStatus.HIGH);
        savedCarStatus.setLockStatus(false);
        savedCarStatus.setActive(true);

        when(carStatusRepository.save(any(CarStatus.class))).thenReturn(savedCarStatus);
        CarStatusDto carStatusDto = carStatusService.getCarStatusDetails(carId);
        verify(tripDetailsRepository, times(1)).findTopByCarIdOrderByTravelEndDateTimeDesc(carId);
        verify(carStatusRepository, times(1)).findByCarId(carId);
        verify(carStatusRepository, times(1)).save(any(CarStatus.class));
        assertEquals(carId, carStatusDto.getCar_id());
        assertEquals(40, carStatusDto.getFuelStatus());
        assertEquals(batteryStatus.HIGH, carStatusDto.getBatteryStatus());
        assertEquals(false, carStatusDto.isLockStatus());
        assertEquals(true, carStatusDto.isActive());
    }

    @Test
    public void testGetCarStatusDetailsTripDetailsNotFoundException() {
        int carId = 1;

        when(tripDetailsRepository.findTopByCarIdOrderByTravelEndDateTimeDesc(carId)).thenReturn(Collections.emptyList());
        assertThrows(TripDetailsNotFoundException.class, () -> carStatusService.getCarStatusDetails(carId));

 
    }
}

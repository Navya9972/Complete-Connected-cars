package com.connected.car.vehicle.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.connected.car.vehicle.service.impl.*;
import com.connected.car.vehicle.exceptions.CarNotFoundException;
import com.connected.car.vehicle.exceptions.RecordDeactivatedException;
import com.connected.car.vehicle.exceptions.TripDetailsNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.connected.car.vehicle.dto.CarDto;
import com.connected.car.vehicle.dto.TripDetailsDto;
import com.connected.car.vehicle.entity.Car;
import com.connected.car.vehicle.repository.TripDetailsRepository;
import com.connected.car.vehicle.repository.CarRepository;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carServiceImpl;
    @InjectMocks
    private TripDetailsImpl TripDetailsImpl;
    @Mock
    private TripDetailsRepository TripDetailsRepository;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    @Order(1)
    public void testCreateCar() {
        CarDto inputCarDto = new CarDto();
        inputCarDto.setCarId(1);
        inputCarDto.setModel("12334");
        inputCarDto.setManufacturer("toyota");
        inputCarDto.setRegistrationNumber("KA19873456");
        inputCarDto.setEngineType("Petrol");
        inputCarDto.setManufactureYear(1994);
        inputCarDto.setDescription("the gud car ");
        inputCarDto.setVinNumber("56789");
        inputCarDto.setUserId(1);

        inputCarDto.setActive(true);
        Car expectedSavedCar = new Car();
        expectedSavedCar.setCarId(1);
        expectedSavedCar.setModel("12334");
        expectedSavedCar.setManufacturer("toyota");
        expectedSavedCar.setRegistrationNumber("KA19873456");
        expectedSavedCar.setEngineType("Petrol");
        expectedSavedCar.setManufactureYear(1994);
        expectedSavedCar.setDescription("the gud car ");
        expectedSavedCar.setVinNumber("56789");
        expectedSavedCar.setUserId(1);
        expectedSavedCar.setOverallAvgMileage(456.7);
        expectedSavedCar.setActive(true);
        when(carRepository.save(any(Car.class))).thenReturn(expectedSavedCar);
        CarDto resultCarDto = carServiceImpl.createCar(inputCarDto);
        assertNotNull(resultCarDto);
    }
    
    @Test
    @Order(2)
    void testCreateCar_Exception() {
        CarDto inputDto = new CarDto();
        inputDto.setCarId(1);
        inputDto.setModel("12334");
        inputDto.setManufacturer("toyota");
        inputDto.setRegistrationNumber("KA19873456");
        inputDto.setEngineType("Petrol");
        inputDto.setManufactureYear(1994);
        inputDto.setDescription("the gud car ");
        inputDto.setVinNumber("56789");
        inputDto.setUserId(1);
        inputDto.setActive(true);
        Car expectedSavedCar = new Car();
        expectedSavedCar.setCarId(1);
        expectedSavedCar.setModel("12334");
        expectedSavedCar.setManufacturer("toyota");
        expectedSavedCar.setRegistrationNumber("KA19873456");
        expectedSavedCar.setEngineType("Petrol");
        expectedSavedCar.setManufactureYear(1994);
        expectedSavedCar.setDescription("the gud car ");
        expectedSavedCar.setVinNumber("56789");
        expectedSavedCar.setUserId(1);
        expectedSavedCar.setOverallAvgMileage(456.7);
        expectedSavedCar.setActive(true);
        when(carRepository.save(any())).thenThrow(new RuntimeException("Simulated exception"));
        assertThrows(RuntimeException.class, () -> {
            carServiceImpl.createCar(inputDto);
        });
    }



    @Test
    @Order(3)
    public void testGetCarById() {
        Integer carId = 1;
        Car expectedCar = new Car();
        expectedCar.setCarId(carId);
        expectedCar.setModel("12334");
        expectedCar.setActive(true);
        when(carRepository.findById(carId)).thenReturn(Optional.of(expectedCar));
        CarDto resultCarDto = carServiceImpl.getCarById(carId);
        assertNotNull(resultCarDto);
        assertEquals(1,expectedCar.getCarId());
    }
    @Test
    @Order(4)
    public void testGetCarById_NotFound() {
        Integer carId = 67;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> carServiceImpl.getCarById(carId));
    }
    @Test
    @Order(5)
    void testDeleteCar_CarNotFoundExceptionTest() {
        Integer carId = 1;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> {
            carServiceImpl.deleteCar(carId);
        });
        verify(carRepository, times(1)).findById(carId);
        verify(carRepository, times(0)).delete(any());
    }
    @Test
    @Order(6)
    public void testDeleteCar() {
        Integer carId = 1;
        Car car = new Car();
        car.setCarId(carId);
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        assertDoesNotThrow(() -> carServiceImpl.deleteCar(carId));
    }


    @Test
    @Order(7)
    void testDeleteCar_ErrorDuringDeletion() {
        Integer carId = 1;
        Car carToDelete = new Car();
        when(carRepository.findById(carId)).thenReturn(Optional.of(carToDelete));
        doThrow(new RuntimeException("Simulated deletion error")).when(carRepository).delete(carToDelete);
        assertThrows(RuntimeException.class, () -> {
            carServiceImpl.deleteCar(carId);
        });
        verify(carRepository, times(1)).findById(carId);
        verify(carRepository, times(1)).delete(carToDelete);
    }

    @Test
    @Order(8)
    public void testUpdateCar() {
        Integer carId = 1;
        CarDto updatedCarDto = new CarDto();
        updatedCarDto.setModel("NewModel");
        updatedCarDto.setDescription("Updated description");
        updatedCarDto.setRegistrationNumber("NEW123");
        updatedCarDto.setManufacturer("NewManufacturer");
        updatedCarDto.setManufactureYear(2022);
        updatedCarDto.setActive(true);
        Car existingCar = new Car();
        existingCar.setCarId(carId);
        existingCar.setModel("OldModel");
        existingCar.setDescription("Old description");
        existingCar.setRegistrationNumber("OLD123");
        existingCar.setManufacturer("OldManufacturer");
        existingCar.setManufactureYear(2010);
        existingCar.setActive(true);
        when(carRepository.findById(carId)).thenReturn(Optional.of(existingCar));
        when(carRepository.save(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));
        CarDto resultCarDto = carServiceImpl.updateCar(updatedCarDto, carId);
        assertNotNull(resultCarDto);
        assertEquals(updatedCarDto.getModel(), resultCarDto.getModel());
        assertEquals(updatedCarDto.getDescription(), resultCarDto.getDescription());
        assertEquals(updatedCarDto.getRegistrationNumber(), resultCarDto.getRegistrationNumber());
        assertEquals(updatedCarDto.getManufacturer(), resultCarDto.getManufacturer());
        assertEquals(updatedCarDto.getManufactureYear(), resultCarDto.getManufactureYear());
    }
    @Test
    @Order(9)
    public void testUpdateCar_NotFound() {
        Integer carId = 1;
        CarDto updatedCarDto = new CarDto();
        updatedCarDto.setModel("NewModel");
        updatedCarDto.setDescription("Updated description");
        updatedCarDto.setRegistrationNumber("NEW123");
        updatedCarDto.setManufacturer("NewManufacturer");
        updatedCarDto.setManufactureYear(2022);

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> carServiceImpl.updateCar(updatedCarDto, carId));

    }
    @Test
    @Order(10)
    public void testGetAllCars() {
        Car car1 = new Car();
        car1.setCarId(1);
        car1.setModel("12334");
        car1.setManufacturer("toyota");
        car1.setRegistrationNumber("KA19873456");
        car1.setEngineType("Petrol");
        car1.setManufactureYear(1994);
        car1.setDescription("the gud car");
        car1.setVinNumber("56789");
        car1.setUserId(1);
        car1.setOverallAvgMileage(456.711110);
        car1.setActive(true);

        Car car2 = new Car();
        car2.setCarId(2);
        car2.setModel("Accord");
        car2.setManufacturer("baleno");
        car2.setRegistrationNumber("KA19873456");
        car2.setEngineType("Deisel");
        car2.setManufactureYear(1997);
        car2.setDescription("the bad car");
        car2.setVinNumber("12349");
        car2.setUserId(2);
        car2.setOverallAvgMileage(453.6788);
        car2.setActive(true);

        List<Car> mockCars = Arrays.asList(car1, car2);
        when(carRepository.findAll()).thenReturn(mockCars);

        List<CarDto> resultCarDtos = carServiceImpl.getAllCars();

        assertNotNull(resultCarDtos);
        assertEquals(2, resultCarDtos.size());

        assertEquals("toyota", resultCarDtos.get(0).getManufacturer());
        assertEquals("12334", resultCarDtos.get(0).getModel());
        assertEquals("KA19873456", resultCarDtos.get(0).getRegistrationNumber());
        assertEquals(1994, resultCarDtos.get(0).getManufactureYear());
        assertEquals("the gud car", resultCarDtos.get(0).getDescription());
        assertEquals("56789", resultCarDtos.get(0).getVinNumber());
        assertEquals(1, resultCarDtos.get(0).getUserId());
        assertEquals(1, resultCarDtos.get(0).getCarId());
        assertEquals("Petrol", resultCarDtos.get(0).getEngineType());

        assertEquals("baleno", resultCarDtos.get(1).getManufacturer());
        assertEquals("Accord", resultCarDtos.get(1).getModel());
        assertEquals("KA19873456", resultCarDtos.get(1).getRegistrationNumber());
        assertEquals("Deisel", resultCarDtos.get(1).getEngineType());
        assertEquals(1997, resultCarDtos.get(1).getManufactureYear());
        assertEquals("the bad car", resultCarDtos.get(1).getDescription());
        assertEquals("12349", resultCarDtos.get(1).getVinNumber());
        assertEquals(2, resultCarDtos.get(1).getUserId());
        assertEquals(2, resultCarDtos.get(1).getCarId());
    }
    @Test
    @Order(11)
    void testGetAllCars_CarsNotFoundException() {
        when(carRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(CarNotFoundException.class, () -> {
            carServiceImpl.getAllCars();
        });
        verify(carRepository, times(1)).findAll();
    }
    
    @Test
    @Order(12)
    void testGetCarByIdRecordDeactivatedException() {
        Integer carId = 1;
        Car deactivatedCar = new Car();
        deactivatedCar.setCarId(1);
        deactivatedCar.setActive(false);
        when(carRepository.findById(carId)).thenReturn(java.util.Optional.of(deactivatedCar));
        assertThrows(RecordDeactivatedException.class, () -> {
        	carServiceImpl.getCarById(carId);
        });
    }
    

    @Test
    @Order(13)
    void testGetCarsByUserIdWithCars() {
        int userId = 1;
        Car car1 = new Car();
        car1.setCarId(1);
        car1.setModel("12334");
        car1.setManufacturer("toyota");
        car1.setRegistrationNumber("KA19873456");
        car1.setEngineType("Petrol");
        car1.setManufactureYear(1994);
        car1.setDescription("the gud car");
        car1.setVinNumber("56789");
        car1.setUserId(1);
        car1.setOverallAvgMileage(456.711110);
        car1.setActive(true);

        Car car2 = new Car();
        car2.setCarId(2);
        car2.setModel("Accord");
        car2.setManufacturer("baleno");
        car2.setRegistrationNumber("KA19873456");
        car2.setEngineType("Deisel");
        car2.setManufactureYear(1997);
        car2.setDescription("the bad car");
        car2.setVinNumber("12349");
        car2.setUserId(2);
        car2.setOverallAvgMileage(453.6788);
        car2.setActive(true);
        List<Car> cars = Arrays.asList(car1, car2);
        when(carRepository.findByUserId(userId)).thenReturn(cars);
        List<CarDto> result =carServiceImpl.getCarsByUserId(userId);
        assertEquals(cars.size(), result.size());
    }

    @Test
    @Order(14)
    void testGetCarsByUserIdWithNoCars() {
        int userId = 1;
        when(carRepository.findByUserId(userId)).thenReturn(Collections.emptyList());
        assertThrows(CarNotFoundException.class, () -> {
        	carServiceImpl.getCarsByUserId(userId);
        });
    }
    @Test
    @Order(15)
    void testUpdateCarRecordDeactivatedException() {
        Integer carId = 1;
        CarDto updatedCarDto = new CarDto(); 
        Car deactivatedCar = new Car();
        deactivatedCar.setCarId(1);
        deactivatedCar.setActive(false);
        when(carRepository.findById(carId)).thenReturn(Optional.of(deactivatedCar));
        assertThrows(RecordDeactivatedException.class, () -> {
        	carServiceImpl.updateCar(updatedCarDto, carId);
        });
    }
    
}







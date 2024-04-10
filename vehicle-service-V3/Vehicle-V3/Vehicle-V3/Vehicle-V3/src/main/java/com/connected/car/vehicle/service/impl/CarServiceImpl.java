package com.connected.car.vehicle.service.impl;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

import com.connected.car.vehicle.exceptions.RecordDeactivatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.connected.car.vehicle.dto.CarDto;
import com.connected.car.vehicle.dto.TripDetailsDto;
import com.connected.car.vehicle.entity.Car;
import com.connected.car.vehicle.exceptions.CarNotFoundException;
import com.connected.car.vehicle.repository.CarRepository;
import com.connected.car.vehicle.service.CarService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarServiceImpl implements CarService {

    private static final Logger logger = LoggerFactory.getLogger(CarServiceImpl.class);

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private TripDetailsImpl tripDetailsService;

    @Override
    public CarDto createCar(CarDto carDto) {
        try {
            Car car = dtoToEntity(carDto);
            car.setActive(true);
            Car savedCar = carRepository.save(car);
            CarDto savedCarDto = entityToDto(savedCar);
            logger.info("Car created successfully");
            return savedCarDto;
        } catch (Exception e) {
            logger.error("Error while creating car");
            throw e;
        }
    }

    @Override
    public CarDto getCarById(Integer carId) {
        try {
            Car car = carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException(carId));
            if(!car.isActive())
                throw new RecordDeactivatedException("The Car details is deactivated");
            CarDto carDto = entityToDto(car);
            logger.info("Retrieved car by CarId: {}", carId);

            return carDto;
        } catch (Exception e) {
            logger.error("Error while getting car by CarId");
            throw e;
        }
    }

    @Override
    @Transactional
    public List<CarDto> getAllCars() {
        try {
            List<Car> cars = carRepository.findAll();
            if (cars.isEmpty()) {
                throw new CarNotFoundException();
            }
            List<CarDto> carDtos = cars.stream().map(this::entityToDto).collect(Collectors.toList());
            logger.info("Retrieved all cars");
            return carDtos;
        } catch (Exception e) {
            logger.error("Error while getting all cars");
            throw e;
        }
    }

    @Override
    public void deleteCar(Integer carId) {
        try {
            Car car = carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException(carId));
            carRepository.delete(car);
            logger.info("Deleted car with CarId: {}", carId);
        } catch (Exception e) {
            logger.error("Error while deleting car");
            throw e;
        }
    }

    @Override
    public TripDetailsDto getRecentTripDetailsByCarId(int carId) {
        try {
            Car car = carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException(carId));
            if(!car.isActive())
                throw new RecordDeactivatedException("Car details deactivated");
            TripDetailsDto carDetails = tripDetailsService.getRecentTripDetailsByCarId(carId);
            logger.info("Retrieved recent trip details for car with carId {}", carId);
            return carDetails;
        } catch (Exception e) {
            logger.error("Error while getting recent trip details for car with carId");
            throw e;
        }
    }

    @Override
    public CarDto updateCar(CarDto carDto, Integer carId) {
        try {
            Car car = carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException(carId));
            if(!car.isActive())
                throw new RecordDeactivatedException("Car details deactivated");
            car.setModel(carDto.getModel());
            car.setDescription(carDto.getDescription());
            car.setRegistrationNumber(carDto.getRegistrationNumber());
            car.setManufacturer(carDto.getManufacturer());
            car.setManufactureYear(Integer.parseInt(String.valueOf(carDto.getManufactureYear())));
            Car updatedCar = carRepository.save(car);
            CarDto updatedCarDto = entityToDto(updatedCar);
            logger.info("Updated car with carId {}", carId);
            return updatedCarDto;
        } catch (Exception e) {
            logger.error("Error while updating car");
            throw e;
        }
    }
    @Override
    public List<CarDto> getCarsByUserId(int userId) {
        try {
            List<Car> cars = carRepository.findByUserId(userId);
            if (cars.isEmpty()) {
                throw new CarNotFoundException(userId);
            }
            List<CarDto> carDtos = cars.stream().map(this::entityToDto).collect(Collectors.toList());
            logger.info("Retrieved all cars");

            return carDtos;
        } catch (Exception e) {
            logger.error("Error while getting all cars");
            throw e;
        }
    }
    
    


    private Car dtoToEntity(CarDto carDto) {
        Car car = new Car();
        car.setCarId(carDto.getCarId());
        car.setModel(carDto.getModel());
        car.setDescription(carDto.getDescription());
        car.setRegistrationNumber(carDto.getRegistrationNumber());
        car.setManufacturer(carDto.getManufacturer());
        car.setManufactureYear(Integer.parseInt(String.valueOf(carDto.getManufactureYear())));
        car.setEngineType(carDto.getEngineType());
        car.setActive(carDto.isActive());
        car.setVinNumber(carDto.getVinNumber());
        car.setUserId(carDto.getUserId());
        return car;
    }

    private CarDto entityToDto(Car car) {
        CarDto carDto = new CarDto();
        carDto.setCarId(car.getCarId());
        carDto.setModel(car.getModel());
        carDto.setActive(car.isActive());
        carDto.setDescription(car.getDescription());
        carDto.setRegistrationNumber(car.getRegistrationNumber());
        carDto.setManufacturer(car.getManufacturer());
        carDto.setManufactureYear(Integer.parseInt(String.valueOf(car.getManufactureYear())));
        carDto.setEngineType(car.getEngineType());
        carDto.setVinNumber(car.getVinNumber());
        carDto.setUserId(car.getUserId());
        return carDto;
    }
}


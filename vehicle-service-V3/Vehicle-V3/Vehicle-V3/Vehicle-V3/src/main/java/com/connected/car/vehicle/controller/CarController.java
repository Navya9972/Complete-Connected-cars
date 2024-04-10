package com.connected.car.vehicle.controller;

import java.util.List;

import com.connected.car.vehicle.dto.TripDetailsDto;
import com.connected.car.vehicle.entity.Car;
import com.connected.car.vehicle.exceptions.InvalidvalueException;
import com.connected.car.vehicle.exceptions.RecordAlreadyExistsException;
import com.connected.car.vehicle.repository.CarRepository;
import com.connected.car.vehicle.response.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.connected.car.vehicle.Constants.ResponseMessage;
import com.connected.car.vehicle.dto.CarDto;
import com.connected.car.vehicle.service.CarService;

@RestController
@RequestMapping("/v1/api/vehicles")
public class CarController {

	@Autowired
	private CarService carService;

	@Autowired
	private CarRepository carRepository;

	@PostMapping("/create")
	public ResponseEntity<ApiResponse> createCar(@Valid @RequestBody CarDto carDto) throws RecordAlreadyExistsException {
		if(carRepository.findByRegistrationNumber(carDto.getRegistrationNumber())!=null)
			throw new RecordAlreadyExistsException("Car with registration number already exists");
//		if (!CarService.isValidYear(carDto.getManufactureYear()))
//			throw new InvalidvalueException("Invalid manufacture year");

		CarDto createCarDto = carService.createCar(carDto);
		ApiResponse apiResponse = new ApiResponse(ResponseMessage.CREATE_MESSAGE, true, createCarDto);
		return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
	}

	@GetMapping("/getCar/{carId}")
	public ResponseEntity<ApiResponse> getCar(@PathVariable("carId") Integer carId) {
		CarDto carDto = carService.getCarById(carId);
		ApiResponse apiResponse = new ApiResponse(ResponseMessage.FETCHING_MESSAGE, true, carDto);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/getAllCars")
	public ResponseEntity<ApiResponse> getAllCars() {
		List<CarDto> carDto = carService.getAllCars();
		ApiResponse apiResponse = new ApiResponse(ResponseMessage.FETCHING_MESSAGE, true, carDto);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@PutMapping("/update/{carId}")
	public ResponseEntity<ApiResponse> updateCar(@RequestBody CarDto carDto, @PathVariable("carId") Integer carId) {
		CarDto updatedCar = carService.updateCar(carDto, carId);
		ApiResponse apiResponse = new ApiResponse(ResponseMessage.UPDATE_MESSAGE, true, updatedCar);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{carId}")
	public ResponseEntity<ApiResponse> deleteCar(@PathVariable("carId") Integer carId) {
		carService.deleteCar(carId);
		ApiResponse apiResponse = new ApiResponse(ResponseMessage.DELETE_MESSAGE, true);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@GetMapping("/{carId}/recent-details")
	public ResponseEntity<ApiResponse> getRecentDetailsByCarId(@PathVariable("carId") int carId) {
		TripDetailsDto carDetails = carService.getRecentTripDetailsByCarId(carId);
		if (carDetails == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		ApiResponse apiResponse = new ApiResponse(ResponseMessage.FETCHING_MESSAGE, true, carDetails);
		return new ResponseEntity<>(apiResponse, HttpStatus.OK);
	}

	@PutMapping("/deactivate/car")
	public ResponseEntity<ApiResponse> deactivateCar(@RequestParam int carId)
	{
		Car car=carRepository.findByCarId(carId);
		car.setActive(false);
		Car carDel=carRepository.save(car);
		ApiResponse apiResponse=new ApiResponse(ResponseMessage.CAR_DELETED_SUCCESS,true,carDel);
		return new ResponseEntity<>(apiResponse,HttpStatus.    OK);
	}
	@GetMapping("/getCarsByUserId/{userId}")
	public ResponseEntity<ApiResponse> getCarsByUserId(@PathVariable("userId") Integer userId) {
		List<CarDto> cars = carService.getCarsByUserId(userId);
		ApiResponse apiResponse = new ApiResponse(ResponseMessage.FETCHING_MESSAGE, true, cars);
		return ResponseEntity.ok(apiResponse);
	}

}

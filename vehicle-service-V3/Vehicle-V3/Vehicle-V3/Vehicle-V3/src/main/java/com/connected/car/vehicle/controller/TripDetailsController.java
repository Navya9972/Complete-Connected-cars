package com.connected.car.vehicle.controller;

import com.connected.car.vehicle.Constants.ResponseMessage;
import com.connected.car.vehicle.dto.TripDetailsDto;
import com.connected.car.vehicle.entity.Car;
import com.connected.car.vehicle.exceptions.CarNotFoundException;
import com.connected.car.vehicle.exceptions.InvalidvalueException;
import com.connected.car.vehicle.repository.CarRepository;
import com.connected.car.vehicle.response.ApiResponse;
import com.connected.car.vehicle.service.TripDetailsService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/vehicles")
public class TripDetailsController {

    @Autowired
    private TripDetailsService tripDetailsService;

    @Autowired
    private CarRepository carRepository;


    @PostMapping("/createTripDetails/{registrationNumber}")
    public ResponseEntity<ApiResponse> createTripDetails(@Valid @RequestBody TripDetailsDto tripDetailsDto, @PathVariable (name ="registrationNumber")String registrationNumber){
        if(carRepository.findByRegistrationNumber(registrationNumber)==null)
            throw new CarNotFoundException(registrationNumber);
        Car car=carRepository.findByRegistrationNumber(registrationNumber);
        int carId=car.getCarId();
        tripDetailsDto.setCarId(carId);
        if(tripDetailsDto.getMaxSpeed()<tripDetailsDto.getMinSpeed())
            throw new InvalidvalueException("Max speed cannot be less than min speed");
        if(tripDetailsDto.getInitialFuelReading()<tripDetailsDto.getCurrentFuelReading())
            throw new InvalidvalueException("Initial fuel reading cannot be less than current fuel reading");
        if(tripDetailsDto.getOdometerStartReading()>tripDetailsDto.getOdometerEndReading())
            throw new InvalidvalueException("Oddometer start reading cannot be greater than oddometer end reading");
        TripDetailsDto createTripDetailsDto=tripDetailsService.createTripDetails(tripDetailsDto);
    	ApiResponse apiResponse = new ApiResponse(ResponseMessage.FETCHING_MESSAGE, true, createTripDetailsDto);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    
    @GetMapping("/getTripDetails/{tripDetailsId}")
    public ResponseEntity<ApiResponse> getTripDetails(@PathVariable("tripDetailsId") long tripDetailsId){
        TripDetailsDto tripDetails=tripDetailsService.getTripDetailsById(tripDetailsId);
    	ApiResponse apiResponse = new ApiResponse(ResponseMessage.FETCHING_MESSAGE, true, tripDetails);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    
    @GetMapping("/getAllTripDetails")
    public ResponseEntity<ApiResponse> getAllTripDetails(){
    	List<TripDetailsDto> tripDetails=tripDetailsService.getAllTripDetails();
    	ApiResponse apiResponse = new ApiResponse(ResponseMessage.FETCHING_MESSAGE, true, tripDetails);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/cars/{registrationNumber}")
    public ResponseEntity<ApiResponse> getTripDetailsByRegistrationNumber(@PathVariable("registrationNumber") String registrationNumber){
        Car car=carRepository.findByRegistrationNumber(registrationNumber);
        if(car==null)
            throw new CarNotFoundException(registrationNumber);
        int carId=car.getCarId();
    	List<TripDetailsDto> tripDetails=tripDetailsService.getTripDetailsByCarId(carId);
    	ApiResponse apiResponse = new ApiResponse(ResponseMessage.FETCHING_MESSAGE, true, tripDetails);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/deleteTripDetails/{tripDetailsId}")
    public ResponseEntity<ApiResponse> deleteTripDetails(@PathVariable("tripDetailsId")Integer tripDetailsId){
        tripDetailsService.deleteTripDetails(tripDetailsId);
    	ApiResponse apiResponse = new ApiResponse(ResponseMessage.FETCHING_MESSAGE, true);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
      
    }


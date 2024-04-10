package com.connected.car.vehicle.controller;

import com.connected.car.vehicle.Constants.ResponseMessage;
import com.connected.car.vehicle.dto.CarSummaryDto;
import com.connected.car.vehicle.entity.CarSummary;
import com.connected.car.vehicle.entity.TripDetails;
import com.connected.car.vehicle.exceptions.InvalidvalueException;
import com.connected.car.vehicle.exceptions.ResourceNotFoundException;
import com.connected.car.vehicle.repository.CarRepository;
import com.connected.car.vehicle.repository.CarSummaryRepository;
import com.connected.car.vehicle.repository.TripDetailsRepository;
import com.connected.car.vehicle.response.ApiResponse;

import com.connected.car.vehicle.service.impl.CarSummaryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/api/vehicles/carSummary")
public class CarSummaryController {

    @Autowired
    private CarSummaryImpl carSummaryImpl;
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarSummaryRepository carSummaryRepository;

    @Autowired
    private TripDetailsRepository tripDetailsRepository;

    ApiResponse apiResponse;

    @GetMapping("/generateCarSummary")
    public ResponseEntity<ApiResponse> generateCarSummary(@RequestParam (name = "carId") int carId, @RequestParam (name = "tripDetailsId")long tripDetailsId) throws Exception {
        if(carRepository.findByCarId(carId)==null || tripDetailsRepository.findByTripDetailsId(tripDetailsId)==null )
            throw new InvalidvalueException("Invalid carid or trip details id");
        CarSummaryDto carSummaryDto=carSummaryImpl.generateCarSummary(carId,tripDetailsId);
        ApiResponse apiResponse=new ApiResponse(ResponseMessage.CALCULATION_SUCCESS_MESSAGE,true, carSummaryDto);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

   @GetMapping("/listOfTripsForCar")
   public ResponseEntity<ApiResponse> getListOfTripDetailsForCarId(@RequestParam (name ="carId")int carId)
   {
       List<TripDetails> tripDetailsList =carSummaryImpl.listOfTripForCarId(carId);
       //System.out.println(tripDetailsList);
       apiResponse=new ApiResponse(ResponseMessage.TRIP_DETAILS_FETCH_MESSAGE, true, tripDetailsList);
       return new ResponseEntity<>(apiResponse,HttpStatus.OK);
   }


    @GetMapping("/calculateMileage")
    public ResponseEntity<ApiResponse> calculateMileage(@RequestParam (name = "carId")int carId, @RequestParam (name = "tripDeatilsId") long tripDetailsId) throws Exception {

        double mileage=carSummaryImpl.calculateMileage(carId,tripDetailsId);
        apiResponse=new ApiResponse(ResponseMessage.CALCULATION_SUCCESS_MESSAGE,true,mileage);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/calculate-fuel-consumption")
    public ResponseEntity<ApiResponse> calculateFuelConsumption(@RequestParam (name = "carId") int carId, @RequestParam (name = "tripDeatilsId")  long tripDetailsId) {
        double fuelConsumption = carSummaryImpl.calculateFuelConsumption(carId,tripDetailsId);
        apiResponse=new ApiResponse(ResponseMessage.FUEL_CONSUMPTION_SUCCESS,true,fuelConsumption);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/calculateCurrentAvgMileage")
    public ResponseEntity<ApiResponse> calculatePresentAvgMileage(@RequestParam (name = "carId")int carId){
       List<TripDetails> tripDetailsList =carSummaryImpl.listOfTripForCarId(carId);
       List<Long> tripDetailsIdList=new ArrayList<>();
       for(int i = 0; i< tripDetailsList.size(); i++) {
           tripDetailsIdList.add(tripDetailsList.get(i).getTripDetailsId());
       }

      double avgMileage= carSummaryImpl.calculatePresentAvgMileage(carId, tripDetailsIdList);
       ApiResponse apiResponse=new ApiResponse(ResponseMessage.CALCULATION_SUCCESS_MESSAGE,true,avgMileage);
       return new ResponseEntity<>(apiResponse,HttpStatus.OK);

    }


    @GetMapping("/calculate-totalDistance")
    public ResponseEntity<ApiResponse> calculateTotalDistance(@RequestParam (name = "carId")int carId, @RequestParam  (name = "triPDetailsId")long tripDetailsId) {
        double TotalDistance = carSummaryImpl.calculateFuelConsumption(carId,tripDetailsId);
        apiResponse=new ApiResponse(ResponseMessage.Distance_Calculation_Success,true,TotalDistance);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/calculate-Duration")
    public ResponseEntity<ApiResponse> calculateDuration(@RequestParam (name = "carId") int carId, @RequestParam (name = "tripDetailsId")long tripDetailsId) {
        int duration = carSummaryImpl.calculateDuration(carId, tripDetailsId);
        apiResponse=new ApiResponse(ResponseMessage.DURATION_CALCULATION_SUCCESS,true,"Duration:" +duration);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @GetMapping("/StartDateRange")
    public ResponseEntity<ApiResponse> getFindByTravelStartDateBetween(@RequestParam("startDate") LocalDateTime startDate,
                                                                       @RequestParam("endDate") LocalDateTime endDate){
        List<CarSummary> carSummaries=carSummaryImpl.getFindByTravelStartDateBetween(startDate,endDate);
        apiResponse=new ApiResponse("Date range car summary details",true,carSummaries);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

    @GetMapping("/getIdleTime/{tripDetailsId}")
    public ResponseEntity<ApiResponse> getTotalIdleTime(@PathVariable (name = "tripDetailsId") Long tripDetailsId) {
        CarSummary carSummary = carSummaryRepository.findByTripDetailsId(tripDetailsId);
        if(carSummary==null)
            throw new ResourceNotFoundException("Car summary for given trip details id is not found");
        System.out.println(carSummary.getIdleTime());
        ApiResponse response = new ApiResponse("success", true, "Idle time: " + carSummary.getIdleTime());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

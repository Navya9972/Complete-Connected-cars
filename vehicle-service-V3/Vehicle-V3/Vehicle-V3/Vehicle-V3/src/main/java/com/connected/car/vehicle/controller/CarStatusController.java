package com.connected.car.vehicle.controller;

import com.connected.car.vehicle.Constants.ResponseMessage;
import com.connected.car.vehicle.dto.CarStatusDto;
import com.connected.car.vehicle.entity.CarStatus;
import com.connected.car.vehicle.repository.CarStatusRepository;
import com.connected.car.vehicle.repository.TripDetailsRepository;
import com.connected.car.vehicle.response.ApiResponse;
import com.connected.car.vehicle.service.CarStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/vehicles")
public class CarStatusController {


    @Autowired
    private CarStatusService carStatusService;
    @Autowired
    private TripDetailsRepository tripDetailsRepository;

    @Autowired
    private CarStatusRepository carStatusRepository;


        @GetMapping("/getCarStatus/By/carId/{carId}")
        public ResponseEntity<ApiResponse> getCarStatusByCarId(@PathVariable (name ="carId") int carId) {
        CarStatusDto carStatus=carStatusService.getCarStatusDetails(carId);
        ApiResponse response=new ApiResponse(ResponseMessage.CAR_STATUS_DETAILS,true,carStatus);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/deactivate/carStatus")
    public ResponseEntity<ApiResponse> deactivateCarStatus(@RequestParam (name ="carId") int carId)
    {
        CarStatus carStatus=carStatusRepository.findByCarId(carId);
        carStatus.setActive(false);
        CarStatus carStatusDel=carStatusRepository.save(carStatus);
        ApiResponse apiResponse=new ApiResponse(ResponseMessage.CAR_STATUS_DELETE,true,carStatusDel);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }

}

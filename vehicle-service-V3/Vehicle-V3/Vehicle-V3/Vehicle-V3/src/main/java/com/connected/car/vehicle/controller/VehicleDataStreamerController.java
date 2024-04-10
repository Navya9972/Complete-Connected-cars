package com.connected.car.vehicle.controller;


import com.connected.car.vehicle.response.ApiResponse;
import com.connected.car.vehicle.service.VehicleDataStreamerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/vehicleDataStreamer")
public class VehicleDataStreamerController {

    @Autowired
    private VehicleDataStreamerService vehicleDataStreamerService;
    @PutMapping("/setEngineStatusActive/{tripDetailId}")
    public ResponseEntity<ApiResponse> setEngineStatusActive(@PathVariable("tripDetailId") Long tripDetailId) {
        vehicleDataStreamerService.setEngineStatusActive(tripDetailId);
        ApiResponse response = new ApiResponse("Engine activated",true, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/setEngineStatusInActive/{tripDetailId}")
    public ResponseEntity<ApiResponse> setEngineStatusInActive(@PathVariable("tripDetailId") Long tripDetailId) {
        vehicleDataStreamerService.setEngineStatusInActive(tripDetailId);
        ApiResponse response = new ApiResponse("Engine Inactivated",true,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/updateCurrentSpeed/{tripDetailId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestParam(name ="currentSpeed") Integer currentSpeed,
                                                  @PathVariable("tripDetailId")  Long tripDetailId) {
        int updateCurrentSpeed = vehicleDataStreamerService.updateCurrentSpeed(currentSpeed,tripDetailId);
        ApiResponse response = new ApiResponse("current speed: " + updateCurrentSpeed,true,"");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}


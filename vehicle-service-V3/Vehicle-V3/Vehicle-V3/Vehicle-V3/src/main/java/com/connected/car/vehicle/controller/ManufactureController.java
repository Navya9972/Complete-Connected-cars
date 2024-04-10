package com.connected.car.vehicle.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.connected.car.vehicle.response.ApiResponse;
import com.connected.car.vehicle.service.impl.ManufacturerServiceImpl;
import com.connected.car.vehicle.Constants.ResponseMessage;
import com.connected.car.vehicle.dto.ManufacturerDto;
import com.connected.car.vehicle.entity.Manufacturer;
 
@RestController
@RequestMapping("/v1/api/vehicles")
public class ManufactureController {
 
	@Autowired
	private ManufacturerServiceImpl ManufacturerService;
 
	@GetMapping("/getAllManufacturer")
	public ResponseEntity<ApiResponse> getManufacturer() {
		List<ManufacturerDto> manufacturerer = ManufacturerService.GetAllManufacturer();
		ApiResponse apiResponse = new ApiResponse(ResponseMessage.FETCHING_MESSAGE, true, manufacturerer);
		return ResponseEntity.ok(apiResponse);
 
	}
 
	@GetMapping("/getmanufacturerById/{id}")
	public ResponseEntity<ApiResponse> getManufacturerById(@PathVariable (name = "id") int id) {
		ManufacturerDto manufacturerer = ManufacturerService.GetManufacturer(id);
		ApiResponse apiResponse = new ApiResponse(ResponseMessage.FETCHING_MESSAGE, true, manufacturerer);
		return ResponseEntity.ok(apiResponse);
	}
}
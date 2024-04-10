package com.connected.car.vehicle.service;

import com.connected.car.vehicle.dto.CarStatusDto;
import com.connected.car.vehicle.entity.CarStatus;

public interface CarStatusService {
	   public CarStatusDto getCarStatusDetails(int carId);

}

package com.connected.car.vehicle.service;
import java.util.List;

import com.connected.car.vehicle.dto.ManufacturerDto;

public interface ManufacturerService {

		public List<ManufacturerDto> GetAllManufacturer();
		public ManufacturerDto GetManufacturer(int id) ;
	}


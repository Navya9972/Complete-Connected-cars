package com.connected.car.vehicle.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
 
import com.connected.car.vehicle.entity.Manufacturer;
import com.connected.car.vehicle.dto.ManufacturerDto;
import com.connected.car.vehicle.exceptions.CarNotFoundException;
import com.connected.car.vehicle.exceptions.ManufacturerNotFoundException;
import com.connected.car.vehicle.repository.ManufacturerRepository;
import com.connected.car.vehicle.service.ManufacturerService;
 
@Service
public class ManufacturerServiceImpl implements ManufacturerService{
 
    private static final Logger logger = LoggerFactory.getLogger(ManufacturerServiceImpl.class);
	
    @Autowired
	private ManufacturerRepository  manufacturerRepo;
	
	public List<ManufacturerDto> GetAllManufacturer(){
        try {
            List<Manufacturer> manufacturer = manufacturerRepo.findAll();
            if (manufacturer.isEmpty()) {
                throw new CarNotFoundException();
            }
            List<ManufacturerDto> manufacturerDto = manufacturer.stream().map(this::entityToDto).toList();
 
            logger.info("Retrieved all Manufacturer");
 
            return manufacturerDto;
        } catch (Exception e) {
            logger.error("Error while getting all cars");
            throw e;
        }
	}

 
 
	@Override
	public ManufacturerDto GetManufacturer(int id) {
	    try {
	        Manufacturer manufacturer = manufacturerRepo.findById(id)
	                .orElseThrow(() -> new ManufacturerNotFoundException(id));
	        return entityToDto(manufacturer);
	    } catch (ManufacturerNotFoundException e) {
	        throw e;
	    }
	}

	   private ManufacturerDto entityToDto(Manufacturer manufacturer) {
		   ManufacturerDto manufacturersDto = new ManufacturerDto();
		   manufacturersDto.setId(manufacturer.getId());
           manufacturersDto.setManufacturer(manufacturer.getManufacturer());
	        return manufacturersDto;
	    }

}


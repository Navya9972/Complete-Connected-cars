package com.connected.car.vehicle.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.connected.car.vehicle.entity.Manufacturer;

public interface ManufacturerRepository  extends JpaRepository<Manufacturer, Integer>{
    
	Optional<Manufacturer> findById(int id);
	 public  List<Manufacturer> findAll();
	 
}

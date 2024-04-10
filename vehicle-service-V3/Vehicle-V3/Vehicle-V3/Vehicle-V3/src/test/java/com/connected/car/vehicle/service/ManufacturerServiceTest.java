package com.connected.car.vehicle.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.connected.car.vehicle.dto.ManufacturerDto;
import com.connected.car.vehicle.entity.Manufacturer;
import com.connected.car.vehicle.exceptions.CarNotFoundException;
import com.connected.car.vehicle.exceptions.ManufacturerNotFoundException;
import com.connected.car.vehicle.repository.ManufacturerRepository;
import com.connected.car.vehicle.service.impl.ManufacturerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ManufacturerServiceTest{
	
@Mock
private ManufacturerRepository manufacturerRepo;

@InjectMocks
private ManufacturerServiceImpl manufacturerService;

@Test
void testGetAllManufacturerWithManufacturers() {
    List<Manufacturer> manufacturers = List.of(new Manufacturer(), new Manufacturer());
    when(manufacturerRepo.findAll()).thenReturn(manufacturers);
    List<ManufacturerDto> result = manufacturerService.GetAllManufacturer();
    assertEquals(manufacturers.size(), result.size());
}

@Test
void testGetAllManufacturerWithNoManufacturers() {
    when(manufacturerRepo.findAll()).thenReturn(Collections.emptyList());
    assertThrows(CarNotFoundException.class, () -> manufacturerService.GetAllManufacturer());
}

@Test
void testGetManufacturer() {
    int manufacturerId = 1;
    Manufacturer manufacturer = new Manufacturer();
    manufacturer.setId(manufacturerId);
    when(manufacturerRepo.findById(manufacturerId)).thenReturn(Optional.of(manufacturer));
    ManufacturerDto result = manufacturerService.GetManufacturer(manufacturerId);
    assertNotNull(result);
    assertEquals(manufacturerId, result.getId());
}

@Test
void testGetManufacturerNotFound() {
    int manufacturerId = 1;
    when(manufacturerRepo.findById(manufacturerId)).thenReturn(Optional.empty());
    assertThrows(ManufacturerNotFoundException.class, () -> manufacturerService.GetManufacturer(manufacturerId));
}
}

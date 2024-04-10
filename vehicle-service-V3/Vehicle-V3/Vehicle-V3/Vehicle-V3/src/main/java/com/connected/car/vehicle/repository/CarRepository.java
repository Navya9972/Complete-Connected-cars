package com.connected.car.vehicle.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.connected.car.vehicle.entity.Car;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer>{

    public Car findByCarId(long carId);
    public Car findByRegistrationNumber(String registrationNumber);

    List<Car> findByUserId(int userId);



}

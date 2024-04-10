package com.connected.car.vehicle.repository;

import com.connected.car.vehicle.entity.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarStatusRepository extends JpaRepository<CarStatus, Long> {
    CarStatus findByCarId(long carId);
}

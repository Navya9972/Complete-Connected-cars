package com.connected.car.vehicle.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import com.connected.car.vehicle.entity.VehicleDataStreamer;

public interface VehicleDataStreamerRepository extends JpaRepository<VehicleDataStreamer, Integer>{
    @Procedure(name="getVehicleDataStreamer")
    Optional<VehicleDataStreamer> findByTripDetailsId(@Param("trip_details_id") Long tripDetailsId);
}
 
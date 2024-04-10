package com.connected.car.vehicle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name="car_status")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class CarStatus {
    @Id
    @GeneratedValue
    @Column(name = "car_status_id")
    private long carStatusId;

    @Column(name = "fuel_status")
    private int fuelStatus;


    @Column(name = "car_id")
    private int carId;

    @Column(name = "lock_status")
    private boolean lockStatus;

    @Column(name = "battery_status")
    @Enumerated(EnumType.STRING)
    private batteryStatus batteryStatus;
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
    @CreatedDate
    @Column(name = "created_datetime")
    private LocalDateTime createdDateTime;
    @LastModifiedBy
    @Column(name = "modified_by")
    private String updatedBy;
    @LastModifiedDate
    @Column(name = "modified_datetime")
    private LocalDateTime updatedDatetime;

	private boolean active;

}

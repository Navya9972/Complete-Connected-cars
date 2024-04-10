package com.connected.car.vehicle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="car")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "car_id")
	private int carId;

	private String model;
	
	private String description;
	@Column(name = "registration_number")
	private String registrationNumber;

	private String manufacturer;
	@Column(name = "manufacture_year")
	private int manufactureYear;

	@Column(name = "avg_mileage")
	private double overallAvgMileage;

	@Column(name = "engine_type")
	private String engineType;

	@Column(name = "vin_number")
	private String vinNumber;

	@Column(name="created_by")
	private String createdBy;

	@Column(name = "created_datetime")
	private LocalDateTime createdDatetime;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name = "modified_datetime")
	private LocalDateTime modifiedDatetime;

	@Column(name = "user_id")
	private int userId;

	private boolean active;

	
}

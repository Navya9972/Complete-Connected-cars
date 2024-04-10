package com.connected.car.vehicle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "car_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarSummary {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name = "car_summary_id")
	private long carSummaryId;

	@Column(name = "trip_details_id")
	private long tripDetailsId;

	@Column(name = "milage")
	private double mileage;

	@Column(name = "avg_milage")
	private double overallAvgMileage;
	
	@Column(name = "fuel_consumption")
	private double fuelConsumption;
	
	@Column(name = "total_distance")
	private double totalDistance;
	
	@Column(name = "idle_time")
	private String idleTime;
	
	@Column(name = "duration_in_minutes")
	private int duration;
	
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
	private LocalDateTime updatedDateTime;
	
	private boolean active;


}

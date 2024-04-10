package com.connected.car.vehicle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="manufacturers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Manufacturer {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		
		@Column(name = "id")
		private int id;
        
		@Column(name ="manufacturer")
		private String manufacturer;
		
}

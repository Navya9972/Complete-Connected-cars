package com.connected.car.vehicle.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ManufacturerDto {
	private int id;
	@NotNull
	private String manufacturer;
}

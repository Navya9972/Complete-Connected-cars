package com.connected.car.user.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class Address {

	private String street;
	private String city;
	private String state;
	private String zipCode;
	private String country;

}

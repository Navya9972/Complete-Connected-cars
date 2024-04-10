package com.connected.car.vehicle.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

	private String message;
	private boolean success;
	Object responseData;
	
	public ApiResponse(String message, boolean b) {
		this.message=message;
		this.success=b;
	}



}

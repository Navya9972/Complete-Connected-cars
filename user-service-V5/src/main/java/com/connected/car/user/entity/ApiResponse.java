package com.connected.car.user.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Getter
@Setter
public class ApiResponse {
	private boolean responseSuccess;
	private String responseMessage;
	private Object responseData;

	public ApiResponse(boolean responseSuccess, String responseMessage, HttpStatus responseStatus, Object responseData) {
		super();
		
		this.responseSuccess = responseSuccess;
		this.responseMessage = responseMessage;
		this.responseData = responseData;
	}

	public ApiResponse(boolean responseSuccess, String responseMessage, Object responseData) {
		super();
		
		this.responseSuccess = responseSuccess;
		this.responseMessage = responseMessage;
		this.responseData = responseData;
	}


	public ApiResponse(boolean responseSuccess, String responseMessage) {
		super();
		this.responseSuccess = responseSuccess;
		this.responseMessage = responseMessage;
	}

	@Override
	public String toString() {
		return "ApiResponse [responseSuccess=" + responseSuccess + ", responseMessage=" + responseMessage
				+ ", responseData=" + responseData + "]";
	}

	
	

}

package com.connected.car.user.payload;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Errorresponse {

	private boolean responseSuccess;
	private String responseMessage;
	private HttpStatus responseStatus;
}

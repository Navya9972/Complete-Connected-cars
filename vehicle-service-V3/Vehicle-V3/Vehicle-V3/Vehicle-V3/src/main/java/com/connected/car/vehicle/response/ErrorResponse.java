package com.connected.car.vehicle.response;


import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorResponse {
    private boolean ApiSuccess;
    private String responseMessage;
    private HttpStatus responseStatus;
}

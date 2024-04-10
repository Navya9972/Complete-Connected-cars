package com.connected.car.vehicle.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.connected.car.vehicle.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlerResourceNotFoundException(ResourceNotFoundException ex) {
		String responseMessage = ex.getMessage();
		ErrorResponse response = ErrorResponse.builder().ApiSuccess(true).responseMessage(responseMessage)
				.responseStatus(HttpStatus.NOT_FOUND).build();
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	// For Handling User not found exception
	@ExceptionHandler(CarNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlerUserNotFoundException(CarNotFoundException ex) {
		String responseMessage = ex.getMessage();
		ErrorResponse response = ErrorResponse.builder().ApiSuccess(true).responseMessage(responseMessage)
				.responseStatus(HttpStatus.NOT_FOUND).build();
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	// For Handling Engine already active exception
	@ExceptionHandler(EngineAlreadyActiveException.class)
	public ResponseEntity<ErrorResponse> handlerEngineAlreadyActiveException(EngineAlreadyActiveException ex) {
		String responseMessage = ex.getMessage();
		ErrorResponse response = ErrorResponse.builder().ApiSuccess(true).responseMessage(responseMessage)
				.responseStatus(HttpStatus.BAD_REQUEST).build();
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	// For Handling Invalid fuel value
	@ExceptionHandler(InvalidvalueException.class)
	public ResponseEntity<ErrorResponse> handlerInvalidFuelvalueException(InvalidvalueException ex) {
		String responseMessage = ex.getMessage();
		ErrorResponse response = ErrorResponse.builder().ApiSuccess(true).responseMessage(responseMessage)
				.responseStatus(HttpStatus.BAD_REQUEST).build();
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	// For Handling Record de-activation exception
	@ExceptionHandler(RecordDeactivatedException.class)
	public ResponseEntity<ErrorResponse> handlerRecordDeactivatedException(RecordDeactivatedException ex) {
		String responseMessage = ex.getMessage();
		ErrorResponse response = ErrorResponse.builder().ApiSuccess(true).responseMessage(responseMessage)
				.responseStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// For Handling the Exception while fetching the details trip
	@ExceptionHandler(TripDetailsNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlerTripDetailsNotFoundException(TripDetailsNotFoundException ex) {
		String responseMessage = ex.getMessage();
		ErrorResponse response = ErrorResponse.builder().ApiSuccess(true).responseMessage(responseMessage)
				.responseStatus(HttpStatus.NOT_FOUND).build();
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	// For Handling Engine already Inactive
	@ExceptionHandler(EngineAlreadyInActiveException.class)
	public ResponseEntity<ErrorResponse> handlerEngineAlreadyInActiveException(EngineAlreadyInActiveException ex) {
		String responseMessage = ex.getMessage();
		ErrorResponse response = ErrorResponse.builder().ApiSuccess(true).responseMessage(responseMessage)
				.responseStatus(HttpStatus.BAD_REQUEST).build();
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	// For Handling general exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handlerException(Exception e) {
		String responseMessage = "An error occurred: " + e.getMessage();
		ErrorResponse response = ErrorResponse.builder().ApiSuccess(false).responseMessage(responseMessage)
				.responseStatus(HttpStatus.INTERNAL_SERVER_ERROR).build();
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(RecordAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handlerRecordAlreadyExistsException(RecordAlreadyExistsException recordAlreadyExistsException)
	{
		String responseMessage=recordAlreadyExistsException.getMessage();
		ErrorResponse response = ErrorResponse.builder().ApiSuccess(true).responseMessage(responseMessage)
				.responseStatus(HttpStatus.CONFLICT).build();
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);

	}
	
	   //to handle validation exception
     @ExceptionHandler(MethodArgumentNotValidException.class)
	 public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex)
     { 
      String responseMessage = "Validation error: " + ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
	  ErrorResponse response = ErrorResponse.builder().ApiSuccess(true).responseMessage(responseMessage)
						.responseStatus(HttpStatus.NOT_ACCEPTABLE).build();
	  return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
	 }

}



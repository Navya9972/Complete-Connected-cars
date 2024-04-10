package com.connected.car.vehicle.GlobalExceptionHandler;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.connected.car.vehicle.response.ErrorResponse;
import com.connected.car.vehicle.exceptions.CarNotFoundException;
import com.connected.car.vehicle.exceptions.EngineAlreadyActiveException;
import com.connected.car.vehicle.exceptions.EngineAlreadyInActiveException;
import com.connected.car.vehicle.exceptions.GlobalExceptionHandler;
import com.connected.car.vehicle.exceptions.InvalidvalueException;
import com.connected.car.vehicle.exceptions.RecordAlreadyExistsException;
import com.connected.car.vehicle.exceptions.RecordDeactivatedException;
import com.connected.car.vehicle.exceptions.ResourceNotFoundException;
import com.connected.car.vehicle.exceptions.TripDetailsNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private ResourceNotFoundException resourceNotFoundException;


    @Mock
    private CarNotFoundException carNotFoundException;

    @Mock
    private EngineAlreadyActiveException engineAlreadyActiveException;

    @Mock
    private InvalidvalueException invalidValueException;
    
    @Mock
    private RecordDeactivatedException recordDeactivatedException;

    @Mock
    private TripDetailsNotFoundException tripDetailsNotFoundException;

    @Mock
    private EngineAlreadyInActiveException engineAlreadyInActiveException;
    
    @Mock
    private Exception generalException;

    @Mock
    private RecordAlreadyExistsException recordAlreadyExistsException;

    @Mock
    private MethodArgumentNotValidException validationException;


    @Test
    public void testHandlerResourceNotFoundException() {
        String errorMessage = "Resource not found";
        when(resourceNotFoundException.getMessage()).thenReturn(errorMessage);
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerResourceNotFoundException(resourceNotFoundException);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(true, response.getBody().isApiSuccess());
        assertEquals(errorMessage, response.getBody().getResponseMessage());
    }
    
    
    @Test
    void testHandlerCarNotFoundException() {
        String errorMessage = "Car not found";
        when(carNotFoundException.getMessage()).thenReturn(errorMessage);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerUserNotFoundException(carNotFoundException);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(true, response.getBody().isApiSuccess());
        assertEquals(errorMessage, response.getBody().getResponseMessage());
    }

    @Test
    void testHandlerEngineAlreadyActiveException() {
        String errorMessage = "Engine already active";
        when(engineAlreadyActiveException.getMessage()).thenReturn(errorMessage);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerEngineAlreadyActiveException(engineAlreadyActiveException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(true, response.getBody().isApiSuccess());
        assertEquals(errorMessage, response.getBody().getResponseMessage());
    }

    @Test
    void testHandlerInvalidFuelValueException() {
        String errorMessage = "Invalid fuel value";
        when(invalidValueException.getMessage()).thenReturn(errorMessage);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerInvalidFuelvalueException(invalidValueException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(true, response.getBody().isApiSuccess());
        assertEquals(errorMessage, response.getBody().getResponseMessage());
    }
    
    @Test
    void testHandlerRecordDeactivatedException() {
        String errorMessage = "Record deactivated";
        when(recordDeactivatedException.getMessage()).thenReturn(errorMessage);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerRecordDeactivatedException(recordDeactivatedException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(true, response.getBody().isApiSuccess());
        assertEquals(errorMessage, response.getBody().getResponseMessage());
    }

    @Test
    void testHandlerTripDetailsNotFoundException() {
        String errorMessage = "Trip details not found";
        when(tripDetailsNotFoundException.getMessage()).thenReturn(errorMessage);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerTripDetailsNotFoundException(tripDetailsNotFoundException);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(true, response.getBody().isApiSuccess());
        assertEquals(errorMessage, response.getBody().getResponseMessage());
    }

    @Test
    void testHandlerEngineAlreadyInActiveException() {
        String errorMessage = "Engine already inactive";
        when(engineAlreadyInActiveException.getMessage()).thenReturn(errorMessage);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerEngineAlreadyInActiveException(engineAlreadyInActiveException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(true, response.getBody().isApiSuccess());
        assertEquals(errorMessage, response.getBody().getResponseMessage());
    }
    
    @Test
    void testHandlerException() {
        String errorMessage = "An error occurred: Test exception";
        when(generalException.getMessage()).thenReturn("Test exception");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerException(generalException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(false, response.getBody().isApiSuccess());
        assertEquals(errorMessage, response.getBody().getResponseMessage());
    }

    @Test
    void testHandlerRecordAlreadyExistsException() {
        String errorMessage = "Record already exists";
        when(recordAlreadyExistsException.getMessage()).thenReturn(errorMessage);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlerRecordAlreadyExistsException(recordAlreadyExistsException);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(true, response.getBody().isApiSuccess());
        assertEquals(errorMessage, response.getBody().getResponseMessage());
    }

    @Test
    @Order(5)
    void testHandleValidationException() {
        String errorMessage = "Validation error: Invalid input";
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(validationException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(new ObjectError("field", "Invalid input")));
        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleValidationException(validationException);
        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseEntity.getStatusCode());
        ErrorResponse responseBody = responseEntity.getBody();
        assertEquals(true, responseBody.isApiSuccess());
        assertEquals(errorMessage, responseBody.getResponseMessage());
        assertEquals(HttpStatus.NOT_ACCEPTABLE, responseBody.getResponseStatus());
    }
}

  
   
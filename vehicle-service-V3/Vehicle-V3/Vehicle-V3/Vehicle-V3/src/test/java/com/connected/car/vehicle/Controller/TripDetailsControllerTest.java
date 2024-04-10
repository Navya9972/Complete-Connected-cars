package com.connected.car.vehicle.Controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.connected.car.vehicle.Constants.ResponseMessage;
import com.connected.car.vehicle.controller.TripDetailsController;
import com.connected.car.vehicle.controller.VehicleDataStreamerController;
import com.connected.car.vehicle.dto.TripDetailsDto;
import com.connected.car.vehicle.entity.Car;
import com.connected.car.vehicle.repository.CarRepository;
import com.connected.car.vehicle.service.TripDetailsService;
import com.connected.car.vehicle.service.VehicleDataStreamerService;

public class TripDetailsControllerTest {

    //	@Mock
//    private VehicleDataStreamerService vehicleDataStreamerService;
    @Mock
    private TripDetailsService tripDetailsService;
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private TripDetailsController tripDetailsController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tripDetailsController).build();
    }
    @Test
    void testGetTripDetails() throws Exception {
        long tripDetailsId = 123L;
        TripDetailsDto mockTripDetails = new TripDetailsDto();
        Mockito.when(tripDetailsService.getTripDetailsById(tripDetailsId)).thenReturn(mockTripDetails);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/vehicles/getTripDetails/{tripDetailsId}", tripDetailsId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ResponseMessage.FETCHING_MESSAGE));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
//                .andReturn();
        // Verify that the service method was called with the correct arguments
//        Mockito.verify(tripDetailsService, Mockito.times(1)).getTripDetailsById(tripDetailsId);
    }
    @Test
    void testGetTripDetailsByRegistrationNumber() throws Exception {
        String registrationNumber = "ABC123";
        Car mockCar = new Car(/* Set your mock data */);
        List<TripDetailsDto> mockTripDetails = new ArrayList<>(); // Add some mock trip details
        Mockito.when(carRepository.findByRegistrationNumber(registrationNumber)).thenReturn(mockCar);
        Mockito.when(tripDetailsService.getTripDetailsByCarId(Mockito.anyInt())).thenReturn(mockTripDetails);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/vehicles/cars/{registrationNumber}", registrationNumber)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ResponseMessage.FETCHING_MESSAGE));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());
//                .andReturn();

        // Verify that the repository methods were called with the correct arguments
//        Mockito.verify(carRepository, Mockito.times(1)).findByRegistrationNumber(registrationNumber);
//        Mockito.verify(tripDetailsService, Mockito.times(1)).getTripDetailsByCarId(Mockito.anyInt());
    }
    @Test
    public void testDeleteTripDetails() throws Exception {
        Integer tripDetailsId = 123;
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/vehicles/deleteTripDetails/{tripDetailsId}", tripDetailsId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ResponseMessage.FETCHING_MESSAGE))
                .andReturn();
        Mockito.verify(tripDetailsService, Mockito.times(1)).deleteTripDetails(tripDetailsId);
    }
    @Test
    public void testGetAllTripDetails() throws Exception {
        List<TripDetailsDto> mockTripDetails = new ArrayList<>(); // Add some mock trip details
        Mockito.when(tripDetailsService.getAllTripDetails()).thenReturn(mockTripDetails);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/vehicles/getAllTripDetails")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ResponseMessage.FETCHING_MESSAGE));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
//                .andReturn();

        // Verify that the service method was called
//        Mockito.verify(tripDetailsService, Mockito.times(1)).getAllTripDetails();
    }
}

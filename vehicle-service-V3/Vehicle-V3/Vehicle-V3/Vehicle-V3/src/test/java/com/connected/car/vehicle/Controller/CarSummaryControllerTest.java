package com.connected.car.vehicle.Controller;

import com.connected.car.vehicle.controller.CarSummaryController;
import com.connected.car.vehicle.dto.CarDto;
import com.connected.car.vehicle.dto.CarSummaryDto;
import com.connected.car.vehicle.entity.Car;
import com.connected.car.vehicle.entity.CarSummary;
import com.connected.car.vehicle.entity.TripDetails;
import com.connected.car.vehicle.repository.CarRepository;
import com.connected.car.vehicle.repository.CarSummaryRepository;
import com.connected.car.vehicle.repository.TripDetailsRepository;
import com.connected.car.vehicle.service.impl.CarSummaryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CarSummaryControllerTest {

    @Mock
    private CarSummaryImpl carSummaryImpl;

    @Mock
    private CarSummaryRepository carSummaryRepository;

    @Mock
    private TripDetailsRepository tripDetailsRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarSummaryController carSummaryController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(carSummaryController).build();
    }
    
    
    @Test
    public void testGetListOfTripDetailsForCarId() throws Exception {
        int carId = 1;
        List<TripDetails> testData = Arrays.asList(new TripDetails()); // Add your test data

        when(carSummaryImpl.listOfTripForCarId(carId)).thenReturn(testData);

        mockMvc.perform(get("/v1/api/vehicles/carSummary/listOfTripsForCar")
                        .param("carId", String.valueOf(carId)) 
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseData").isArray())
                .andExpect(jsonPath("$.responseData").isNotEmpty())
                .andExpect(jsonPath("$.responseData[0]").exists()) ;
    }
      
    @Test
    public void testCalculateMileage() throws Exception {
        int carId = 1;
        long tripDetailsId = 123;
        double mileage = 50.0; 
        when(carSummaryImpl.calculateMileage(carId, tripDetailsId)).thenReturn(mileage);
        mockMvc.perform(get("/v1/api/vehicles/carSummary/calculateMileage")
                        .param("carId", String.valueOf(carId))
                        .param("tripDetailsId", String.valueOf(tripDetailsId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


    }

//    @Test
//    public void testCalculateFuelConsumption() throws Exception {
//        int carId = 1;
//        long tripDetailsId = 123;
//        double fuelConsumption = 10.0;
//        when(carSummaryImpl.calculateFuelConsumption(carId, tripDetailsId)).thenReturn(fuelConsumption);
//        mockMvc.perform(get("/v1/api/vehicles/carSummary/calculate-fuel-consumption")
//                        .param("carId", String.valueOf(carId))
//                        .param("tripDetailsId", String.valueOf(tripDetailsId))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Car Fuel consumption calculated successfully"))
//                .andExpect(jsonPath("$.success").value(true));
//
//    }


    @Test
    public void getFindByTravelStartDateBetweenTest() throws Exception {
        LocalDateTime startDate=LocalDateTime.of(2022, 1, 9, 12, 30);
        LocalDateTime endDate=LocalDateTime.of(2022, 1, 10, 12, 30);
        List<CarSummary> testData=new ArrayList<>();
        testData.add(new CarSummary());
        when(carSummaryImpl.getFindByTravelStartDateBetween(startDate,endDate)).thenReturn(testData);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/vehicles/carSummary/StartDateRange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("startDate",String.valueOf(startDate))
                        .param("endDate",String.valueOf(endDate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Date range car summary details"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseData").isArray())
                .andExpect(jsonPath("$.responseData").isNotEmpty())
                .andExpect(jsonPath("$.responseData").exists())
                .andExpect(jsonPath("$.responseData").hasJsonPath());;
    }
}

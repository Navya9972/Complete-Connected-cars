package com.connected.car.vehicle.Controller;


import com.connected.car.vehicle.controller.CarController;
import com.connected.car.vehicle.dto.CarDto;
import com.connected.car.vehicle.dto.TripDetailsDto;
import com.connected.car.vehicle.service.CarService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();
    }


//    @Test
//    public void createCarTest() throws Exception {
//        // CarDto carDto=new CarDto();
//        CarDto testData=new CarDto();
//        testData.setCarId(1);
//
//        when(carService.createCar(any(CarDto.class))).thenReturn(testData);
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/v1/api/vehicles/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(String.valueOf(CarDto.class)))
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(jsonPath("$.message").value("Created Successfully"))
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.responseData").exists())
//                .andExpect(jsonPath("$.responseData").isNotEmpty())
//                .andExpect(jsonPath("$.responseData").hasJsonPath());
//    }

    @Test
    public void getCarTest() throws Exception {
        int carId=1;
        CarDto testData=new CarDto();
        testData.setCarId(carId);
        when(carService.getCarById(carId)).thenReturn(testData);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/vehicles/getCar/{carId}",carId)
                        .content("{\"carId\": " + carId + "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.message").value("Details fetched successfully"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseData").exists())
                .andExpect(jsonPath("$.responseData").isNotEmpty())
                .andExpect(jsonPath("$.responseData").hasJsonPath());;
    }

    @Test
    public void getAllCarsTest() throws Exception {
        List<CarDto> testData=new ArrayList<>();
        when(carService.getAllCars()).thenReturn(testData);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/vehicles/getAllCars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Details fetched successfully"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseData").isArray())
                .andExpect(jsonPath("$.responseData").exists())
                .andExpect(jsonPath("$.responseData").hasJsonPath());;
    }

    @Test
    public void updateCarTest() throws Exception {
        int carId=1;
        CarDto testData=new CarDto();
        when(carService.updateCar(any(CarDto.class),eq(carId))).thenReturn(testData);
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/api/vehicles/update/{carId}",carId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"carId\": " + carId + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Details updated Successfully!"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseData").exists())
                .andExpect(jsonPath("$.responseData").isNotEmpty())
                .andExpect(jsonPath("$.responseData").hasJsonPath());


    }

    @Test
    public void deleteCarTest() throws Exception {
        int carId=1;
        doNothing().when(carService).deleteCar(eq(carId));
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/api/vehicles/delete/{carId}",carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Detail Deleted successfully"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseData").isEmpty());
    }

    @Test
    public void getRecentDetailsByCarIdFoundTest() throws Exception {
        int carId=1;
        TripDetailsDto testData=new TripDetailsDto();
        when(carService.getRecentTripDetailsByCarId(eq(carId))).thenReturn(testData);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/vehicles/{carId}/recent-details",carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Details fetched successfully"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseData").exists())
                .andExpect(jsonPath("$.responseData").hasJsonPath());
    }


    @Test
    public void getRecentDetailsByCarIdNotFoundTest() throws Exception {
        int carId=708;
        when(carService.getRecentTripDetailsByCarId(eq(carId))).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/vehicles/{carId}/recent-details",carId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.responseData").doesNotExist());
    }

    @Test
    public void getCarByUserIdTest() throws Exception{
        int userId=1;
        List<CarDto> testData=new ArrayList<>();
        when(carService.getCarsByUserId(eq(userId))).thenReturn(testData);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/vehicles/getCarsByUserId/{userId}",userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Details fetched successfully"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseData").exists())
                .andExpect(jsonPath("$.responseData").hasJsonPath());

    }





}


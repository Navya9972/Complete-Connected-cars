package com.connected.car.vehicle.Controller;

import com.connected.car.vehicle.controller.CarStatusController;
import com.connected.car.vehicle.dto.CarStatusDto;
import com.connected.car.vehicle.entity.CarStatus;
import com.connected.car.vehicle.repository.CarStatusRepository;
import com.connected.car.vehicle.repository.TripDetailsRepository;
import com.connected.car.vehicle.service.CarStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
public class CarStatusControllerTest {

    @Mock
    private CarStatusService carStatusService;

    @Mock
    private TripDetailsRepository tripDetailsRepository;

    @Mock
    private CarStatusRepository carStatusRepository;

    @InjectMocks
    private CarStatusController carStatusController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(carStatusController).build();
    }

    @Test
    public void getCarStatusByCarId() throws Exception{
        int carId=1;
        CarStatusDto testData=new CarStatusDto();
        when(carStatusService.getCarStatusDetails(eq(carId))).thenReturn(testData);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/api/vehicles/getCarStatus/By/carId/{carId}",carId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Car status details fetched successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseData").hasJsonPath());
    }


}


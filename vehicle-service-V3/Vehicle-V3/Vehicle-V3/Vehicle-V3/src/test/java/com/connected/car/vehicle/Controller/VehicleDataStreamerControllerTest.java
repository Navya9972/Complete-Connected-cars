package com.connected.car.vehicle.Controller;

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

import com.connected.car.vehicle.controller.VehicleDataStreamerController;
import com.connected.car.vehicle.service.VehicleDataStreamerService;

public class VehicleDataStreamerControllerTest {
    @Mock
    private VehicleDataStreamerService vehicleDataStreamerService;

    @InjectMocks
    private VehicleDataStreamerController vehicleDataStreamerController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp()
    {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleDataStreamerController).build();
    }
    @Test
    public void testSetEngineStatusActive() throws Exception {
        Long tripDetailId = 123L;
        Mockito.doNothing().when(vehicleDataStreamerService).setEngineStatusActive(tripDetailId);
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/api/vehicleDataStreamer/setEngineStatusActive/{tripDetailId}", tripDetailId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Engine activated"));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(""));
        // Add more assertions based on your specific response structure
//                .andReturn();
//        Mockito.verify(vehicleDataStreamerService, Mockito.times(1)).setEngineStatusActive(tripDetailId);
    }
    @Test
    void testSetEngineStatusInActive() throws Exception {
        Long tripDetailId = 123L;
        Mockito.doNothing().when(vehicleDataStreamerService).setEngineStatusInActive(tripDetailId);
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/api/vehicleDataStreamer/setEngineStatusInActive/{tripDetailId}", tripDetailId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Engine Inactivated"));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(""))
//                .andReturn();

        // Verify that the service method was called with the correct arguments
//        Mockito.verify(vehicleDataStreamerService, Mockito.times(1)).setEngineStatusInActive(tripDetailId);
    }
    @Test
    void testUpdateCurrentSpeed() throws Exception {
        int currentSpeed = 60;
        Long tripDetailId = 123L;
        int updatedSpeed = 65;  // Adjust this based on your service behavior

        Mockito.when(vehicleDataStreamerService.updateCurrentSpeed(currentSpeed, tripDetailId)).thenReturn(updatedSpeed);
        mockMvc.perform(MockMvcRequestBuilders.put("/v1/api/vehicleDataStreamer/updateCurrentSpeed/{tripDetailId}", tripDetailId)
                        .param("currentSpeed", String.valueOf(currentSpeed))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
//                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("current speed: " + updatedSpeed))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(""))
//                .andReturn();
//        Mockito.verify(vehicleDataStreamerService, Mockito.times(1)).updateCurrentSpeed(currentSpeed, tripDetailId);
    }
}

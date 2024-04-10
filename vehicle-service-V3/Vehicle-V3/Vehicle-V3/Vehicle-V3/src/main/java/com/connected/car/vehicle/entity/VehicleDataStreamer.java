package com.connected.car.vehicle.entity;



import jakarta.persistence.Column;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;

import jakarta.persistence.GenerationType;

import jakarta.persistence.Id;

import jakarta.persistence.Table;

import lombok.AllArgsConstructor;

import lombok.Getter;

import lombok.NoArgsConstructor;

import lombok.Setter;

@Entity

@Getter

@Setter

@Table(name="vehicle_data_streamer")

@NoArgsConstructor

@AllArgsConstructor

public class VehicleDataStreamer {

    @Id

    @Column(name="stream_id")

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int streamId;

    @Column(name="current_speed")

    private int currentSpeed;

    @Column(name="is_engine_active")

    private boolean isEngineActive;

    @Column(name="trip_details_id")

    private Long tripDetailsId;

    @Column(name = "active")

    private boolean active;

}



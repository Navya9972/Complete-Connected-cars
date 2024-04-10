package com.connected.car.vehicle.exceptions;

public class CarNotFoundException extends RuntimeException{



    private static final long serialVersionUID = 1L;


    public CarNotFoundException(int carId)
    {
        super(String.format("Car with carId %d not found.",carId));
    }

    public CarNotFoundException()
    {
        super("No cars found");
    }


    public CarNotFoundException(String registrationNumber) {
        super(String.format("Car with carId %s not found.",registrationNumber));
    }

}

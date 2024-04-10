package com.connected.car.vehicle.exceptions;

public class ManufacturerNotFoundException extends RuntimeException{
	 private static final long serialVersionUID = 1L;


	    public ManufacturerNotFoundException(int id)
	    {
	        super(String.format("Manufacturer with Id %d not found.",id));
	    }

	    public ManufacturerNotFoundException()
	    {
	        super("No Manufacturer found");
	    }


}

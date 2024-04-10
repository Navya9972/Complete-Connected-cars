package com.connected.car.vehicle.exceptions;



    public class TripDetailsNotFoundException extends RuntimeException{


        private static final long serialVersionUID = 1L;


        public TripDetailsNotFoundException(int carId)
        {
            super(String.format("Trip details not found for car with car ID:%d " , carId));
        }

        public TripDetailsNotFoundException(long tripDetailsId)
        {
            super(String.format("Trip details not found with Id: %d" , tripDetailsId));
        }
        public TripDetailsNotFoundException(String message)
        {
            super(message);
        }
        public TripDetailsNotFoundException()
        {
            super("No Trip details found");
        }

    }
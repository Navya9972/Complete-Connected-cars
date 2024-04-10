package com.connected.car.vehicle.exceptions;

public class EngineAlreadyInActiveException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public EngineAlreadyInActiveException(long tripDetailId) {
        super(String.format("Engine is already Inactive with trip Id: %d",tripDetailId));
    }

    public EngineAlreadyInActiveException(String message) {
        super(message);
    }

}

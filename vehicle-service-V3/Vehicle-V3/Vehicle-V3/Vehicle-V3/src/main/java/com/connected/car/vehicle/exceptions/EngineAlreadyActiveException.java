package com.connected.car.vehicle.exceptions;

public class EngineAlreadyActiveException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public EngineAlreadyActiveException(Long tripDetailId) {
        super(String.format("Engine already active with id: %d " , tripDetailId));
    }


    public EngineAlreadyActiveException(String message) {
        super(message);
    }


}
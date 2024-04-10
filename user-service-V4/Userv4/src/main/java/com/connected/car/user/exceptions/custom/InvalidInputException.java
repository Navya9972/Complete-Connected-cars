package com.connected.car.user.exceptions.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidInputException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidInputException()
	{
		super("The request contains invalid parameters or data ");
	}
	

	public InvalidInputException(String message) 
	{
		super(message);
	}
	
}

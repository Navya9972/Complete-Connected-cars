package com.connected.car.user.exceptions.custom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException()
	{
		super("Resource Not found");
	}
	
	public ResourceNotFoundException(String message)
	{
		super(message);
	}
	
}

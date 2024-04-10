package com.connected.car.user.exceptions.custom;

public class UserCreationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserCreationException()
	{
		super("Failed to create user details");
	}
	
	public UserCreationException(String message)
	{
		super(message);
	}
}
